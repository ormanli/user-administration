package com.serdarormanli.useradministration.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.serdarormanli.useradministration.model.Captcha;

public interface CaptchaRepository extends MongoRepository<Captcha, String> {

}
