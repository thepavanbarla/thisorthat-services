package com.tot.pojo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class PostAndDetails {
	Post post;
	List<UserVote> votes;
	UserVote sessionUserVote;
	PostStats postStats;
}
