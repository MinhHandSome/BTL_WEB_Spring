package qldt.service;
import qldt.AppUser;
import qldt.Student;
import qldt.data.AppUserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserSer {

    @Autowired
    private AppUserRepository appUserRepo;

	public List<AppUser> getAppUser() {
		return appUserRepo.findAll();
	}

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
    public boolean checkExistedUserName( String username) {
    	List<AppUser> appUsers= appUserRepo.findAll();
    	boolean check_existed = false;
    	for(AppUser appUser: appUsers) {
    		if(appUser.getUserName().equalsIgnoreCase(username)) {
    			check_existed=true;
    			break;
    		}
    	}
    	return check_existed;
    }

}