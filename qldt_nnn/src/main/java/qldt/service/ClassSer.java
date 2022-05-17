package qldt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qldt.Classs;
import qldt.Student;
import qldt.data.ClassRepo;


@Service
public class ClassSer {
	@Autowired
	private ClassRepo classRepo;

	public Classs addClass(Classs classs) {
		return classRepo.save(classs);
	}

}
