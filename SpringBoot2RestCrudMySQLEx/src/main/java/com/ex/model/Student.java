package com.ex.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class Student {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	private String name;
	private Double fee;
	private String email;
	private String course;
	private String addr;

}
