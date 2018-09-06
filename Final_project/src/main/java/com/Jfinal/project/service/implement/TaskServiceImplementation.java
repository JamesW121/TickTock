package com.Jfinal.project.service.implement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Jfinal.project.domain.Task;
import com.Jfinal.project.domain.TaskRepository;
import com.Jfinal.project.service.TaskService;

@Service
public class TaskServiceImplementation implements TaskService{

	@Autowired
	TaskRepository taskrepo;
	
	@Override
	public List<Task> findAll() {
		// TODO Auto-generated method stub
		return taskrepo.findAll();
	}

	@Override
	public Task insertByTask(Task task) {
		// TODO Auto-generated method stub
		return taskrepo.save(task);
	}

	@Override
	public Task update(Task task) {
		// TODO Auto-generated method stub
		return taskrepo.save(task);
	}

	@Override
	public Task delete(Long id) {
		// TODO Auto-generated method stub
		Task t = taskrepo.findById(id).get();
		taskrepo.delete(t);
		return t;
	}

	@Override
	public Task findById(Long id) {
		// TODO Auto-generated method stub
		return taskrepo.findById(id).get();
	}

	@Override
	public List<Task> findByUsernameAndUserid(String username, int userid) {
		// TODO Auto-generated method stub
		return taskrepo.findByUsernameAndUserid(username, userid);
	}

}
