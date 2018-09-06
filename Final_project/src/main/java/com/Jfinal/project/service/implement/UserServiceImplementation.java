package com.Jfinal.project.service.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Jfinal.project.domain.User;
import com.Jfinal.project.domain.UserRepository;
import com.Jfinal.project.service.UserService;

@Service
public class UserServiceImplementation implements UserService{

	@Autowired 
	UserRepository userrepo;

	@Override
	public List<User> findAll() {
		// TODO Auto-generated method stub
		return userrepo.findAll();
	}

	@Override
	public User insertByUser(User user) {
		// TODO Auto-generated method stub
		return userrepo.save(user);
	}

	@Override
	public User update(User user) {
		// TODO Auto-generated method stub
		return userrepo.save(user);
	}

	@Override
	public User delete(Long id) {
		// TODO Auto-generated method stub
		User user = userrepo.findById(id).get();
		userrepo.delete(user);
		return user;
	}

	@Override
	public User findById(Long id) {
		// TODO Auto-generated method stub
		User user = userrepo.findById(id).get();
		return user;
	}
	
}
