//package qldt.controller;
//
//import qldt.AppUser;
//import qldt.Student;
//import qldt.Teacher;
//import qldt.UserRole;
//import qldt.data.StudentRepo;
//import qldt.data.TeacherRepo;
//import qldt.service.AppRoleSer;
//import qldt.service.AppUserSer;
//import qldt.service.StudentSer;
//import qldt.service.TeacherSer;
//import qldt.service.UserRoleSer;
//import java.security.Principal;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//
//import javax.servlet.http.HttpSession;
//
//import java.security.Principal;
//import java.util.List;
//@Controller
//public class StudentCon {
//	@Autowired
//	private StudentSer studentSer;
//	@Autowired
//	private AppUserSer appUserSer;
//	@Autowired
//	@GetMapping("/student_home/Info")
//	public String Student_Info(Model model,Principal principal) {
//		List<Student> students = studentSer.getStudent();
//		String userName = principal.getName(); 
//		AppUser appUser = appUserSer.findAppUserbyUsername(userName);
//		long userId = appUser.getUserId();
//		for(Student student:students) {
//			if(student.getAppUser().getUserId()==userId) {
//				model.addAttribute("student",student);
//				return "studentInfo";
//			}
//		}
//		return "studentInfo";	
//	}
//}
