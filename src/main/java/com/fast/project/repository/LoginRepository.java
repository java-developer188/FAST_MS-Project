package com.fast.project.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.fast.project.entity.Login;
import com.fast.project.entity.Student;

public interface LoginRepository extends CrudRepository<Login, Long> {

	@Query("select l.student from Login l where l.username = :username")
	public Student findByUsername(@Param(value = "username")String username);
}
