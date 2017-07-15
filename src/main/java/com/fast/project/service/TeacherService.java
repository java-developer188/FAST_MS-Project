package com.fast.project.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fast.project.entity.Teacher;
import com.fast.project.repository.CourseSectionTeacherRepository;
import com.fast.project.repository.TeacherRepository;
import com.fast.project.repository.TimeTableRepository;

@Service
public class TeacherService {

	@Autowired
	TeacherRepository teacherRepository;
	
	@Autowired
	TimeTableRepository timeTableRepository;
	
	@Autowired
	CourseSectionTeacherRepository courseSectionTeacherRepository;
	
	public List<Teacher> getTeachers(){
		List<Teacher> list = new ArrayList<>();
		for(Teacher teacher : teacherRepository.findAll())
		{
			list.add(teacher);
		}
		return list;
	}
	
	public List<HashMap<String, String>> getTeacherTimeTable(Long id){
		List<HashMap<String, String>> list = new ArrayList<>();
		for(Object[] obj :timeTableRepository.getTimetableByTeacherId(id)){
			HashMap<String, String> map = new HashMap<>();
			map.put("day", obj[0].toString());
			map.put("time", obj[1].toString());
			map.put("room", obj[2].toString());
			map.put("course", obj[3].toString());
			map.put("section", obj[4].toString());
			list.add(map);
		}
		return list ;
	}
	
}
