package com.serdarormanli.useradministration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.serdarormanli.useradministration.model.User;

public interface UserRepository extends MongoRepository<User, String> {
}
