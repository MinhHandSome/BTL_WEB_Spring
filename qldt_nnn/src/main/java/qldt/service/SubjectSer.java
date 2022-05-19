package qldt.service;

import java.util.List;
import java.util.Optional;

import qldt.Student;
import qldt.Subject;

import qldt.data.SubjectRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectSer {
	@Autowired
	private SubjectRepo subjectRepo;

	public Subject addSubject(Subject subject) {
		return subjectRepo.save(subject);
	}

	public List<Subject> getSubject() {
		return subjectRepo.findAll();
	}

	public Subject getSjdByID(long ID) {

		Optional<Subject> model = subjectRepo.findById(ID);

		if (model.isPresent()) {
			return model.get();
		}
		return null;
	}

	public Subject getSjByName(String name_subject) {
		List<Subject> subjects = subjectRepo.findAll();
		Subject result = new Subject();
		for (Subject subject : subjects) {
			if (subject.getName_subject().equalsIgnoreCase(name_subject)) {
				result = subject;
				break;
			}
		}
		return result;
	}

	public boolean checkExistNameSubject(String name) {
		List<Subject> subjects = subjectRepo.findAll();
		boolean check_existed = false;
		for (Subject subject : subjects) {
			if (subject.getName_subject().equalsIgnoreCase(name)) {
				check_existed = true;
				break;
			}
		}
		return check_existed;

	}

	public void deleteBySubjectId(Long ID) {
		subjectRepo.deleteById(ID);

	}
}
