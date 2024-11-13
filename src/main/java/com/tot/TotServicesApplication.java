package com.tot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author karthik on 04/12/21.
 * @project totservices
 */

@SpringBootApplication 
public class TotServicesApplication extends SpringBootServletInitializer{
    
	public static void main(String[] args) {
        SpringApplication.run(TotServicesApplication.class, args);
    }
    
    @Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TotServicesApplication.class);
	}

}
