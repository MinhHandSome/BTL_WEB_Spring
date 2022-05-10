package qldt.service;
import qldt.AppRole;
import qldt.AppUser;
import qldt.UserRole;
import qldt.data.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import antlr.collections.List;

@Service
public class UserRoleSer {

    @Autowired
    private UserRoleRepository userRoleRepo;


    public UserRole addUserRole(UserRole userRole){
        return userRoleRepo.save(userRole);
    }
    public  UserRole findAppRole(AppUser appUser) {
    	java.util.List<UserRole> a = userRoleRepo.findByAppUser(appUser);
    	return a.get(0);

	}
    public void deleteUserRoleByAppUser(Long ID) {
    	userRoleRepo.deleteById(ID);
    }
    	

}