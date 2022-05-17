package qldt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qldt.ClassHP;

import qldt.data.ClassRepo;

@Service
public class ClassSer {
	@Autowired
	private ClassRepo classRepo;

	public ClassHP addClass(ClassHP classs) {
		return classRepo.save(classs);
	}

	public List<ClassHP> getClassHP() {
		return classRepo.findAll();
	}
}
