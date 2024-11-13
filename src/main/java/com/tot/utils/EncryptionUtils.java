package com.tot.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class EncryptionUtils {
	
	public static final String jwtIssuer = "tot-service";
	private static final String JWT_SIGNING_KEY = "key";
	
	public static String verifyJWT(String jwt) {
		Algorithm algorithm = Algorithm.HMAC512(JWT_SIGNING_KEY);
		JWTVerifier verifier = JWT.require(algorithm)
	        .withIssuer(jwtIssuer)
	        .build(); 
		DecodedJWT decodedJwt = verifier.verify(jwt);

		Date expiry = decodedJwt.getExpiresAt();
		Date now = new Date();
		if(decodedJwt.getSubject() != null && !decodedJwt.getSubject().isEmpty() && now.before(expiry)) {
			return decodedJwt.getSubject();
		}
		return null;
	}
	
}
