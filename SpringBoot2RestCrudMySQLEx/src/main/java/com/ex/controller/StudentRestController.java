package com.ex.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ex.model.Student;
import com.ex.service.IStudentService;
import com.ex.util.StudentUtil;

@RestController
@RequestMapping("/student")
public class StudentRestController {
	
	private Logger log= LoggerFactory.getLogger(StudentRestController.class);
	
	@Autowired
	private IStudentService service;
	@Autowired
	private StudentUtil util;
	
	/*
	 * 1. Read JSON(Student) and convert to Object Format
	 * Store Data in Database. Return One message
	 */
	@PostMapping("/save")
	public ResponseEntity<String> saveStudent(@RequestBody Student student) {
		
		log.info("Entered into method with student data to save");
		ResponseEntity<String> resp=null;
		try {
			log.info("About to call save Operation");
			
			Integer id=service.saveStudent(student);
			
			log.debug("Student saved with id "+id);
			
			String body="Student '"+id+"' Created";
			
			resp=new ResponseEntity<String>(body, HttpStatus.CREATED);//201
			
			log.info("Success response Constructed");
		}
		catch(Exception e) {
			log.error("Unable to save Student. : Problem is : "+e.getMessage());
			
			resp=new ResponseEntity<String>("Unable to Create Student", HttpStatus.INTERNAL_SERVER_ERROR);//500
			e.printStackTrace();
			
		}
		log.info("About to Exit method with Response");
		return resp;
	}
	
	/*
	 * 2. Fetch all row from database using service
	 *  Sort data using name, return as JSON,
	 *  else String Message no data found
	 * 
	 */
	@GetMapping("/all")
	public ResponseEntity<?> getAllStudents() {
		
		log.info("Entered into method to fetch the data ");
		
		ResponseEntity<?> resp=null;
		try {
		log.info("About to call fetch student service");
		
		List<Student> list=service.getAllStudents();
		if(list!=null && !list.isEmpty()) {
			
			log.info("Data is not empty "+list.size());
			
			list.sort((s1,s2)->s1.getName().compareTo(s2.getName()));
			resp=new ResponseEntity<List<Student>>(list, HttpStatus.OK);
		}
		else {
			log.info("No Student Exist: Size "+list.size());
			//resp=new ResponseEntity<>(HttpStatus.NO_CONTENT);
			
			resp=new ResponseEntity<String>("No Student Found ", HttpStatus.OK);
		}
		}
		catch(Exception e) {
			log.error("Unable to Fetch Student : Problem is "+e.getMessage());
			
			resp=new ResponseEntity<String>("Unable to Fetch Student", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		log.info("About to Exit fetch all method with Response");
		return resp;
	}
	
	/*
	 * 3. Get one student Object based on Id (PathVariable)
	 *    If Object Exist then return Student object
	 *    else provide message
	 */
	
	@GetMapping("/one/{id}")
	public ResponseEntity<?> getOne(@PathVariable Integer id){
		
		log.info("Entered into Get One Student method");
		
		ResponseEntity<?> resp=null;
		try {
			log.info("About to make service call to fetch one record");
			
		Optional<Student> opt =service.getOneStudent(id);
		if(opt.isPresent()) {
			log.info("Student exist =>"+id);
			
			resp=new ResponseEntity<Student>(opt.get(), HttpStatus.OK);
			//resp=ResponseEntity.ok(opt.get());
		}
		else {
			log.warn("Given Student id not exist "+id);
			
			resp=new ResponseEntity<String>("Student '"+id+"' Not Found", HttpStatus.BAD_REQUEST);
		}
		} catch (Exception e) {
			
			log.error("Unable to process Request fetch "+e.getMessage());
			
			resp=new ResponseEntity<String>("Unable Process Student fetch", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return resp;
	}
	
	/*
	 * 4. Delete one row based on ID (PathVariable)
	 *    First check given id exist on not?
	 *    if exist then call delete operation
	 *    else return no Record Message
	 */
	
	@DeleteMapping("/remove/{id}")
	public ResponseEntity<String> removeStudent(@PathVariable Integer id){
		ResponseEntity<String> resp=null;
		
		log.info("Entered into delete method");
		
		try {
			log.info("About to make service call for data check");
			
		boolean exist=service.isStudentExist(id);
		if(exist) {
			
			log.info("Student exist with given ID and deleted "+id);
			
			service.deleteStudent(id);
			resp=new ResponseEntity<String>("Student '"+id+"' deleted", HttpStatus.OK);
		} else {
			log.warn("Given Student id not exist "+id);
			
			resp=new ResponseEntity<String>("Student '"+id+"' Not Found", HttpStatus.BAD_REQUEST);
		}
		} catch(Exception e) {
			
			log.error("Unable to perform delete operation");
			
			resp=new ResponseEntity<String>("Unable to delete", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return resp;
	}
	
	/*
	 * 5. Update
	 */
	
	@PutMapping("/modify/{id}")
	public ResponseEntity<String> updateStudent(
			@PathVariable Integer id,
			@RequestBody Student student
			){
		log.info("Entered into Update method");
		ResponseEntity<String> resp=null;
		try {
			log.info("About to check given id exist or not in database");
		Optional<Student> opt = service.getOneStudent(id);
		if(opt.isPresent()) {
			
			log.info("Student present in database");
			
			Student actual=opt.get();
			
			util.mapToActualObject(actual, student);
			
			service.updateStudent(actual);
			
			resp = new ResponseEntity<String>("Student '"+id+"' Updated",
					//HttpStatus.RESET_CONTENT
					HttpStatus.OK
					);
			log.info("Student done update Successfully...");
		} else {
			log.info("Student not exist"+id);
			
			resp = new ResponseEntity<String>("Student '"+id+"' Not Found ",HttpStatus.BAD_REQUEST);
		}
		}
		catch(Exception e) {
			log.error("Unable to perform update operation "+e.getMessage());
			
			resp = new ResponseEntity<String>("Unable to Process Update", HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return resp;
	}

}
