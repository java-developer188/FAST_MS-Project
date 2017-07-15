package com.fast.project.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fast.project.entity.Teacher;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Long> {

}
