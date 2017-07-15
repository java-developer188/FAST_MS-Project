package com.fast.project.repository;

import org.springframework.data.repository.CrudRepository;

import com.fast.project.entity.Student;

public interface StudentRepository extends CrudRepository<Student, Long> {

}
