package com.tot.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserIdentityUtils {

	public static String getUserIdFromSecurityContext() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	}
	
}
