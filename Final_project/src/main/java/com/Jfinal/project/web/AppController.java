package com.Jfinal.project.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.Jfinal.project.domain.Task;
import com.Jfinal.project.domain.User;
import com.Jfinal.project.service.TaskService;
import com.Jfinal.project.service.UserService;

@Controller
@RequestMapping("/")
public class AppController {

	@Autowired
	UserService userservice;
	@Autowired
	TaskService taskservice;
	
	@Autowired
	JavaMailSender jms;
	
	 @RequestMapping("/register")
	    public String getUserList(ModelMap map) {
	        return "signup";
	    }
	  
	
	@RequestMapping("/uregister")
	public String register(HttpServletRequest request) {
		String name = (String) request.getParameter("username");
		String email = (String) request.getParameter("email");
		String pass = (String) request.getParameter("password");
		String confirm = (String) request.getParameter("confirm");
		boolean flag = false;
		
		ArrayList<User> all_users = (ArrayList<User>) userservice.findAll();
		for(int i=0; i<all_users.size(); i++) {
			if(all_users.get(i).getUsername().equals(name)) {
				System.out.println(all_users.get(i).getUsername().equals(name));
				flag = true;
			}
		}
		System.out.println(flag);
		System.out.println(!pass.equals(confirm));
		if(!pass.equals(confirm) || flag) {
			return "failpage_signup";
		}
		else {
			User add_new = new User();
			add_new.setUsername(name);
			add_new.setPassword(pass);
			
			SimpleMailMessage mainMessage = new SimpleMailMessage();
			String sendstring = "Hello, \r\n\n"
					+ "Thank you for registering at TickTock! \n"
					+ "Please write down the following login information: \n\n"
					+"Username: "+ add_new.getUsername() + "\n"
					+"Password: " + add_new.getPassword() + "\n\n"
					+"Thank you \n"
					+ "TickTock";
			
			mainMessage.setFrom("ticktocktick@sina.com");
			mainMessage.setTo(email);
			mainMessage.setSubject("TickTock");
			mainMessage.setText(sendstring);
			try {
				jms.send(mainMessage);
			} catch(Exception e) {
				return "wrongemail_signup";
			}
			userservice.insertByUser(add_new);

		}
		return "successpage_signup";
	}
	
	@RequestMapping("/login")
	public String log() {
		return "login";
	}
	
	@RequestMapping("/log")
	public String login( HttpServletRequest request, ModelMap map) {
		String name = (String) request.getParameter("username");
		String pass = (String) request.getParameter("password");
		User user = new User();
		ArrayList<User>all_users = (ArrayList<User>) userservice.findAll();
		boolean flag = false;
		for(int i = 0; i < all_users.size(); i++) {
			if(all_users.get(i).getUsername().equals(name)) {
				if(all_users.get(i).getPassword().equals(pass)){
					flag = true;
					user.setUsername(name);
					user.setPassword(pass);
					user.setIduser(all_users.get(i).getIduser());
				}
			}
		}
		if(!flag) {
			return "failed_login";
		}
		
		List<Task>tasks = taskservice.findAll();
		List<Task>mytasks = new ArrayList<Task>();
		for(int i = 0; i < tasks.size(); i++) {
			if(tasks.get(i).getUserid() == user.getIduser() && tasks.get(i).getUsername().equals(name)) {
				mytasks.add(tasks.get(i));
			}
		}
		map.addAttribute("userLogin", user);
		map.addAttribute("taskList",mytasks); 
		request.getSession().setAttribute("userLogin", user);
		request.getSession().setAttribute("taskList", mytasks);
		return "redirect:/homepage";
	}
	
	@RequestMapping("/homepage")
	public String tohome(@SessionAttribute User userLogin,HttpSession session, ModelMap map) {
		List<Task>tasks = taskservice.findAll();
		List<Task>mytasks = new ArrayList<Task>();
		for(int i = 0; i < tasks.size(); i++) {
			if(tasks.get(i).getUserid() == userLogin.getIduser() && tasks.get(i).getUsername().equals(userLogin.getUsername())) {
				if(tasks.get(i).getStatus().equals("Unfinished"))
					mytasks.add(tasks.get(i));
			}
		}
		map.addAttribute("userLogin", userLogin);
		map.addAttribute("taskList", mytasks);
		return "home";
	}
	
	@RequestMapping("/showhistory")
	public String showhistory(@SessionAttribute User userLogin,ModelMap map) {
		map.addAttribute("userLogin", userLogin);
		return "redirect:/history";
	}
	
	@RequestMapping("/history")
	public String history(@SessionAttribute User userLogin,HttpSession session, ModelMap map) {
		List<Task>tasks = taskservice.findAll();
		List<Task>mytasks = new ArrayList<Task>();
		for(int i = 0; i < tasks.size(); i++) {
			if(tasks.get(i).getUserid() == userLogin.getIduser() && tasks.get(i).getUsername().equals(userLogin.getUsername())) {
				if(!tasks.get(i).getStatus().equals("Unfinished"))
					mytasks.add(tasks.get(i));
			}
		}
		map.addAttribute("userLogin", userLogin);
		map.addAttribute("taskList", mytasks);
		return "history";
	}
	
	
	@RequestMapping("/add_task")
	public String add(@SessionAttribute User userLogin,ModelMap map) {
		map.addAttribute("userLogin", userLogin);
		return "addTask";
	}
	
	@RequestMapping("/addtask")
	public String addtask(@SessionAttribute User userLogin, HttpServletRequest request, ModelMap map) {
		map.addAttribute("userLogin", userLogin);
		String name = (String) request.getParameter("name");
		String type = (String) request.getParameter("type");
		String time1 = (String) request.getParameter("time1");
		String time2 = (String) request.getParameter("time2");
		String status = (String) request.getParameter("status");
		
		time1 = time1.replace('T', ' ');
		time2 = time2.replace('T', ' ');
		
		Task new_task = new Task();
		new_task.setTaskname(name);
		new_task.setTasktype(type);
		new_task.setTime_start(time1);
		new_task.setTime_end(time2);
		new_task.setUserid(userLogin.getIduser());
		new_task.setUsername(userLogin.getUsername());
		new_task.setStatus(status);
		
		taskservice.insertByTask(new_task);
		
		return "redirect:/homepage";
	}
	
    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {

        taskservice.delete(id);
        return "redirect:/homepage";
    }
    
    
    @RequestMapping("/update/{id}")
    public String getUser(@PathVariable Long id,ModelMap map,@SessionAttribute User userLogin) {
    	map.addAttribute("userLogin", userLogin);
    	Task selected = taskservice.findById(id);
    	String timea = selected.getTime_start();
    	String timeb = selected.getTime_end();
    	timea = timea.replace(' ', 'T');
		timeb = timeb.replace(' ', 'T');
		selected.setTime_start(timea);
		selected.setTime_end(timeb);
        map.addAttribute("task", selected);
        return "updateTask";
    }

    @RequestMapping("/update/up")
    public String putUser(ModelMap map, HttpServletRequest request) {
    	Task updatetask = new Task();
		String name = (String) request.getParameter("name");
		String type = (String) request.getParameter("type");
		String time1 = (String) request.getParameter("time1");
		String time2 = (String) request.getParameter("time2");
		String status = (String) request.getParameter("status");
		String idtask = (String)request.getParameter("idtask");
		String iduser = (String)request.getParameter("userid");
		String username = (String) request.getParameter("username");
		
		time1 = time1.replace('T', ' ');
		time2 = time2.replace('T', ' ');
		
		updatetask.setIdtask(Long.parseLong(idtask));
		updatetask.setStatus(status);
		updatetask.setTaskname(name);
		updatetask.setTasktype(type);
		updatetask.setTime_end(time2);
		updatetask.setTime_start(time1);
		updatetask.setUserid(Integer.parseInt(iduser));
		updatetask.setUsername(username);
		
        taskservice.update(updatetask);
        return "redirect:/homepage";
    }
    
	@RequestMapping("/togenerate")
	public String torandom(@SessionAttribute User userLogin,ModelMap map) {
		map.addAttribute("userLogin", userLogin);
		return "hobby";
	}
	
	@RequestMapping("/generate")
	public String randomlist(@SessionAttribute User userLogin,HttpSession session, ModelMap map,HttpServletRequest request) throws ParseException {
		String time = (String) request.getParameter("time_select");
		ArrayList<String> randompool = new ArrayList<String>();
		randompool.add(time + " 08:00");
		randompool.add(time + " 10:00");
		randompool.add(time + " 11:00");
		randompool.add(time + " 14:00");
		randompool.add(time + " 16:00");
		randompool.add(time + " 18:00");
		randompool.add(time + " 20:00");
		randompool.add(time + " 22:00");
		
		List<Task>tasks = taskservice.findAll();
		List<Task>mytasks = new ArrayList<Task>();
		List<Task>pretasks = new ArrayList<Task>();
		for(int i = 0; i < tasks.size(); i++) {
			if(tasks.get(i).getUserid() == userLogin.getIduser() && tasks.get(i).getUsername().equals(userLogin.getUsername())) {
				System.out.print("All: ");
				System.out.println(tasks.get(i));

				String temp = tasks.get(i).getTime_start();
				String [] arr = temp.split("\\s+");
				String temp2 = tasks.get(i).getTime_end();
				String [] arr2 = temp2.split("\\s+");
				
				if(tasks.get(i).getStatus().equals("Unfinished")) {
					if((arr[0].equals(time) || arr2[0].equals(time))) {
						mytasks.add(tasks.get(i));
						System.out.print("mytask: ");
						System.out.println(tasks.get(i));
					}
					if(temp.isEmpty() && temp2.isEmpty()) {
						Random rand=new Random();
						int j=rand.nextInt(100);
						System.out.println("+++++++++++++++++++++++++++++++");
						System.out.println(j);
						if(j >= 50) {
							mytasks.add(tasks.get(i));
						}
					}
				}
			}
			if(tasks.get(i).getTasktype().equals("default_activity")) {
				pretasks.add(tasks.get(i));
				//System.out.print("default: ");
				//System.out.println(tasks.get(i));
			}
		}
		
		if(mytasks.size() < 3) {
			
			int a = (int)(Math.random() * (pretasks.size()-1));
			int b = (int)(Math.random() * (pretasks.size()-1));
			int c = (int)(Math.random() * (pretasks.size()-1));
			while(a == b){
			b = (int)(Math.random() * (pretasks.size()-1));
			}
			while(a == c || b == c ){
			c = (int)(Math.random() * (pretasks.size()-1));
			}

			mytasks.add(pretasks.get(a));
			mytasks.add(pretasks.get(b));
			mytasks.add(pretasks.get(c));
		}
		else if(mytasks.size() >= 3) {
			Random rand=new Random();
			mytasks.add(pretasks.get(rand.nextInt(pretasks.size()-1)));
		}
		
		 List<Task> mytask_non = new ArrayList<Task>();
		 mytask_non.add(mytasks.get(0));
     	boolean flag = false;
	        for (int i = 1; i<mytasks.size(); i++) {
	        	for(int j = 0; j < i; j++) {
		            if(mytasks.get(i).getIdtask() == mytask_non.get(j).getIdtask()) {
		            	flag = true;
		            }
	        	}
	        	if(!flag) {
	        		mytask_non.add(mytasks.get(i));
	        	}
	        }
	        
		
        
		for(int i = 0; i < mytask_non.size(); i++) {
			if(mytask_non.get(i).getTime_start().isEmpty()) {
				if(mytask_non.get(i).getTime_end().isEmpty()) {
					Random rand=new Random();  
					mytask_non.get(i).setTime_start(randompool.get(rand.nextInt(7)));
				}
				else {
					mytask_non.get(i).setTime_start(mytask_non.get(i).getTime_end());
				}
			}
		}
		
		Task temp = new Task();
		for(int i =  mytask_non.size() - 1; i > 0; i--) {
			for(int j = 0; j < i; j++) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				
				if(format.parse(mytask_non.get(j+1).getTime_start()).before(format.parse(mytask_non.get(j).getTime_start()))){
					temp = mytask_non.get(j);
					mytask_non.set(j, mytasks.get(j+1));
					mytask_non.set(j+1, temp);
            	}
			}
		}
		
		
		System.out.println("----------------------------------------");
		for(int i = 0; i < mytask_non.size(); i++) {
			System.out.print(i + " ");
			System.out.println(mytask_non.get(i));
		}
		for(int i = 0; i < mytask_non.size(); i++) {
			
			String temp_time = mytask_non.get(i).getTime_start();

			mytask_non.get(i).setTime_start(temp_time.substring(10));
		}
		for(int i = 0; i < mytask_non.size(); i++) {
			if(mytask_non.get(i).getTasktype().equals("default_activity")) {
				mytask_non.get(i).setTasktype("Random :)");
			}
		}
		map.addAttribute("userLogin", userLogin);
		map.addAttribute("taskList", mytask_non);
		map.addAttribute("time", time);
		return "mylist";
	}
	
	
}
