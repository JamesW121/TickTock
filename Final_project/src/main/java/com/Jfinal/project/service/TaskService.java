package com.Jfinal.project.service;

import java.util.List;

import com.Jfinal.project.domain.Task;

public interface TaskService {
	  List<Task> findAll();

	  Task insertByTask(Task task);

	  Task update(Task task);

	  Task delete(Long id);
	   
	  Task findById(Long id);
	  
	  List<Task> findByUsernameAndUserid(String username, int userid);
}
