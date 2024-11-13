package com.tot.service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tot.dao.BlockDao;
import com.tot.dao.CommentDao;
import com.tot.dao.DeletedPostDao;
import com.tot.dao.NotificationDao;
import com.tot.dao.PostDao;
import com.tot.dao.PostStatsDao;
import com.tot.dao.UserDao;
import com.tot.dao.UserStatsDao;
import com.tot.dao.VoteDao;
import com.tot.pojo.Block;
import com.tot.pojo.Comment;
import com.tot.pojo.DeletedPost;
import com.tot.pojo.Notification;
import com.tot.pojo.OptionStats;
import com.tot.pojo.Post;
import com.tot.pojo.PostAndDetails;
import com.tot.pojo.PostStats;
import com.tot.pojo.RestResponse;
import com.tot.pojo.TOTOption;
import com.tot.pojo.Tag;
import com.tot.pojo.User;
import com.tot.pojo.UserVote;
import com.tot.utils.UserIdentityUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author karthik on 04/12/21.
 * @project totservices
 */
@Slf4j
@Service
public class PostServiceImpl implements PostService {

	@Autowired
	PostDao postDao;
	@Autowired
	VoteDao voteDao;
	@Autowired
	UserDao userDao;
	@Autowired
	CommentDao commentDao;
	@Autowired
	PostStatsDao postStatsDao;
	@Autowired
	NotificationDao notificationDao;
	@Autowired
	BlockDao blockDao;
	@Autowired
	UserStatsDao userStatsDao;
	@Autowired
	PushService pushService;
	@Autowired
	DeletedPostDao deletedPostDao;

	public static Set<String> masterCategories = new HashSet<>();

	static {
		//Need to update in every release based on updates to interests_master table in mysql
		//TODO: find a better way to keep this list updated
		masterCategories.add("architecture");
		masterCategories.add("art");
		masterCategories.add("automobiles");
		masterCategories.add("books");
		masterCategories.add("business");
		masterCategories.add("comedy");
		masterCategories.add("design");
		masterCategories.add("earth");
		masterCategories.add("education");
		masterCategories.add("electronics");
		masterCategories.add("entertainment");
		masterCategories.add("family");
		masterCategories.add("fashion");
		masterCategories.add("fitness");
		masterCategories.add("food");
		masterCategories.add("forests");
		masterCategories.add("future");
		masterCategories.add("gaming");
		masterCategories.add("gardening");
		masterCategories.add("health");
		masterCategories.add("lifestyle");
		masterCategories.add("money");
		masterCategories.add("nature");
		masterCategories.add("oceans");
		masterCategories.add("photography");
		masterCategories.add("politics");
		masterCategories.add("productivity");
		masterCategories.add("science");
		masterCategories.add("social_media");
		masterCategories.add("space");
		masterCategories.add("sports");
		masterCategories.add("technology");
		masterCategories.add("travel");
		masterCategories.add("work");
	}

	@Override
	public RestResponse createPost(Post post) {
		Post response = null;
		try {
			Timestamp instant = Timestamp.from(Instant.now());
			post.setCreatedTime(instant);
			for (TOTOption option : post.getOptions()) {
				if (null == option.getId()) {
					option.setId(UUID.randomUUID().toString());
				}
			}
			boolean newPost = post.getPostId() == null;
			Set<String> categories = post.getCategories() == null? new HashSet<>() : post.getCategories();
			if(post.getTags() != null) {
				post.getTags().stream().forEach(tag -> {
					if (masterCategories.contains(tag.toLowerCase()))
						categories.add(tag);
				});
			}
			post.setCategories(categories);
			response = postDao.save(post);

			CompletableFuture.runAsync(() -> {
				if (newPost) {
					PostStats postStats = getEmptyPostStats(post);
					postStatsDao.save(postStats);
					userStatsDao.updatePostCount(post.getUserId(), 1l);
					post.getTags().stream().forEach(tag -> userDao.addTag(tag.substring(1)));
				}
			});

		} catch (Exception e) {
			log.error("Error while saving post...", e);
			return RestResponse.getFailedResponse(420, "Error while saving post..." + e.getMessage());
		}

		return RestResponse.getSuccessResponse(response.getPostId());
	}

	private PostStats getEmptyPostStats(Post post) {
		PostStats stats = new PostStats();
		stats.setPostId(post.getPostId());
		stats.setTotalVotes(0l);
		stats.setTotalVotesMale(0l);
		stats.setTotalVotesFemale(0l);
		for (TOTOption option : post.getOptions()) {
			OptionStats optionStats = new OptionStats();
			optionStats.setOptionId(option.getId());
			optionStats.setTitle(option.getTitle());
			optionStats.setTotalOptionCount(0l);
			optionStats.setOptionCountMale(0l);
			optionStats.setOptionCountFemale(0l);
			stats.getOptions().add(optionStats);
		}
		return stats;
	}

	@Override
	public RestResponse getPost(String postId, boolean isPublic) {
		Optional<Post> post = postDao.findById(postId);
		String sessionUserId = isPublic ? null : UserIdentityUtils.getUserIdFromSecurityContext();
		if (post.isPresent()) {
			Post postById = post.get();
			User user = userDao.getUserbyId(postById.getUserId());
			if (isPublic && user.getIsPrivate()) {
				return RestResponse.getFailedResponse(403, "Access denied for fetching private post using public api");
			}
			postById.setUserDetails(user);
			if(postById.getDraftPostUserId() != null) {
				postById.setDraftPostUserDetails(userDao.getUserbyId(postById.getDraftPostUserId()));
			}
			Optional<PostStats> postStats = postStatsDao.findById(postId);
			PostAndDetails postAndDetails = null;
			if (isPublic) {
				postAndDetails = new PostAndDetails(postById, null, null, postStats.get());
			} else {
				List<UserVote> votes = voteDao.findByPostId(postId, PageRequest.of(0, Integer.MAX_VALUE));
				votes.forEach(v -> v.setUserDetails(userDao.getBasicUserbyId(v.getUserId())));
				UserVote sessionUserVote = sessionUserId != null ? getUserVoteOnPost(sessionUserId, postId) : null;
				postAndDetails = new PostAndDetails(postById, votes, sessionUserVote, postStats.get());
			}
			return RestResponse.getSuccessResponse(postAndDetails);
		}
		return RestResponse.getFailedResponse(404, "Post does not exists...");
	}

	@Override
	public RestResponse getPostsByCategories(String[] categories, int skip, int limit) {
		List<Post> posts = postDao.findByCategoriesInAndIsPrivate(Arrays.asList(categories), false,
			PageRequest.of(skip, limit));
		return RestResponse.getSuccessResponse(posts);
	}

	@Override
	public RestResponse updatePost(Post post) {
		return null;
	}

	@Override
	public RestResponse votePost(String userId, String postId, Integer choice) {
		return null;
	}

	@Override
	public RestResponse sharePost(String postId, String userId) {
		return null;
	}

	@Override
	public RestResponse addComment(Comment comment) {
		Comment response = null;
		try {
			Timestamp instant = Timestamp.from(Instant.now());
			comment.setCreatedTime(instant);
			response = commentDao.save(comment);
			response.setUserDetails(userDao.getUserbyId(response.getUserId()));
			String postUserId = postDao.findById(comment.getPostId()).get().getUserId();
			User commentUser = userDao.getUserbyId(comment.getUserId());

			if (!UserIdentityUtils.getUserIdFromSecurityContext().equals(postUserId)) {
				CompletableFuture.runAsync(() -> {
					notificationDao.save(new Notification(postUserId, comment.getUserId(), comment.getPostId(),
							"comment", null, Timestamp.from(Instant.now())));
					pushService.sendPostPushNotificationToUser(postUserId, "You have a new comment!",
							commentUser.getFullName() + " has commented on your post. Check it out now. ",
							comment.getPostId());
				});
			}
		} catch (Exception e) {
			log.error("Error while saving comment...");
			return RestResponse.getFailedResponse(420, "Error while saving comment...");
		}

		return RestResponse.getSuccessResponse(response);
	}

	@Override
	public RestResponse deleteComment(String commentId) {
		commentDao.deleteById(commentId);
		return RestResponse.getSuccessResponse("Comment deleted successfully...");
	}

	@Override
	public RestResponse getComments(String postId, int skip, int limit) {
		Sort sort = Sort.by(Sort.Direction.DESC, "createdTime");
		List<Comment> comments = commentDao.findByPostId(postId, sort, PageRequest.of(skip, limit));
		comments.forEach(c -> c.setUserDetails(userDao.getUserbyId(c.getUserId())));
		return RestResponse.getSuccessResponse(comments);
	}

	@Override
	public RestResponse getFeed(String lastFeedTime, int skip, int limit) {
		String userId = UserIdentityUtils.getUserIdFromSecurityContext();
		List<String> followingUserIds = userDao.getFollowingIds(userId);
		followingUserIds.add(userId);
		Sort sort = Sort.by(Sort.Direction.DESC, "createdTime");

		List<Post> feed = postDao.findPostsByUserIdIn(followingUserIds, lastFeedTime, sort,
				PageRequest.of(skip, limit));

		List<PostAndDetails> response = feed.parallelStream().map(post -> {
			post.setUserDetails(userDao.getUserbyId(post.getUserId()));
			if(post.getDraftPostUserId() != null) 
				post.setDraftPostUserDetails(userDao.getUserbyId(post.getDraftPostUserId()));
			List<UserVote> votes = voteDao.findByPostId(post.getPostId(), PageRequest.of(0, Integer.MAX_VALUE));
			votes.forEach(v -> v.setUserDetails(userDao.getBasicUserbyId(v.getUserId())));
			UserVote sessionUserVote = getUserVoteOnPost(userId, post.getPostId());
			Optional<PostStats> postStats = postStatsDao.findById(post.getPostId());
			return new PostAndDetails(post, votes, sessionUserVote, postStats.get());
			
		}).collect(Collectors.toList());
		return RestResponse.getSuccessResponse(response);
	}

	@Override
	public RestResponse getPublicFeed(String lastFeedTime, int skip, int limit, String interest) {
		Sort sort = Sort.by(Sort.Direction.DESC, "createdTime");
		String userId = UserIdentityUtils.getUserIdFromSecurityContext();
		List<String> followingUserIds = userDao.getFollowingIds(userId);
		followingUserIds.add(userId);
		try {
			List<Block> blocked = blockDao.findByBlockerId(userId);
			blocked.forEach((b) -> followingUserIds.add(b.getBlockedId()));
			List<Block> blockedBy = blockDao.findByBlockerId(userId);
			blockedBy.forEach((b) -> followingUserIds.add(b.getBlockerId()));
		} catch (Exception e) {
			log.error("Error while getting block users", e);
		}

		List<Post> publicFeed = (interest == null || "".equals(interest) || "null".equals(interest)) 
				? postDao.findPublicFeed(lastFeedTime, followingUserIds, sort, PageRequest.of(skip, limit)) 
				: postDao.findPublicFeedForInterest(lastFeedTime, followingUserIds, interest, sort, PageRequest.of(skip, limit));
		
		List<PostAndDetails> response = publicFeed.parallelStream().map(post -> {
			post.setUserDetails(userDao.getUserbyId(post.getUserId()));
			if(post.getDraftPostUserId() != null) 
				post.setDraftPostUserDetails(userDao.getUserbyId(post.getDraftPostUserId()));
			List<UserVote> votes = voteDao.findByPostId(post.getPostId(), PageRequest.of(0, Integer.MAX_VALUE));
			votes.forEach(v -> v.setUserDetails(userDao.getBasicUserbyId(v.getUserId())));
			UserVote sessionUserVote = getUserVoteOnPost(userId, post.getPostId());
			Optional<PostStats> postStats = postStatsDao.findById(post.getPostId());
			return new PostAndDetails(post, votes, sessionUserVote, postStats.get());
		}).collect(Collectors.toList());

		return RestResponse.getSuccessResponse(response);
	}

	@Override
	public RestResponse getPostByUserId(String userId, int skip, int limit, boolean isPublic) {
		String sessionUserId = isPublic ? null : UserIdentityUtils.getUserIdFromSecurityContext();
		List<Post> posts = postDao.findByUserId(userId, PageRequest.of(skip, limit),
				Sort.by(Sort.Direction.DESC, "createdTime"));
		User postuserDetails = userDao.getUserbyId(userId);
		if (isPublic && postuserDetails.getIsPrivate()) {
			return RestResponse.getFailedResponse(403, "Access denied for fetching private profile using public api");
		}
		if (!posts.isEmpty()) {
			List<PostAndDetails> response = posts.parallelStream().map(post -> {
				post.setUserDetails(postuserDetails);
				if(post.getDraftPostUserId() != null) 
					post.setDraftPostUserDetails(userDao.getUserbyId(post.getDraftPostUserId()));
				List<UserVote> votes = voteDao.findByPostId(post.getPostId(), PageRequest.of(0, Integer.MAX_VALUE));
				votes.forEach(v -> v.setUserDetails(userDao.getBasicUserbyId(v.getUserId())));
				UserVote sessionUserVote = sessionUserId != null ? getUserVoteOnPost(sessionUserId, post.getPostId()) : null;
				Optional<PostStats> postStats = postStatsDao.findById(post.getPostId());
				return new PostAndDetails(post, votes, sessionUserVote, postStats.get());
			}).collect(Collectors.toList());
			return RestResponse.getSuccessResponse(response);
		}
		return RestResponse.getFailedResponse(420, "No Posts found for the user");
	}

	@Override
	public RestResponse deletePost(String postId) {
		Optional<Post> opt = postDao.findById(postId);
		if (opt.isPresent()) {
			Post post = opt.get();
			post.setIsDeleted(true);
			deletedPostDao.save(new DeletedPost().buildDeletedPost(post));
			postDao.delete(post);
			CompletableFuture.runAsync(() -> {
					userStatsDao.updatePostCount(post.getUserId(), -1l);
			});
		} else {
			log.error("Post not found with id: " + postId);
		}
		return RestResponse.getSuccessResponse("Post deleted successfully...");
	}

	@Override
	public RestResponse getTags(String tag) {
		List<Tag> tags = userDao.findTagsForAutocomplete(tag);
		return RestResponse.getSuccessResponse(tags);
	}

	@Override
	public RestResponse getPostsByTag(String tag, int skip, int limit) {
		List<Post> posts = postDao.findByTagsContaining("#" + tag, PageRequest.of(skip, limit),
				Sort.by(Sort.Direction.DESC, "createdTime"));
		posts.forEach(p -> p.setUserDetails(userDao.getUserbyId(p.getUserId())));
		
		List<PostAndDetails> response = posts.parallelStream().map(post -> {
			return new PostAndDetails(post, null, null, null);
		}).collect(Collectors.toList());
		return RestResponse.getSuccessResponse(response);
	}

	private UserVote getUserVoteOnPost(String userId, String postId) {
		List<UserVote> sessionUserVotes = voteDao.findByUserIdAndPostId(userId, postId, PageRequest.of(0, 1));
		if(sessionUserVotes.size() > 0) {
			return sessionUserVotes.get(0);
		}
		return null;
	}

	@Override
	public RestResponse getCollabPosts(String draftPostId, int skip, int limit) {
		String sessionUserId = UserIdentityUtils.getUserIdFromSecurityContext();
		List<Post> posts = postDao.findByDraftPostId(draftPostId, PageRequest.of(skip, limit),
				Sort.by(Sort.Direction.DESC, "numVotes"));
		posts.forEach(p -> p.setUserDetails(userDao.getUserbyId(p.getUserId())));
		
		List<PostAndDetails> response = posts.parallelStream().map(post -> {
			post.setUserDetails(userDao.getUserbyId(post.getUserId()));
			if(post.getDraftPostUserId() != null) 
				post.setDraftPostUserDetails(userDao.getUserbyId(post.getDraftPostUserId()));
			List<UserVote> votes = voteDao.findByPostId(post.getPostId(), PageRequest.of(0, Integer.MAX_VALUE));
			votes.forEach(v -> v.setUserDetails(userDao.getBasicUserbyId(v.getUserId())));
			UserVote sessionUserVote = getUserVoteOnPost(sessionUserId, post.getPostId());
			Optional<PostStats> postStats = postStatsDao.findById(post.getPostId());
			return new PostAndDetails(post, votes, sessionUserVote, postStats.get());
		}).collect(Collectors.toList());
		return RestResponse.getSuccessResponse(response);
	}

	public Post getPostImages(String postId) {
		Optional<Post> post = postDao.findById(postId);
		if (post.isPresent()) {
			return post.get();
		}
		return null;
	}
	
}
