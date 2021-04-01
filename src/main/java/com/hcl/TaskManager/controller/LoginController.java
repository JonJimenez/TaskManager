package com.hcl.TaskManager.controller;

import com.hcl.TaskManager.model.Task;
import com.hcl.TaskManager.model.User;
import com.hcl.TaskManager.service.TaskService;
import com.hcl.TaskManager.service.UserService;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

import javax.print.attribute.standard.Destination;
import javax.validation.Valid;

@Controller
public class LoginController {
	
    @Autowired
    private UserService userService;
    
    @Autowired
    private TaskService taskService;
    
    @GetMapping(value={"/", "/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping(value="/registration")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "There is already a user registered with the user name provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @GetMapping(value="/admin/home")
    public ModelAndView adminHome(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }
    @GetMapping(value="/user/home")
    public ModelAndView userHome() {
    	ModelAndView modelAndView = new ModelAndView();
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User user = userService.findUserByUserName(auth.getName());
    	modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
    	modelAndView.addObject("listOfTasks",taskService.findTaskByUser(user.getId()));
    	modelAndView.setViewName("user/home");
    	return modelAndView;
    }
    @GetMapping(value="/user/addTask")
    public ModelAndView addTask() {
    	ModelAndView modelAndView = new ModelAndView();
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User user = userService.findUserByUserName(auth.getName());
    	modelAndView.addObject("user",user);
    	modelAndView.setViewName("user/addTask");
    	return modelAndView;
    }
    @PostMapping(value="/user/addTask")
    public ModelAndView createTask(@RequestParam(name="taskName") String taskName,
    		@RequestParam(name="startDate") String startDate,@RequestParam(name="endDate") String endDate,
    		@RequestParam(name="severity") String severity,@RequestParam("description") String description,ModelMap model) {
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User user = userService.findUserByUserName(auth.getName());
    	Task savedTask= taskService.saveTask(taskName,startDate,endDate,severity,description,user.getEmail(),user.getId());
    	if(savedTask !=null) {
        	return new ModelAndView("redirect:/user/home",model);
    	}
    	else {
    		return new ModelAndView("redirect:/error",model);
    	}
    }
    @GetMapping(value="/user/updateTask")
    public ModelAndView dispalyUpdateTask(@RequestParam(name="taskid") Integer taskId,ModelMap model) {
    	Logger logger=Logger.getLogger(LoginController.class);
    	BasicConfigurator.configure();
    	logger.info("Get Updating Task");
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User user = userService.findUserByUserName(auth.getName());
    	Task task = taskService.findTaskByTaskId(taskId);
    	
    	if(task.getUserId().equals(user.getId())) {
    		logger.info("Task Belongs to user");
        	ModelAndView modelAndView= new ModelAndView();
        	modelAndView.setViewName("user/updateTask");
        	modelAndView.addObject("task",task);
        	return modelAndView;
    	}
    	logger.info("Task doesn't Belongs to user");
    	ModelAndView modelAndView= new ModelAndView("redirect:/user/home",model);
    	return modelAndView;
    }
    @PostMapping(value="/user/updateTask")
    public ModelAndView updateTask(@RequestParam(name="taskId") Integer taskid,@RequestParam(name="taskName") String taskName,
    		@RequestParam(name="startDate") String startDate,@RequestParam(name="endDate") String endDate,
    		@RequestParam(name="severity") String severity,@RequestParam("description") String description,
    		ModelMap model) {
    	Logger logger=Logger.getLogger(LoginController.class);
    	BasicConfigurator.configure();
    	ModelAndView modelAndView = new ModelAndView();
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	Task task = taskService.updateTask(taskid,taskName,startDate,endDate,severity,description);
    	logger.info("Post Updating Task");
    	if(task !=null) {
    		logger.info("Home View");
    		return new ModelAndView("redirect:/user/home", model);	
    	}
    	else {
    		logger.info("Error View");
    		return new ModelAndView("redirect:/error", model);
    	}
    }
    @GetMapping(value="/user/deleteTask")
    public ModelAndView displayDeleteTask(@RequestParam(name="taskid2") Integer taskId,ModelMap model) {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User user = userService.findUserByUserName(auth.getName());
    	Task task = taskService.findTaskByTaskId(taskId);
    	
    	if(task.getUserId().equals(user.getId())) {
    		ModelAndView modelAndView= new ModelAndView();
        	modelAndView.setViewName("user/deleteTask");
        	modelAndView.addObject("task",task);
        	return modelAndView;
    		
    	}
    	return new ModelAndView("redirect:/user/home",model);
    }
    @PostMapping(value="/user/deleteTask")
    public ModelAndView deleteTask(@RequestParam(name="taskId") Integer taskId,ModelMap model) {
    	ModelAndView modelAndView = new ModelAndView();
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	User user = userService.findUserByUserName(auth.getName());
    	taskService.deleteTask(taskId,user);
    	return new ModelAndView("redirect:/user/home", model);	
    }


}