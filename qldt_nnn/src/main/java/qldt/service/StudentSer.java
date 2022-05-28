package qldt.service;

import java.util.List;
import java.util.Optional;

import qldt.Student;
import qldt.data.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentSer {

	@Autowired
	private StudentRepo studentRepo;

	public Student addStudent(Student student) {
		return studentRepo.save(student);
	}

	public List<Student> getStudent() {
		return studentRepo.findAll();
	}

	public Student getStdByID(long ID) {

		Optional<Student> model = studentRepo.findById(ID);

		if (model.isPresent()) {
			return model.get();
		}
		return null;
	}

	public void deleteByStudentId(Long ID) {
		studentRepo.deleteById(ID);

	}

	public boolean checkExistMSV(String MSV) {
		List<Student> students = studentRepo.findAll();
		boolean check_existed = false;
		for (Student student : students) {
			if (student.getMSV().equalsIgnoreCase(MSV)) {
				check_existed = true;
				break;
			}
		}
		return check_existed;

	}

	public boolean checkExistMSVpassCurrent(String MSV, long ID) {
		List<Student> students = studentRepo.findAll();
		boolean check_existed = false;
		for (Student student : students) {
			if (student.getMSV().equalsIgnoreCase(MSV)) {
				if (student.getID() == ID)
					continue;
				check_existed = true;
				break;
			}
		}
		return check_existed;

	}

	public boolean checkExistEmail(String email) {
		List<Student> students = studentRepo.findAll();
		boolean check_existed = false;
		for (Student student : students) {
			if (student.getEmail().equalsIgnoreCase(email)) {
				check_existed = true;
				break;
			}
		}
		return check_existed;

	}

	public boolean checkExistEmailpassCurrent(String email, Long ID) {
		List<Student> students = studentRepo.findAll();
		boolean check_existed = false;
		for (Student student : students) {
			if (student.getEmail().equalsIgnoreCase(email)) {
				if (student.getID() == ID)
					continue;
				check_existed = true;
				break;
			}
		}
		return check_existed;

	}

	public Student getStbyUserName(String username) {
		List<Student> students = studentRepo.findAll();
		for (Student student : students) {
			if (student.getAppUser().getUserName().equalsIgnoreCase(username)) {
				return student;
			}
		}
		return null;
	}

}