package com.ex.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ex.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

}
