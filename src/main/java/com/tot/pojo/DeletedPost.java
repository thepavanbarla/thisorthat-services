package com.tot.pojo;

import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

/**
 * @author karthik on 11/07/22.
 * @project totservices
 */

@Getter @Setter @JsonInclude(JsonInclude.Include.NON_NULL)
@Document("tot_deleted_post") public class DeletedPost extends Post {
    public DeletedPost buildDeletedPost(Post p) {
        super.postId = p.getPostId();
        super.userId = p.getUserId();
        super.context = p.getContext();
        super.options = p.getOptions();
        super.categories = p.getCategories();
        super.createdTime = p.getCreatedTime();
        super.isPrivate = p.getIsPrivate();
        super.isShare = p.getIsShare();
        super.numVotes = p.getNumVotes();
        super.numComments = p.getNumComments();
        super.isDeleted = p.getIsDeleted();
        super.userDetails = p.getUserDetails();
        super.gyan = p.getGyan();
        return this;
    }
}
