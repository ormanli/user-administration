package com.serdarormanli.useradministration.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;

@Configuration
@EnableMongoRepositories("com.serdarormanli.useradministration.repository")
public class MongoDBConfiguration extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return "test-db";
	}

	@Override
	public Mongo mongo() throws Exception {
		return new Mongo();
	}
}
