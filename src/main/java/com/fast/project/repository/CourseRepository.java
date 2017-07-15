package com.fast.project.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fast.project.entity.Course;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {

}
