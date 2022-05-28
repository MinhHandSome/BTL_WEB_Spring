package qldt.controller

;

import qldt.AppUser;
import qldt.Student;
import qldt.Teacher;
import qldt.Subject;
import qldt.UserRole;
import qldt.ClassHP;
import qldt.TimeTable;
import qldt.data.StudentRepo;
import qldt.data.SubjectRepo;
import qldt.data.TeacherRepo;
import qldt.service.AppRoleSer;
import qldt.service.AppUserSer;
import qldt.service.ClassSer;
import qldt.service.StudentSer;
import qldt.service.SubjectSer;
import qldt.service.TeacherSer;
import qldt.service.TimeTableSer;
import qldt.service.UserRoleSer;
import java.security.Principal;
import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class StudentCon {
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
	private SubjectSer subjectSer;
	@Autowired
	private ClassSer classSer;
	@Autowired
	private TimeTableSer timetableSer;

	@PostMapping("/addTimeTable")
	public String TimeTable(@ModelAttribute TimeTable timeTable, Model model, Principal principal,
			HttpSession session) {

		String userName = principal.getName();
		Student student = studentSer.getStbyUserName(userName);
		Boolean check_exist = true;
		Boolean check_common_time = true;
		List<TimeTable> list_timetable = timetableSer.getTimeTable();
		for (TimeTable timetable_each_student : list_timetable) {
			if ((timetable_each_student.getStudent().getAppUser().getUserName().equalsIgnoreCase(userName))
					&& ((timeTable.getClassHP().getID() == timetable_each_student.getClassHP().getID())
							|| (timetable_each_student.getClassHP().getTime()
									.equalsIgnoreCase(timeTable.getClassHP().getTime())))) {

				check_exist = false;
				break;
			}
//			if((timetable_each_student.getStudent().getAppUser().getUserName().equalsIgnoreCase(userName)) && (timetable_each_student.getClassHP().getTime().equalsIgnoreCase(timeTable.getClassHP().getTime()))) {
//				check_common_time = false;
//				break;
//			}
		}
		if (check_exist == false) {
			session.setAttribute("msg", "Subject existed");
		}
//		if(check_common_time == false) {
//			session.setAttribute("msg","overlapping school time" );
//		}
		else {
			System.out.println(timeTable);
			timeTable.setStudent(student);
			timetableSer.addTimeTable(timeTable);

		}
		List<ClassHP> classes = classSer.getClassHP();
		model.addAttribute("classes", classes);
		model.addAttribute("newTimeTable", new qldt.TimeTable());
		return "addTimeTable";
	}

	@GetMapping("/TimeTable")
	public String TimeTable(Model model) {

		List<ClassHP> classes = classSer.getClassHP();
		model.addAttribute("classes", classes);
		model.addAttribute("newTimeTable", new qldt.TimeTable());
		return "addTimeTable";
	}

	@GetMapping("/showTimeTable")
	public String TimeTable_show(Model model, Principal principal) {
		List<TimeTable> timetable_student = new ArrayList<>();
		String username = principal.getName();

		List<TimeTable> list_timetable = timetableSer.getTimeTable();
		for (TimeTable timetable : list_timetable) {
			if (timetable.getStudent().getAppUser().getUserName().equalsIgnoreCase(username)) {
				timetable_student.add(timetable);
			}
		}
		model.addAttribute("timetable_student", timetable_student);
		return "showtimetable";

	}

	@GetMapping("/showTimeTable/delete/{ID}")
	public String deleteTimeTable(@PathVariable("ID") Long ID, HttpSession session) {
		timetableSer.deleteByTimeTableId(ID);
		session.setAttribute("msg", "The User ID " + ID + " Deleted Succesfully");
		return "redirect:/showTimeTable";
	}

}