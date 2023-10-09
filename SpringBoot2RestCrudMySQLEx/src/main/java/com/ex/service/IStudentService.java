package com.ex.service;

import java.util.List;
import java.util.Optional;

import com.ex.model.Student;

public interface IStudentService {
	//CRUD Operations
	
	Integer saveStudent(Student s);
	
	void updateStudent(Student s);
	
	void deleteStudent(Integer id);
	
	Optional<Student> getOneStudent(Integer id);
	
	List<Student> getAllStudents();
	
	boolean isStudentExist(Integer id);
	

}
