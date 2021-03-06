package qldt.controller

;

import qldt.AccountChange;
import qldt.AppUser;
import qldt.Student;
import qldt.Teacher;
import qldt.TimeTable;
import qldt.Subject;
import qldt.Notification;
import qldt.UserRole;
import qldt.ClassHP;
import qldt.data.StudentRepo;
import qldt.data.SubjectRepo;
import qldt.data.TeacherRepo;
import qldt.service.AppRoleSer;
import qldt.service.AppUserSer;
import qldt.service.ClassSer;
import qldt.service.NotificationSer;
import qldt.service.StudentSer;
import qldt.service.SubjectSer;
import qldt.service.TeacherSer;
import qldt.service.TimeTableSer;
import qldt.service.UserRoleSer;
import java.security.Principal;
import java.sql.Time;
import java.time.LocalDate;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
import java.util.Date;
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
	@Autowired
	private NotificationSer notificationSer;
	@Autowired
	private TimeTableSer timeTableSer;

	@PostMapping("/addStudent")
	public String addStudent(@ModelAttribute Student student, @ModelAttribute AppUser appUser,
			@ModelAttribute UserRole userRole, Model model, HttpSession session) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(appUser.getEncrytedPassword());
		appUser.setEnabled(true);
		appUser.setEncrytedPassword(encodedPassword);
		if (studentSer.checkExistEmail(student.getEmail())) {
			session.setAttribute("msg_email", "Email already exists");

		}
		if (studentSer.checkExistMSV(student.getMSV())) {
			session.setAttribute("msg_studentID", "Student ID already exists ");
		}
		if (appUserSer.checkExistedUserName(appUser.getUserName())) {
			session.setAttribute("msg_username", "Username already exists ");
		}
		if (!studentSer.checkExistEmail(student.getEmail()) && !studentSer.checkExistMSV(student.getMSV())
				&& !appUserSer.checkExistedUserName(appUser.getUserName())) {
			AppUser temp = appUserSer.addAppUser(appUser);
			student.setAppUser(temp);
			studentSer.addStudent(student);
			userRole.setAppUser(appUser);
			userRole.setAppRole(appRoleSer.findAppRole("ROLE_USER"));
			userRoleSer.addUserRole(userRole);
			session.setAttribute("msg", "Student Added Sucessfully...");
			model.addAttribute("newUserRole", new UserRole());
			model.addAttribute("newAppUser", new AppUser());
			model.addAttribute("newStudent", new Student());
		} else {
			model.addAttribute("newUserRole", userRole);
			model.addAttribute("newAppUser", appUser);
			model.addAttribute("newStudent", student);
		}

		return "addStudent";
	}

	@PostMapping("/addTeacher")
	public String addTeacher(@ModelAttribute Teacher teacher, @ModelAttribute AppUser appUser,
			@ModelAttribute UserRole userRole, Model model, HttpSession session) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(appUser.getEncrytedPassword());
		appUser.setEnabled(true);
		appUser.setEncrytedPassword(encodedPassword);
		if (teacherSer.checkExistEmail(teacher.getEmail())) {
			session.setAttribute("msg_email", "Email already exists");
		}
		if (appUserSer.checkExistedUserName(appUser.getUserName())) {
			session.setAttribute("msg_username", "Username already exists");
		}
		if (!teacherSer.checkExistEmail(teacher.getEmail())
				&& !appUserSer.checkExistedUserName(appUser.getUserName())) {
			AppUser temp = appUserSer.addAppUser(appUser);
			teacher.setAppUser(temp);
			teacherSer.addTeacher(teacher);
			userRole.setAppUser(appUser);
			userRole.setAppRole(appRoleSer.findAppRole("ROLE_USER_TEACHER"));
			userRoleSer.addUserRole(userRole);
			session.setAttribute("msg", "Teacher Added Sucessfully...");
			model.addAttribute("newUserRole", new UserRole());
			model.addAttribute("newAppUser", new AppUser());
			model.addAttribute("newTeacher", new Teacher());
		} else {
			model.addAttribute("newUserRole", userRole);
			model.addAttribute("newAppUser", appUser);
			model.addAttribute("newTeacher", teacher);
		}

		return "addTeacher";
	}

	@PostMapping("/addSubject")
	public String addSubject(@ModelAttribute Subject subject, Model model, HttpSession session) {

		if (subjectSer.checkExistNameSubject(subject.getName_subject())) {
			session.setAttribute("msg_subject", "Subject already exists");
		
			model.addAttribute("newSubject", subject);
			return "addSubject";
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
			session.setAttribute("msg_roomtime", "Room " + classHP.getRoom() + " is not empty on " + classHP.getTime());
			model.addAttribute("newClass", classHP);
		}
		if (classSer.checkBusyTeacher(classHP.getTeacher(), classHP.getTime())) {
			session.setAttribute("msg_busy",
					"Teacher " + classHP.getTeacher().getFullName() + " is busy on" + classHP.getTime());
			model.addAttribute("newClass", classHP);
		}
		if (!classSer.checkExistedRoomTime(classHP.getRoom(), classHP.getTime())
				&& !classSer.checkBusyTeacher(classHP.getTeacher(), classHP.getTime())) {
			classSer.addClass(classHP);
			session.setAttribute("msg", "Class Added Sucessfully...");
			model.addAttribute("newClass", new ClassHP());
		}

		List<Subject> subjects = subjectSer.getSubject();
		List<Teacher> teachers = teacherSer.getTeacher();
		model.addAttribute("subjects", subjects);
		model.addAttribute("teachers", teachers);

		return "addClass";
	}

	@PostMapping("/addNotification")
	public String addNotification(@ModelAttribute Notification notification, Model model, HttpSession session) {
		System.out.println(notification.getContent());
		session.setAttribute("msg", "Notification Added Sucessfully...");
		notificationSer.addNotification(notification);
		model.addAttribute("newNotification", new Notification());
		return "addNotification";
		// return "redirect:/Teachershow";
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

	@GetMapping("/Notification")
	public String Notification(Model model) {

		model.addAttribute("newNotification", new Notification());

		return "addNotification";

	}

	@GetMapping("/ChangePassword")
	public String ChangePassword(Model model, Principal principal) {

		String userName = principal.getName();
		AppUser appUser = appUserSer.findAppUserbyUsername(userName);
		AccountChange accountChange = new AccountChange();
		accountChange.setUserName(appUser.getUserName());

		model.addAttribute("accountChange", accountChange);
		return "UpdateAccount";
	}

	@PostMapping("/UpdateAccount")
	public String UpdateAccount(@ModelAttribute AccountChange accountChange, Principal principal, Model model,
			HttpSession session) {
		String userName = principal.getName();
		AppUser appUser = appUserSer.findAppUserbyUsername(userName);
		String passwordCurrent = appUser.getEncrytedPassword();
		System.out.println(passwordCurrent);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(BCrypt.checkpw(accountChange.getOldPassword(), passwordCurrent));
		boolean checkExisted = false;

		if (BCrypt.checkpw(accountChange.getOldPassword(), passwordCurrent) == true) {
			checkExisted = true;
		}

		if (checkExisted == false) {
			session.setAttribute("msg", "Old Password is incorrect...");
		} else if (accountChange.getNewPassword().equals(accountChange.getConfirmPassword()) == false) {
			session.setAttribute("msg", "New password is the same Confirm password");
		} else {
			String encodedPassword = passwordEncoder.encode(accountChange.getNewPassword());
			appUser.setEnabled(true);
			appUser.setEncrytedPassword(encodedPassword);
			appUserSer.addAppUser(appUser);
			session.setAttribute("msg", "Update Account Sucessfully...");

		}

		return "redirect:/ChangePassword";
	}

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

	@GetMapping("/Notificationshow")
	public String NoHome(Model model) {
		List<Notification> notification = notificationSer.getNotificationt();
		model.addAttribute("notification", notification);
		return "Notificationshow";
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

	@GetMapping("/Notificationshow/edit/{ID}")
	public String editNo(@PathVariable("ID") long ID, Model model) {
		Notification notification = notificationSer.getNodByID(ID);
		model.addAttribute("notification", notification);
		return "NotificationEdit";
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

		if (studentSer.checkExistEmailpassCurrent(student.getEmail(), student.getID())) {
			session.setAttribute("msg_email", "Email already exists");
		}
		if (studentSer.checkExistMSVpassCurrent(student.getMSV(), student.getID())) {
			session.setAttribute("msg_studentID", "Student ID already exists ");
		}

		if (!studentSer.checkExistEmailpassCurrent(student.getEmail(), student.getID())
				&& !studentSer.checkExistMSVpassCurrent(student.getMSV(), student.getID())) {
			studentSer.addStudent(student);

			session.setAttribute("msg", "Student Updated Sucessfully...");
			return "redirect:/Studentshow";
		}

		model.addAttribute("student", student);
		return "StudentEdit";
	}

	@PostMapping("/Teachershow/edit/UpdateTeacher")
	public String UpdateTeacher(@ModelAttribute Teacher teacher, Model model, HttpSession session) {
		if (teacherSer.checkExistEmailPassCurrent(teacher.getEmail(), teacher.getID())) {
			session.setAttribute("msg_email", "Email already exists");
		}
		if (!teacherSer.checkExistEmailPassCurrent(teacher.getEmail(), teacher.getID())) {
			teacherSer.addTeacher(teacher);
			session.setAttribute("msg", "Teacher Edited Sucessfully...");
			return "redirect:/Teachershow";
		}

		model.addAttribute("teacher", teacher);
		return "TeacherEdit";

	}

	@PostMapping("/Notificationshow/edit/UpdateNotification")
	public String UpdateNotification(@ModelAttribute Notification notification, Model model, HttpSession session) {
		Date date = new Date();
		notification.setDate(date);
		notificationSer.addNotification(notification);

		model.addAttribute("newStudent", new Student());
		session.setAttribute("msg", "Notification Edited Sucessfully...");
		return "redirect:/Notificationshow";
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
		session.setAttribute("msg_subject", "Name subject already exists");
		Long idSubject_string = subject.getID();

		return "SubjectEdit";

	}

	@PostMapping("/Classshow/edit/UpdateClass")
	public String UpdateClass(@ModelAttribute ClassHP classHP, Model model, HttpSession session) {

		Subject subject = subjectSer.getSjByName(classHP.getSubject().getName_subject());
		Teacher teacher = teacherSer.getTByName(classHP.getTeacher().getFullName());

		classHP.setTeacher(teacher);
		classHP.setSubject(subject);

		if (classSer.checkExistedRoomTimePassCurrent(classHP.getRoom(), classHP.getTime(),classHP.getID())) {
			session.setAttribute("msg_roomtime", "Room " + classHP.getRoom() + " is not empty on " + classHP.getTime());
			model.addAttribute("classHP", classHP);
		}
		if (classSer.checkBusyTeacherPassCurrent(classHP.getTeacher(), classHP.getTime(),classHP.getID())) {
			session.setAttribute("msg_busy",
					"Teacher " + classHP.getTeacher().getFullName() + " is busy on" + classHP.getTime());
			model.addAttribute("classHP", classHP);
		}
		if (!classSer.checkExistedRoomTime(classHP.getRoom(), classHP.getTime())
				&& !classSer.checkBusyTeacher(classHP.getTeacher(), classHP.getTime())) {
			classSer.addClass(classHP);
			session.setAttribute("msg", "Class Added Sucessfully...");
			model.addAttribute("classHP", new ClassHP());
		}

		List<Subject> subjects = subjectSer.getSubject();
		List<Teacher> teachers = teacherSer.getTeacher();
		model.addAttribute("subjects", subjects);
		model.addAttribute("teachers", teachers);

		return "ClassEdit";
	}

	@PostMapping("/Studentshow/edit_account/UpdateAccountStudent")
	public String UpdateAccountStudent(@ModelAttribute AppUser appUser, Model model, HttpSession session) {
		if (appUserSer.checkExistedUserNamePassCurrent(appUser.getUserName(), appUser.getUserId())) {
			session.setAttribute("msg_username", "Username already exists");
		}
		if (!appUserSer.checkExistedUserNamePassCurrent(appUser.getUserName(), appUser.getUserId())) {

			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = passwordEncoder.encode(appUser.getEncrytedPassword());
			appUser.setEnabled(true);
			appUser.setEncrytedPassword(encodedPassword);
			appUserSer.addAppUser(appUser);
//		model.addAttribute("newStudent", new Student());
			session.setAttribute("msg", "Account Student updated Sucessfully...");
			return "redirect:/Studentshow";
		}
		model.addAttribute("newAppUser", new AppUser());
		return "EditAccountStudent";
	}

	@PostMapping("/Teachershow/edit_account/UpdateAccountTeacher")
	public String UpdateAccountTeacher(@ModelAttribute AppUser appUser, Model model, HttpSession session) {
		if (appUserSer.checkExistedUserNamePassCurrent(appUser.getUserName(), appUser.getUserId())) {
			session.setAttribute("msg_username", "Username already exists");
		}
		if (!appUserSer.checkExistedUserNamePassCurrent(appUser.getUserName(), appUser.getUserId())) {

			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedPassword = passwordEncoder.encode(appUser.getEncrytedPassword());
			appUser.setEnabled(true);
			appUser.setEncrytedPassword(encodedPassword);
			appUserSer.addAppUser(appUser);
//		model.addAttribute("newStudent", new Student());
			session.setAttribute("msg", "Account Teacher updated Sucessfully...");
			return "redirect:/Teachershow";
		}
		model.addAttribute("newAppUser", new AppUser());
		return "EditAccountTeacher";
	}

	@GetMapping("/Studentshow/delete/{ID}")
	public String deleteStudent(@PathVariable("ID") Long ID, HttpSession session) {
		Student student = studentSer.getStdByID(ID);
		AppUser appUser = student.getAppUser();
		Long idUser = appUser.getUserId();
		UserRole ul = userRoleSer.findAppRole(appUser);
		List<TimeTable> timetables = timeTableSer.findTimeTablebyStudenT(student);
		if (timetables != null) {
			for (TimeTable timetable : timetables) {
				timeTableSer.deleteByTimeTableId(timetable.getID());
			}
		}
		userRoleSer.deleteUserRoleByAppUser(ul.getId());
		studentSer.deleteByStudentId(ID);
		appUserSer.deleteByAppUserId(idUser);
		session.setAttribute("msg", "The Student ID:" + student.getMSV() + " Deleted Succesfully");
		return "redirect:/Studentshow";
	}

	@GetMapping("/Subjectshow/delete/{ID}")
	public String deleteSubject(@PathVariable("ID") Long ID, HttpSession session) {
		Subject subject = subjectSer.getSjdByID(ID);
		List<ClassHP> classHPs = classSer.findClassbySubject(subject);
		if (classHPs != null) {
			for (ClassHP classHP : classHPs)
				classSer.deleteClasById(classHP.getID());
		}
		subjectSer.deleteBySubjectId(ID);
		session.setAttribute("msg", "The Subject ID: SJ" + ID + " Deleted Succesfully");
		return "redirect:/Subjectshow";
	}

	@GetMapping("/Teachershow/delete/{ID}")
	public String deleteTeacher(@PathVariable("ID") Long ID, HttpSession session) {
		Teacher teacher = teacherSer.getTeacherByID(ID);
		List<ClassHP> classHPs = classSer.findClassByTeacher(teacher);
		if (classHPs != null) {
			for (ClassHP classHP : classHPs) {
				List<TimeTable> timetables = timeTableSer.findTimeTablebyClassHP(classHP);
				for (TimeTable timetable : timetables) {
					timeTableSer.deleteByTimeTableId(timetable.getID());
				}
				classSer.deleteClasById(classHP.getID());
			}
		}
		AppUser appUser = teacher.getAppUser();
		Long idUser = appUser.getUserId();
		UserRole ul = userRoleSer.findAppRole(appUser);

		userRoleSer.deleteUserRoleByAppUser(ul.getId());
		teacherSer.deleteTeacherId(ID);
		appUserSer.deleteByAppUserId(idUser);
		session.setAttribute("msg", "The Teacher ID: TE" + ID + " Deleted Succesfully");
		return "redirect:/Teachershow";
	}

	@GetMapping("/Classshow/delete/{ID}")
	public String deleteClass(@PathVariable("ID") Long ID, HttpSession session) {
		List<TimeTable> timetables = timeTableSer.findTimeTablebyClassHP(classSer.getCldByID(ID));
		if (timetables != null) {
			for (TimeTable timetable : timetables) {
				timeTableSer.deleteByTimeTableId(timetable.getID());

			}
		}
		classSer.deleteClasById(ID);
		session.setAttribute("msg", "The Class ID: CL" + ID + " Deleted Succesfully");
		return "redirect:/Classshow";
	}

	@GetMapping("/Notificationshow/delete/{ID}")
	public String deleteNotification(@PathVariable("ID") Long ID, HttpSession session) {

		notificationSer.deleteNotificationById(ID);
		session.setAttribute("msg", "The Notification ID " + ID + " Deleted Succesfully");
		return "redirect:/Notificationshow";
	}
}