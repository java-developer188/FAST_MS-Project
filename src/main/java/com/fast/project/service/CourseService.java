package com.fast.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fast.project.entity.Course;
import com.fast.project.entity.Teacher;
import com.fast.project.repository.CourseRepository;
import com.fast.project.repository.CourseSectionTeacherRepository;
import com.fast.project.repository.TeacherRepository;
import com.fast.project.repository.TimeTableRepository;

@Service
public class CourseService {

	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	TimeTableRepository timeTableRepository;
	
	@Autowired
	CourseSectionTeacherRepository courseSectionTeacherRepository;
	
	public List<Course> getCourses(){
		List<Course> list = new ArrayList<>();
		for(Course course : courseRepository.findAll())
		{
			list.add(course);
		}
		return list;
	}
	
	
}
