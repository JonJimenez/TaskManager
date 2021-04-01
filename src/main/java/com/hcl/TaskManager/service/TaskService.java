package com.hcl.TaskManager.service;

import com.hcl.TaskManager.model.Task;
import com.hcl.TaskManager.model.User;
import com.hcl.TaskManager.repository.TaskRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger; 

@Service
public class TaskService {
	Logger logger=Logger.getLogger(TaskService.class);
    private TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll(){
    	return taskRepository.findAll();
    }
    public Task findTaskByTaskId(Integer id){
    	return taskRepository.findById(id);
    }
    public List<Task> findTaskByUser(Integer userId){
    	List<Task> allTasks = findAll();
    	List<Task> tasksByUser = new LinkedList<Task>();
    	for(Task task: allTasks) {
    		if(task.getUserId().equals(userId)) {
    			tasksByUser.add(task);
    		}
    	}
    	return tasksByUser;
    }
    public Task saveTask(String taskName,String startDate,String endDate,String severity,String description,String email,Integer userId) {
    	Task task = new Task(taskName,startDate,endDate,severity,description,email,userId);
    	
    	logger.info("Task INFO");
    	logger.info("Task name:"+task.getTaskName());
    	logger.info("Task start date:"+task.getStartDate());
    	logger.info("Task end date:"+task.getEndDate());
    	logger.info("Task severity:"+task.getSeverity());
    	logger.info("Task userid:"+task.getUserId());
    	logger.info("Task taskid:"+task.getId());
    	logger.info("Task description:"+task.getDescription());
    	return taskRepository.save(task);
    }
    public Task updateTask(Integer taskid,String taskName,String startDate,String endDate,String severity,String description) {
    	Task task = taskRepository.findById(taskid);
    	task.setDescription(description);
    	task.setEndDate(endDate);
    	task.setSeverity(severity);
    	task.setStartDate(startDate);
    	task.setTaskName(taskName);
    	return taskRepository.save(task);
    }
    public void deleteTask(Integer taskId,User user) {
    	Task task = taskRepository.findById(taskId);
    	if(user.getId().equals(task.getUserId())) {
    		taskRepository.delete(task);
    	}
    }
    

}