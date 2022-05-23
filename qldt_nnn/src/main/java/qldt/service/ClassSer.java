package qldt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qldt.ClassHP;
import qldt.Student;
import qldt.Subject;
import qldt.Teacher;
import qldt.data.ClassRepo;

@Service
public class ClassSer {
	@Autowired
	private ClassRepo classRepo;

	public ClassHP addClass(ClassHP classs) {
		return classRepo.save(classs);

	}
	public ClassHP getCldByID(long ID) {

		Optional<ClassHP> model = classRepo.findById(ID);

		if (model.isPresent()) {
			return model.get();
		}
		return null;
	}
	public List<ClassHP> getClassHP() {
		return classRepo.findAll();
	}

	public boolean checkExistedRoomTime(String room, String time) {
		List<ClassHP> classHPs = classRepo.findAll();
		boolean check_exsited = false;
		for (ClassHP classHP : classHPs) {
			if (classHP.getRoom().equalsIgnoreCase(room) && classHP.getTime().equalsIgnoreCase(time)) {
				check_exsited = true;
				break;
			}
		}
		return check_exsited;
	}

	public void deleteClasById(Long ID) {
		classRepo.deleteById(ID);

	}

	public ClassHP findClassbySubject(Subject subject) {
		List<ClassHP> classHPs = classRepo.findAll();
		for (ClassHP classHP : classHPs) {
			if (classHP.getSubject().getName_subject().equalsIgnoreCase(subject.getName_subject())) {
				return classHP;
			}
		}
		return null;
	}
	public  ClassHP findClassByTeacher(Teacher teacher) {
		List<ClassHP> classHPs = classRepo.findAll();
		for (ClassHP classHP : classHPs) {
			if (classHP.getTeacher().getID() == teacher.getID()) {
				return classHP;
			}
		}
		return null;
	}
}
