package com.tot.config;

import static com.tot.utils.SecurityConstants.TOKEN_HEADER_NAME;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.tot.exception.UnauthorizedAccessException;
import com.tot.utils.CommonUtils;

import lombok.SneakyThrows;

public class JWTVerificationFilter extends OncePerRequestFilter {

    @SuppressWarnings("deprecation")
	public JWTVerificationFilter() throws IOException {
    	String firebaseKeyFile = "this-or-that-50b96-firebase-adminsdk-fkeu4-495da1310c.json";
    	
    	String env = CommonUtils.getActiveProfile();
    	if("staging".equals(env))
    		firebaseKeyFile = "this-or-that-staging-firebase-adminsdk-soiug-cdb8d8080c.json";
    	else if("local".equals(env) || "dev".equals(env))
    		firebaseKeyFile = "this-or-that-dev-42eb0-firebase-adminsdk-y7tzt-8825bf23f2.json";
    	
    	System.out.println("KF# " + firebaseKeyFile);
    	
    	Resource resource = new ClassPathResource(firebaseKeyFile);
    	InputStream resourceInputStream = resource.getInputStream();
    	FirebaseOptions options =
    	        new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(resourceInputStream))
    	            .build();
        FirebaseApp.initializeApp(options);
    }
    
    @SneakyThrows @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader(TOKEN_HEADER_NAME);

        if (token == null || token.trim().isEmpty()) {
            throw new UnauthorizedAccessException("Invalid or expired access token. ");
        }
        
        String userId = null;

		try {
	        FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
	        userId = decodedToken.getUid();
		}
		catch(Exception ex) {
            throw new UnauthorizedAccessException("Invalid or expired access token. ");
		}

        if (userId != null && !userId.isEmpty())
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>()));
        else {
            throw new UnauthorizedAccessException("Invalid or expired access token. ");
        }

        filterChain.doFilter(request, response);
    }

}
