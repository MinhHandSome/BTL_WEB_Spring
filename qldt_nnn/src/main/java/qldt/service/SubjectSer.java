package qldt.service;
import java.util.List;
import java.util.Optional;


import qldt.Subject;

import qldt.data.SubjectRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectSer {
	   @Autowired
	    private SubjectRepo subjectRepo;

	    public Subject addSubject(Subject subject){
	        return subjectRepo.save(subject);
	    }

	    public List<Subject> getSubject(){
	        return subjectRepo.findAll();
	    }

	    public Subject getSjdByID(long ID){

	        Optional<Subject> model=subjectRepo.findById(ID);

	        if (model.isPresent())
	        {
	            return model.get();
	        }
	        return null;
	    }
	   
	    public void deleteBySubjectId(Long ID) { 
	    	subjectRepo.deleteById(ID); 
	    	
	    	}
}
