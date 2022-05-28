package qldt.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import qldt.AppUser;
import qldt.ClassHP;
import qldt.Student;
import qldt.TimeTable;
import qldt.data.StudentRepo;
import qldt.data.TeacherRepo;
import qldt.data.TimeTableRepo;
import qldt.service.AppRoleSer;
import qldt.service.AppUserSer;
import qldt.service.ClassSer;
import qldt.service.StudentSer;
import qldt.service.SubjectSer;
import qldt.service.TeacherSer;
import qldt.service.TimeTableSer;
import qldt.service.UserRoleSer;

@Controller
public class TeacherCon {
	@Autowired
	private TeacherSer teacherSer;
	@Autowired
	private StudentSer studentSer;
	@Autowired
	private AppUserSer appUserSer;
	@Autowired
	private AppRoleSer appRoleSer;
	@Autowired
	private UserRoleSer userRoleSer;
	@Autowired
	private StudentRepo studentRepo;
	@Autowired
	private TeacherRepo teacherRepo;
	@Autowired
	private TimeTableRepo timetableRepo;
	@Autowired
	private SubjectSer subjectSer;
	@Autowired
	private ClassSer classSer;
    @Autowired
    private TimeTableSer timetableSer;
	
	@GetMapping("/showTeacherClasses/showStudents/{ID}")
	public String showTeachershowStudents(@PathVariable("ID") long ID, Model m) {
		List<TimeTable> timetable1 = new ArrayList<>();
		List<TimeTable> list_timetable = timetableSer.getTimeTable();
		for(TimeTable timetable : list_timetable){
			if(timetable.getClassHP().getID() == ID) {
				timetable1.add(timetable);
			}
		}
		m.addAttribute("timetable1", timetable1);
		return "showTeachershowStudents";
	}
	
	@GetMapping("/showTeacherClasses/showStudents/editMarks/{ID}")
	public String editMarks(@PathVariable("ID") long ID, Model m) {
		System.out.println("1");
		TimeTable timetable1 = timetableSer.getTimeTableByID(ID);
		ClassHP classHP =timetable1.getClassHP();
		String id_class =(String) classHP.getID().toString();
	
		m.addAttribute("id_class",  id_class);
		m.addAttribute("timetable1", timetable1);
		return "editMarks";
	}
    
	@GetMapping("/showTeacherClasses")
	public String TeacherClassShow(Model model, Principal principal) {
		List<ClassHP> timetable_teacher = new ArrayList<>();
		String username = principal.getName();
		
		List<ClassHP> list_timetable = classSer.getClassHP();
		for(ClassHP classes : list_timetable) {
			if(classes.getTeacher().getAppUser().getUserName().equalsIgnoreCase(username)){
				timetable_teacher.add(classes);
			}
		}
		model.addAttribute("timetable_teacher",timetable_teacher);
		return "showTimetableTeacher";
	}
	
	@PostMapping("/showTeacherClasses/showStudents/editMarks/UpdateMarks")
	public String UpdateMarks(@ModelAttribute TimeTable timetable, Model model, HttpSession session) {
//		TimeTable tt1 = timetableSer.getTimeTableByID(timetable.getID())
		
		ClassHP classhp = classSer.getClassHPByID(timetable.getClassHP().getID());
		Student student = studentSer.getStdByID(timetable.getStudent().getID());
		
		timetable.setClassHP(classhp);
		timetable.setStudent(student);
//		timetable.setID(timetable.getID());
		
		System.out.println(timetable);
//		timetableRepo.deleteById(timetable.getID());
		timetableSer.addTimeTable(timetable);
		session.setAttribute("msg", "Student Edited Sucessfully...");
		return "redirect:/showTeacherClasses";
	}
}
