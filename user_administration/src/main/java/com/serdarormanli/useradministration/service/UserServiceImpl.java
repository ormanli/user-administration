package com.serdarormanli.useradministration.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serdarormanli.useradministration.model.User;
import com.serdarormanli.useradministration.repository.UserRepository;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService {
	@Autowired
	UserRepository userRepository;

	@Override
	public List<User> getUsersList() {
		return userRepository.findAll();
	}

	@Override
	// TODO validation koy
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
