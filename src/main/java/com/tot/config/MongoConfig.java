package com.tot.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.tot.utils.DateToTimestampConverter;

/**
 * @author karthik on 04/12/21.
 * @project totservices
 */
@Configuration public class MongoConfig extends AbstractMongoClientConfiguration {
	
	@Value("${spring.data.mongodb.uri}") String mongoConnectionUri;

    private List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();

    @Override public MongoCustomConversions customConversions() {
        converters.add(new DateToTimestampConverter());
        return new MongoCustomConversions(converters);
    }

    @Override protected String getDatabaseName() {
        return "tot";
    }
    
    @Override
    public MongoClient mongoClient() {
        final ConnectionString connectionString = new ConnectionString(mongoConnectionUri);
        final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();
        return MongoClients.create(mongoClientSettings);
    }
    

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }}
