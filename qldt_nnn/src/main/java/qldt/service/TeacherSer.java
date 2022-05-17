package qldt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qldt.Subject;
import qldt.Teacher;

import qldt.data.TeacherRepo;
@Service
public class TeacherSer {
	 @Autowired
	    private TeacherRepo teacherRepo;

	    public Teacher addTeacher(Teacher teacher){
	        return teacherRepo.save(teacher);
	    }

	    public List<Teacher> getTeacher(){
	        return teacherRepo.findAll();
	    }

	    public Teacher getStdByID(long ID){

	        Optional<Teacher> model=teacherRepo.findById(ID);
	       
	        if (model.isPresent())
	        {
	            return model.get();
	        }
	        return null;
	    }
	    public Teacher getTByName(String fullName) {
	    	List<Teacher> teachers = teacherRepo.findAll();
	    	Teacher result = new Teacher();
	    	for(Teacher teacher: teachers) {
	    		if(teacher.getFullName().equalsIgnoreCase(fullName)) {
	    			result = teacher;
	    			break;
	    		}
	    	}
	    	return result;
	    }
	    public void deleteTeacherId(Long ID) { 
	    	teacherRepo.deleteById(ID); 
	    	
	    	}
}
