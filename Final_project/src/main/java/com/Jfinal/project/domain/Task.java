package com.Jfinal.project.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Task implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idtask;
	@NotNull
	private String taskname;
	@NotNull
	private String tasktype;
	@NotNull
	private int userid;
	@NotNull
	private String username;
	
	private String time_start;
	private String time_end;
	@NotNull
	private String status;
	
	
	public long getIdtask() {
		return idtask;
	}
	public void setIdtask(long idtask) {
		this.idtask = idtask;
	}
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	public String getTasktype() {
		return tasktype;
	}
	public void setTasktype(String tasktype) {
		this.tasktype = tasktype;
	}
	public int getUserid() {
		return userid;
	}
	public void setUserid(int userid) {
		this.userid = userid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTime_start() {
		return time_start;
	}
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}
	public String getTime_end() {
		return time_end;
	}
	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Task [idtask=" + idtask + ", taskname=" + taskname + ", tasktype=" + tasktype + ", userid=" + userid
				+ ", username=" + username + ", time_start=" + time_start + ", time_end=" + time_end + ", status="
				+ status + "]";
	}
}
