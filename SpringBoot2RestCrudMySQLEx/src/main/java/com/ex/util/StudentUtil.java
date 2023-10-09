package com.ex.util;

import org.springframework.stereotype.Component;

import com.ex.model.Student;

@Component
public class StudentUtil {

	public void mapToActualObject(Student actual, Student student) {
		if(student.getName()!=null)
		actual.setName(student.getName());
		if(student.getFee()!=null)
		actual.setFee(student.getFee());
		if(student.getCourse()!=null)
		actual.setCourse(student.getCourse());
		if(student.getEmail()!=null)
		actual.setEmail(student.getEmail());
		if(student.getAddr()!=null)
		actual.setAddr(student.getAddr());
	}
	

}
