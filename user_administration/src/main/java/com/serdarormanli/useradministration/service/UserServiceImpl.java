package com.serdarormanli.useradministration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serdarormanli.useradministration.model.User;
import com.serdarormanli.useradministration.repository.UserRepository;

/***
 * Simple service class for {@link User} database operations
 * @author Serdar ORMANLI
 *
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;

	@Override
	public List<User> getUsersList() {
		return userRepository.findAll();
	}

	@Override
	public User insertNewUser(User newUser) {
		return userRepository.save(newUser);
	}

	@Override
	public void updateUser(User updatedUser) {
		userRepository.save(updatedUser);
	}

	@Override
	public void deleteUser(String id) {
		userRepository.delete(id);
	}

	@Override
	public User getUser(String id) {
		return userRepository.findOne(id);
	}

}
