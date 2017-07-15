package com.fast.project.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fast.project.CRUDPerformer;
import com.fast.project.CourseDataManager;
import com.fast.project.DaySheetParser;
import com.fast.project.FetchController;
import com.fast.project.ParseIntermediateTimeTable;
import com.fast.project.SectionDataManager;
import com.fast.project.TeacherDataManager;
import com.fast.project.entity.Course;
import com.fast.project.entity.CourseSectionTeacher;
import com.fast.project.entity.Student;
import com.fast.project.entity.Teacher;
import com.fast.project.entity.TimeTable;
import com.fast.project.repository.CourseRepository;
import com.fast.project.repository.CourseSectionTeacherRepository;
import com.fast.project.repository.TimeTableRepository;
import com.fast.project.service.CourseService;
import com.fast.project.service.StudentService;
import com.fast.project.service.TeacherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class TimetableController {

	@Autowired
	CourseRepository courseRepository;

	@Autowired
	TimeTableRepository timeTableRepository;

	@Autowired
	CourseSectionTeacherRepository cstRepository;

	@Autowired
	TeacherService teacherService;

	@Autowired
	StudentService studentService;
	
	@Autowired
	CourseService courseService;

	boolean onlyOnce = true;

	/**
	 * Controller to call Category page service.
	 * 
	 * @param request
	 * @param httpreq
	 * @return
	 */
	@RequestMapping(path = "/test", method = RequestMethod.GET)
	public String test() {
		long entry = System.currentTimeMillis();
		HashMap<String, Object> response = new HashMap<>();
		ObjectMapper m = new ObjectMapper();
		try {
			Course course = courseRepository.findOne((long) 7);
			TimeTable tt = timeTableRepository.findOne((long) 5);
			CourseSectionTeacher cst = cstRepository.findOne((long) 6);
			response.put("Course", m.writeValueAsString(course));
			response.put("test", "testing value is good");
		} catch (Exception e) {

			response.put("resultcode", "INVALID_ARGUMENT");
			response.put("resultdescription", e.getMessage());

		} finally {
			long exit = System.currentTimeMillis();
		}
		try {
			return m.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			return "JSON Processing error";
		}
	}

	/**
	 * Controller to call Category page service.
	 * 
	 * @param request
	 * @param httpreq
	 * @return
	 */
	@RequestMapping(path = "/init", method = RequestMethod.GET)
	public String initialize(@RequestParam("value") boolean init) {
		long entry = System.currentTimeMillis();
		HashMap<String, Object> response = new HashMap<>();
		ObjectMapper m = new ObjectMapper();
		try {
			if (onlyOnce && init) {
				onlyOnce = false;
				FetchController.perform();
				DaySheetParser.perform();
				CourseDataManager courseDataManager = new CourseDataManager();
				CRUDPerformer.insertCourses(courseDataManager.getCourseBeans());
				SectionDataManager sectionDataManager = new SectionDataManager();
				sectionDataManager.extractSections();
				CRUDPerformer.insertSections(sectionDataManager.getSectionBeans());
				TeacherDataManager teacherDataManager = new TeacherDataManager();
				teacherDataManager.extractTeacher();
				CRUDPerformer.insertTeachers(teacherDataManager.getTeacherBeans());
				ParseIntermediateTimeTable parseIntermediateTimeTable = new ParseIntermediateTimeTable();
				parseIntermediateTimeTable.extractTimeTable();
				CRUDPerformer.insertTimeTable(parseIntermediateTimeTable.getTimetableBeans());
				response.put("Result", "System Initialized Successfully");
			} else {
				response.put("Result", "Init Paramter found false");
			}
		} catch (Exception e) {

			response.put("resultcode", "INVALID_ARGUMENT");
			response.put("resultdescription", e.getMessage());

		} finally {
			long exit = System.currentTimeMillis();
		}
		try {
			return m.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			return "JSON Processing error";
		}
	}

	/**
	 * Controller to call Category page service.
	 * 
	 * @param request
	 * @param httpreq
	 * @return
	 */
	@RequestMapping(path = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	public String login(@RequestBody HashMap<String, String> map) {
		long entry = System.currentTimeMillis();
		HashMap<String, Object> response = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			String username = "", password = "";
			if (map.containsKey("username") && map.containsKey("password")) {
				username = map.get("username");
				password = map.get("password");

				Student student = studentService.login(username, password);
				response.put("Student", mapper.writeValueAsString(student));
				response.put("result", "SUCCESS");
			} else {
				// List<Teacher> result = teacherService.getTeachers();
				// response.put("Teachers", mapper.writeValueAsString(result));
				// response.put("TeacherCount", result.size());
				// response.put("result", "SUCCESS");
			}
		} catch (Exception e) {
			response.put("result", "ERROR");
			response.put("errorDescription", e.getMessage());

		} finally {
			long exit = System.currentTimeMillis();
		}
		try {
			return mapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			return "{\"result\":\"ERROR\"," + "\"errorDescription\":\"JSON Processing Error\"}";
		} catch (NumberFormatException nfe) {
			return "{\"result\":\"ERROR\"," + "\"errorDescription\":\"Invalid ID\"}";
		}
	}

	/**
	 * Controller to call Category page service.
	 * 
	 * @param request
	 * @param httpreq
	 * @return
	 */
	@RequestMapping(path = "/faculty", method = { RequestMethod.GET, RequestMethod.POST })
	public String faculty(@RequestBody HashMap<String, String> map) {
		long entry = System.currentTimeMillis();
		HashMap<String, Object> response = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (map.containsKey("id")) {
				Long id = Long.valueOf(map.get("id"));
				List<HashMap<String, String>> result = teacherService.getTeacherTimeTable(id);
				response.put("TimeTable", mapper.writeValueAsString(result));
				response.put("TimeTableCount", result.size());
				response.put("result", "SUCCESS");
			} else {
				List<Teacher> result = teacherService.getTeachers();
				response.put("Teachers", mapper.writeValueAsString(result));
				response.put("TeacherCount", result.size());
				response.put("result", "SUCCESS");
			}
		} catch (Exception e) {
			response.put("result", "ERROR");
			response.put("errorDescription", e.getMessage());

		} finally {
			long exit = System.currentTimeMillis();
		}
		try {
			return mapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			return "{\"result\":\"ERROR\"," + "\"errorDescription\":\"JSON Processing Error\"}";
		} catch (NumberFormatException nfe) {
			return "{\"result\":\"ERROR\"," + "\"errorDescription\":\"Invalid ID\"}";
		}
	}

	/**
	 * Controller to call Category page service.
	 * 
	 * @param request
	 * @param httpreq
	 * @return
	 */
	@RequestMapping(path = "/courses", method = { RequestMethod.GET, RequestMethod.POST })
	public String courses(@RequestBody HashMap<String, String> map) {
		long entry = System.currentTimeMillis();
		HashMap<String, Object> response = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (map.containsKey("id")) {
				Long id = Long.valueOf(map.get("id"));
//				List<HashMap<String, String>> result = teacherService.getTeacherTimeTable(id);
//				response.put("TimeTable", mapper.writeValueAsString(result));
//				response.put("TimeTableCount", result.size());
				response.put("result", "SUCCESS");
			} else {
				List<Course> result = courseService.getCourses();
				response.put("Courses", mapper.writeValueAsString(result));
				response.put("CourseCount", result.size());
				response.put("result", "SUCCESS");
			}
		} catch (Exception e) {
			response.put("result", "ERROR");
			response.put("errorDescription", e.getMessage());

		} finally {
			long exit = System.currentTimeMillis();
		}
		try {
			return mapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			return "{\"result\":\"ERROR\"," + "\"errorDescription\":\"JSON Processing Error\"}";
		} catch (NumberFormatException nfe) {
			return "{\"result\":\"ERROR\"," + "\"errorDescription\":\"Invalid ID\"}";
		}
	}
	
	
	/**
	 * Controller to call Category page service.
	 * 
	 * @param request
	 * @param httpreq
	 * @return
	 */
	@RequestMapping(path = "/student", method = { RequestMethod.GET, RequestMethod.POST })
	public String student(@RequestBody HashMap<String, String> map) {
		long entry = System.currentTimeMillis();
		HashMap<String, Object> response = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			if (map.containsKey("id")) {
				Long id = Long.valueOf(map.get("id"));
				List<HashMap<String, String>> result = studentService.getStudentTimeTable(id);
				response.put("TimeTable", mapper.writeValueAsString(result));
				response.put("TimeTableCount", result.size());
				response.put("result", "SUCCESS");
			} else {
				// List<Teacher> result = teacherService.getTeachers();
				// response.put("Teachers", mapper.writeValueAsString(result));
				// response.put("TeacherCount", result.size());
				// response.put("result", "SUCCESS");
			}
		} catch (Exception e) {
			response.put("result", "ERROR");
			response.put("errorDescription", e.getMessage());

		} finally {
			long exit = System.currentTimeMillis();
		}
		try {
			return mapper.writeValueAsString(response);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			return "{\"result\":\"ERROR\"," + "\"errorDescription\":\"JSON Processing Error\"}";
		} catch (NumberFormatException nfe) {
			return "{\"result\":\"ERROR\"," + "\"errorDescription\":\"Invalid ID\"}";
		}
	}

}
