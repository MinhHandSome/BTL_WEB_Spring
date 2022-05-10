package qldt.controller

;

import qldt.AppUser;
import qldt.Student;
import qldt.Teacher;
import qldt.Subject;
import qldt.UserRole;
import qldt.data.StudentRepo;
import qldt.data.SubjectRepo;
import qldt.data.TeacherRepo;
import qldt.service.AppRoleSer;
import qldt.service.AppUserSer;
import qldt.service.StudentSer;
import qldt.service.SubjectSer;
import qldt.service.TeacherSer;
import qldt.service.UserRoleSer;
import java.security.Principal;
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
import java.util.List;

@Controller
public class AdminCon {
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

	@PostMapping("/addStudent")
	public String addStudent(@ModelAttribute Student student, @ModelAttribute AppUser appUser,
			@ModelAttribute UserRole userRole, Model model, HttpSession session) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(appUser.getEncrytedPassword());
		appUser.setEnabled(true);
		appUser.setEncrytedPassword(encodedPassword);
		AppUser temp = appUserSer.addAppUser(appUser);
		student.setAppUser(temp);
		studentSer.addStudent(student);
		userRole.setAppUser(appUser);
		userRole.setAppRole(appRoleSer.findAppRole("ROLE_USER"));
		userRoleSer.addUserRole(userRole);

		model.addAttribute("newUserRole", new UserRole());
		model.addAttribute("newAppUser", new AppUser());
		model.addAttribute("newStudent", new Student());
		session.setAttribute("msg", "Student Added Sucessfully...");
		return "addStudent";
	}

	@PostMapping("/addTeacher")
	public String addTeacher(@ModelAttribute Teacher teacher, @ModelAttribute AppUser appUser,
			@ModelAttribute UserRole userRole, Model model, HttpSession session) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(appUser.getEncrytedPassword());
		appUser.setEnabled(true);
		appUser.setEncrytedPassword(encodedPassword);
		AppUser temp = appUserSer.addAppUser(appUser);
		teacher.setAppUser(temp);
		teacherSer.addTeacher(teacher);
		userRole.setAppUser(appUser);
		userRole.setAppRole(appRoleSer.findAppRole("ROLE_USER_TEACHER"));
		userRoleSer.addUserRole(userRole);

		model.addAttribute("newUserRole", new UserRole());
		model.addAttribute("newAppUser", new AppUser());
		model.addAttribute("newTeacher", new Teacher());
		session.setAttribute("msg", "Teacher Added Sucessfully...");
		return "addTeacher";
	}

	@PostMapping("/addSubject")
	public String addSubject(@ModelAttribute Subject subject, Model model, HttpSession session) {
		int check_existed = 0;
		List<Subject> subjects = subjectSer.getSubject();
		for (Subject subject1 : subjects) {
			if (subject1.getName_subject().equalsIgnoreCase(subject.getName_subject())) {
				check_existed = 1;
				session.setAttribute("msg", "Subject Added Failed...");
				break;
			}
		}
		if (check_existed == 1) {
			subjectSer.addSubject(subject);

			session.setAttribute("msg", "Subject Added Sucessfully...");
//			return "addSubject";
		}
//		return "redirect:/addSubject";
		model.addAttribute("newSubject", new Subject());
		return "addSubject";
	}

//    @PostMapping("AssignSubjectConform")
//    public String AssignSubjectConform(@ModelAttribute Student student, Model model){
////        System.out.println("type added");
////        StudentSer.addStudent(Student);
//        Student student1=studentRepo.findById(student.getID()).get();
//        student1.setSubjects(student.getSubjects());
//
//
//
//
//        studentSer.addStudent(student1);
//
//        List<Student> student2 =studentSer.getStudent();
//        model.addAttribute("student",  student2);
//
//        return "Studentshow";
//    }

	@GetMapping("/Student")
	public String Student(Model model) {
		model.addAttribute("newAppUser", new AppUser());
		model.addAttribute("newStudent", new Student());
		model.addAttribute("newUserRole", new UserRole());
		return "addStudent";

	}

	@GetMapping("/Teacher")
	public String Teacher(Model model) {
		model.addAttribute("newAppUser", new AppUser());
		model.addAttribute("newTeacher", new Teacher());
		model.addAttribute("newUserRole", new UserRole());
		return "addTeacher";

	}

	@GetMapping("/Subject")
	public String Subject(Model model) {
		model.addAttribute("newSubject", new Subject());

		return "addSubject";

	}

//    @GetMapping("/")
//    public String Home(Model model){
//
//        return "adminPage";
//
//    }
//

	@GetMapping("/student_home/Info")
	public String Student_Info(Model model, Principal principal) {
		List<Student> students = studentSer.getStudent();
		String userName = principal.getName();
		AppUser appUser = appUserSer.findAppUserbyUsername(userName);
		long userId = appUser.getUserId();
		for (Student student : students) {
			if (student.getAppUser().getUserId() == userId) {
				model.addAttribute("student", student);
				return "studentInfo";
			}
		}
		return "studentInfo";
	}

	@GetMapping("/teacher_home/Info")
	public String Teacher_Info(Model model, Principal principal) {
		List<Teacher> teachers = teacherSer.getTeacher();
		String userName = principal.getName();
		AppUser appUser = appUserSer.findAppUserbyUsername(userName);
		long userId = appUser.getUserId();
		for (Teacher teacher : teachers) {
			if (teacher.getAppUser().getUserId() == userId) {
				model.addAttribute("teacher", teacher);
				return "teacherInfo";
			}
		}
		return "teacherInfo";
	}

	@GetMapping("/Studentshow")
	public String STHome(Model model) {
		List<Student> student = studentSer.getStudent();
		model.addAttribute("student", student);
		return "Studentshow";
	}

	@GetMapping("/Teachershow")
	public String TeHome(Model model) {
		List<Teacher> teacher = teacherSer.getTeacher();
		model.addAttribute("teacher", teacher);
		return "Teachershow";
	}

	@GetMapping("/Subjectshow")
	public String SuHome(Model model) {
		List<Subject> subject = subjectSer.getSubject();
		model.addAttribute("subject", subject);
		return "Subjectshow";
	}

	@GetMapping("/Studentshow/edit/{ID}")
	public String editST(@PathVariable("ID") long ID, Model m) {
		Student student = studentSer.getStdByID(ID);
		m.addAttribute("student", student);
		return "StudentEdit";
	}

	@GetMapping("/Teachershow/edit/{ID}")
	public String editT(@PathVariable("ID") long ID, Model m) {
		Teacher teacher = teacherSer.getStdByID(ID);
		m.addAttribute("teacher", teacher);
		return "TeacherEdit";
	}

	@GetMapping("/Studentshow/edit_account/{ID}")
	public String editAccountStudent(@PathVariable("ID") long ID, Model m) {

		AppUser appUser = studentRepo.getById(ID).getAppUser();
		m.addAttribute("appUser", appUser);

		return "EditAccountStudent";
	}

	@GetMapping("/Teachershow/edit_account/{ID}")
	public String editAccountTeacher(@PathVariable("ID") long ID, Model m) {

		AppUser appUser = teacherRepo.getById(ID).getAppUser();
		m.addAttribute("appUser", appUser);

		return "EditAccountTeacher";
	}

	@PostMapping("/Studentshow/edit/UpdateStudent")
	public String UpdateStudent(@ModelAttribute Student student, Model model, HttpSession session) {

		studentSer.addStudent(student);

		model.addAttribute("newAppUser", new AppUser());
		model.addAttribute("newStudent", new Student());
		session.setAttribute("msg", "Student Edited Sucessfully...");
		return "addStudent";
	}

	@PostMapping("/Teachershow/edit/UpdateTeacher")
	public String UpdateTeacher(@ModelAttribute Teacher teacher, Model model, HttpSession session) {

		teacherSer.addTeacher(teacher);

		model.addAttribute("newAppUser", new AppUser());
		model.addAttribute("newTeacher", new Teacher());
		session.setAttribute("msg", "Teacher Edited Sucessfully...");
		return "redirect:/Teachershow";
	}

	@PostMapping("/Studentshow/edit_account/UpdateAccountStudent")
	public String UpdateAccountStudent(@ModelAttribute AppUser appUser, Model model, HttpSession session) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(appUser.getEncrytedPassword());
		appUser.setEnabled(true);
		appUser.setEncrytedPassword(encodedPassword);
		appUserSer.addAppUser(appUser);

		model.addAttribute("newAppUser", new AppUser());
//		model.addAttribute("newStudent", new Student());
		session.setAttribute("msg", "User Student edited Sucessfully...");
		return "redirect:/Studentshow";
	}

	@PostMapping("/Teachershow/edit_account/UpdateAccountTeacher")
	public String UpdateAccountTeacher(@ModelAttribute AppUser appUser, Model model, HttpSession session) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(appUser.getEncrytedPassword());
		appUser.setEnabled(true);
		appUser.setEncrytedPassword(encodedPassword);
		appUserSer.addAppUser(appUser);

		model.addAttribute("newAppUser", new AppUser());
		session.setAttribute("msg", "User teacher edited Sucessfully...");
		return "redirect:/Teachershow";
	}

	@GetMapping("/Studentshow/delete/{ID}")
	public String deleteStudent(@PathVariable("ID") Long ID, HttpSession session) {
		Student student = studentSer.getStdByID(ID);
		AppUser appUser = student.getAppUser();
		Long idUser = appUser.getUserId();
		UserRole ul = userRoleSer.findAppRole(appUser);

		userRoleSer.deleteUserRoleByAppUser(ul.getId());
		studentSer.deleteByStudentId(ID);
		appUserSer.deleteByAppUserId(idUser);
		session.setAttribute("msg", "The User ID " + ID + " Deleted Succesfully");
		return "redirect:/Studentshow";
	}

	@GetMapping("/Teachershow/delete/{ID}")
	public String deleteTeacher(@PathVariable("ID") Long ID, HttpSession session) {
		Teacher teacher = teacherSer.getStdByID(ID);
		AppUser appUser = teacher.getAppUser();
		Long idUser = appUser.getUserId();
		UserRole ul = userRoleSer.findAppRole(appUser);

		userRoleSer.deleteUserRoleByAppUser(ul.getId());
		teacherSer.deleteTeacherId(ID);
		appUserSer.deleteByAppUserId(idUser);
		session.setAttribute("msg", "The User ID " + ID + " Deleted Succesfully");
		return "redirect:/Teachershow";
	}

}