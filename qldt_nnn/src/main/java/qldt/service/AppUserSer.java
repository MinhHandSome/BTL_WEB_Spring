package qldt.service;
import qldt.AppUser;
import qldt.Student;
import qldt.data.AppUserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserSer {

    @Autowired
    private AppUserRepository appUserRepo;


    public AppUser addAppUser(AppUser appUser){
        return appUserRepo.save(appUser);
    }
    public AppUser findAppUserByID(long ID){

        Optional<AppUser> model=appUserRepo.findById(ID);

        if (model.isPresent())
        {
            return model.get();
        }
        return null;
    }
    public void deleteByAppUserId(Long ID) { 
    	appUserRepo.deleteById(ID); 
    	
    	}
    public AppUser findAppUserbyUsername(String username) { 
    	return appUserRepo.findByUserName(username);
    	
    	}

}