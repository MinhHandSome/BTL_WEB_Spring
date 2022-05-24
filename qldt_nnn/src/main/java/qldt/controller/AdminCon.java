package qldt.controller

;

import qldt.AppUser;
import qldt.Student;
import qldt.Teacher;
import qldt.Subject;
import qldt.UserRole;
import qldt.ClassHP;
import qldt.data.StudentRepo;
import qldt.data.SubjectRepo;
import qldt.data.TeacherRepo;
import qldt.service.AppRoleSer;
import qldt.service.AppUserSer;
import qldt.service.ClassSer;
import qldt.service.StudentSer;
import qldt.service.SubjectSer;
import qldt.service.TeacherSer;
import qldt.service.UserRoleSer;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
	@Autowired
	private ClassSer classSer;

	@PostMapping("/addStudent")
	public String addStudent(@ModelAttribute Student student, @ModelAttribute AppUser appUser,
			@ModelAttribute UserRole userRole, Model model, HttpSession session) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(appUser.getEncrytedPassword());
		appUser.setEnabled(true);
		appUser.setEncrytedPassword(encodedPassword);
		if (studentSer.checkExistEmail(student.getEmail()) || studentSer.checkExistMSV(student.getMSV())
				|| appUserSer.checkExistedUserName(appUser.getUserName())) {
			session.setAttribute("msg", "Student Added Failed...");
		} else {
			AppUser temp = appUserSer.addAppUser(appUser);
			student.setAppUser(temp);
			studentSer.addStudent(student);
			userRole.setAppUser(appUser);
			userRole.setAppRole(appRoleSer.findAppRole("ROLE_USER"));
			userRoleSer.addUserRole(userRole);
			session.setAttribute("msg", "Student Added Sucessfully...");
		}

		model.addAttribute("newUserRole", new UserRole());
		model.addAttribute("newAppUser", new AppUser());
		model.addAttribute("newStudent", new Student());

		return "addStudent";
	}

	@PostMapping("/addTeacher")
	public String addTeacher(@ModelAttribute Teacher teacher, @ModelAttribute AppUser appUser,
			@ModelAttribute UserRole userRole, Model model, HttpSession session) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(appUser.getEncrytedPassword());
		appUser.setEnabled(true);
		appUser.setEncrytedPassword(encodedPassword);
		if (teacherSer.checkExistEmail(teacher.getEmail()) || appUserSer.checkExistedUserName(appUser.getUserName())) {
			session.setAttribute("msg", "Teacher Added Failed...");
		} else {
			AppUser temp = appUserSer.addAppUser(appUser);
			teacher.setAppUser(temp);
			teacherSer.addTeacher(teacher);
			userRole.setAppUser(appUser);
			userRole.setAppRole(appRoleSer.findAppRole("ROLE_USER_TEACHER"));
			userRoleSer.addUserRole(userRole);
			session.setAttribute("msg", "Teacher Added Sucessfully...");
		}

		model.addAttribute("newUserRole", new UserRole());
		model.addAttribute("newAppUser", new AppUser());
		model.addAttribute("newTeacher", new Teacher());

		return "addTeacher";
	}

	@PostMapping("/addSubject")
	public String addSubject(@ModelAttribute Subject subject, Model model, HttpSession session) {

		if (subjectSer.checkExistNameSubject(subject.getName_subject())) {
			session.setAttribute("msg", "Subject Added Failed...");
		} else {

			subjectSer.addSubject(subject);
			session.setAttribute("msg", "Subject Added Sucessfully...");
		}
		model.addAttribute("newSubject", new Subject());
		return "addSubject";
		// return "redirect:/Teachershow";
	}

	@PostMapping("/addClass")
	public String addClass(@ModelAttribute ClassHP classHP, Model model, HttpSession session) {
		System.out.println(classHP.getTeacher().getFullName());
		System.out.println(classHP.getSubject().getName_subject());
		Subject subject = subjectSer.getSjByName(classHP.getSubject().getName_subject());
		Teacher teacher = teacherSer.getTByName(classHP.getTeacher().getFullName());

		classHP.setTeacher(teacher);
		classHP.setSubject(subject);

		if (classSer.checkExistedRoomTime(classHP.getRoom(), classHP.getTime())) {
			session.setAttribute("msg", "Class Added Failed...");
		} else {

			classSer.addClass(classHP);
			session.setAttribute("msg", "Class Added Sucessfully...");
		}

		List<Subject> subjects = subjectSer.getSubject();
		List<Teacher> teachers = teacherSer.getTeacher();
		model.addAttribute("subjects", subjects);
		model.addAttribute("teachers", teachers);
		model.addAttribute("newClass", new ClassHP());

		return "addClass";
	}

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

	@GetMapping("/Class")
	public String Class(Model model) {
		List<Subject> subjects = subjectSer.getSubject();
		List<Teacher> teachers = teacherSer.getTeacher();
		model.addAttribute("subjects", subjects);
		model.addAttribute("teachers", teachers);
		model.addAttribute("newClass", new ClassHP());

		return "addClass";

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

	@GetMapping("/Classshow")
	public String ClHome(Model model) {
		List<ClassHP> classHP = classSer.getClassHP();
		model.addAttribute("classHP", classHP);
		return "Classshow";
	}

	@GetMapping("/Studentshow/edit/{ID}")
	public String editST(@PathVariable("ID") long ID, Model m) {
		Student student = studentSer.getStdByID(ID);
		m.addAttribute("student", student);
		return "StudentEdit";
	}

	@GetMapping("/Teachershow/edit/{ID}")
	public String editT(@PathVariable("ID") long ID, Model m) {
		Teacher teacher = teacherSer.getTeacherByID(ID);
		m.addAttribute("teacher", teacher);
		return "TeacherEdit";
	}

	@GetMapping("/Subjectshow/edit/{ID}")
	public String editSj(@PathVariable("ID") long ID, Model m) {
		Subject subject = subjectSer.getSjdByID(ID);
		m.addAttribute("subject", subject);
		return "SubjectEdit";
	}

	@GetMapping("/Classshow/edit/{ID}")
	public String editCl(@PathVariable("ID") long ID, Model model) {
		ClassHP classHP = classSer.getCldByID(ID);
		List<Subject> subjects = subjectSer.getSubject();
		List<Teacher> teachers = teacherSer.getTeacher();
		model.addAttribute("subjects", subjects);
		model.addAttribute("teachers", teachers);
		model.addAttribute("classHP", classHP);
		return "ClassEdit";
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

	@PostMapping("/Subjectshow/edit/UpdateSubject")
	public String UpdateSubject(@ModelAttribute Subject subject, Model model, HttpSession session) {
		List<Subject> subjects = subjectSer.getSubject();
		boolean check_update = true;
		for (Subject subject1 : subjects) {
			if (subject1.getName_subject().equalsIgnoreCase(subject.getName_subject())) {
				if (subject1.getID() == subject.getID()) {
					check_update = true;
					break;
				} else {
					check_update = false;
				}
			}

		}
		if (check_update == true) {
			subjectSer.addSubject(subject);
//		model.addAttribute("subject", new Subject());
			session.setAttribute("msg", "Subject ID:" + subject.getID() + " updated Sucessfully...");
			return "redirect:/Subjectshow";
		}
		model.addAttribute("subject", new Subject());
		session.setAttribute("msg", "Subject ID:" + subject.getID() + " update failed");
		Long idSubject_string = subject.getID();

		return "redirect:/Subjectshow}";

	}

	@PostMapping("/Classshow/edit/UpdateClass")
	public String UpdateClass(@ModelAttribute ClassHP classHP, Model model, HttpSession session) {

		Subject subject = subjectSer.getSjByName(classHP.getSubject().getName_subject());
		Teacher teacher = teacherSer.getTByName(classHP.getTeacher().getFullName());

		classHP.setTeacher(teacher);
		classHP.setSubject(subject);

		if (classSer.checkExistedRoomTime(classHP.getRoom(), classHP.getTime())) {
			session.setAttribute("msg", "...");
		} else {

			classSer.addClass(classHP);
			session.setAttribute("msg", "Class Added Sucessfully...");
			return "redirect:/Classshow";
		}

		List<Subject> subjects = subjectSer.getSubject();
		List<Teacher> teachers = teacherSer.getTeacher();
		model.addAttribute("subjects", subjects);
		model.addAttribute("teachers", teachers);
		model.addAttribute("classHP", new ClassHP());

		return "ClassEdit";
		// return "redirect:/Classshow";
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

	@GetMapping("/Subjectshow/delete/{ID}")
	public String deleteSubject(@PathVariable("ID") Long ID, HttpSession session) {
		Subject subject = subjectSer.getSjdByID(ID);
		ClassHP classHP = classSer.findClassbySubject(subject);
		if (classHP != null) {
			classSer.deleteClasById(classHP.getID());
		}
		subjectSer.deleteBySubjectId(ID);
		session.setAttribute("msg", "The Subject ID " + ID + " Deleted Succesfully");
		return "redirect:/Subjectshow";
	}

	@GetMapping("/Teachershow/delete/{ID}")
	public String deleteTeacher(@PathVariable("ID") Long ID, HttpSession session) {
		Teacher teacher = teacherSer.getTeacherByID(ID);
		ClassHP classHP = classSer.findClassByTeacher(teacher);
		if (classHP != null) {
			classSer.deleteClasById(classHP.getID());
		}
		AppUser appUser = teacher.getAppUser();
		Long idUser = appUser.getUserId();
		UserRole ul = userRoleSer.findAppRole(appUser);

		userRoleSer.deleteUserRoleByAppUser(ul.getId());
		teacherSer.deleteTeacherId(ID);
		appUserSer.deleteByAppUserId(idUser);
		session.setAttribute("msg", "The User ID " + ID + " Deleted Succesfully");
		return "redirect:/Teachershow";
	}

	@GetMapping("/Classshow/delete/{ID}")
	public String deleteClass(@PathVariable("ID") Long ID, HttpSession session) {

		classSer.deleteClasById(ID);
		session.setAttribute("msg", "The Subject ID " + ID + " Deleted Succesfully");
		return "redirect:/Classshow";
	}
}