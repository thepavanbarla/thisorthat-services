package com.tot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SpringSecutiryConfig extends WebSecurityConfigurerAdapter {

	private static final String[] AUTH_WHITELIST = {
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/public/**",
            "/user/stats/**",
            "/post/stats/**"
    };

	@Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues()).and().csrf().disable().authorizeRequests()
	        .anyRequest().authenticated()
	        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        .and().addFilterBefore(new JWTVerificationFilter(), BasicAuthenticationFilter.class)
	        .addFilterBefore(new ExceptionHandlerFilter(), JWTVerificationFilter.class);
    }

	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring().mvcMatchers(AUTH_WHITELIST);
	}

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
	    return authenticationManager();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
	    return source;
	}

	
	

}
