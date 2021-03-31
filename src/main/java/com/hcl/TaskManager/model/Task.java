package com.hcl.TaskManager.model;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
 
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "task_id")
    private Integer id;
    
    @Column(name = "description")
    @NotEmpty(message = "*Please provide a description")
    private String description;
    
    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;
    
    @Column(name = "task_name")
    @NotEmpty(message = "*Please provide the name of the Task")
    private String taskName;
    
    @Column(name = "end_date")
    @NotEmpty(message = "*Please provide an end Date")
    private String endDate;
    
    @Column(name = "start_date")
    @NotEmpty(message = "*Please provide a start Date")
    private String startDate;
    
    @Column(name = "severity")
    @NotEmpty(message = "*Please provide the level of Severity")
    private String severity;
    
    @Column(name = "user_id")
    private Integer userId;
    
    public Task() {
    	
    }
    public Task(String taskName,String startDate,String endDate,String severity,String description,String email,Integer userId) {
    	this.taskName=taskName;
    	this.startDate=startDate;
    	this.endDate=endDate;
    	this.severity=severity;
    	this.description=description;
    	this.userId=userId;
    	this.email=email;
    	
    }
    public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}


}