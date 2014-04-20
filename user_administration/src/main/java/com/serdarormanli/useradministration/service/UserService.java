package com.serdarormanli.useradministration.service;

import java.util.List;

import com.serdarormanli.useradministration.model.User;

public interface UserService {

	List<User> getUsersList();

	User insertNewUser(User newUser);

	void updateUser(User updatedUser);

	void deleteUser(String id);

	User getUser(String id);
}
