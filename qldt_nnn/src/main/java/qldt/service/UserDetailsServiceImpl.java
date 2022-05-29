package qldt.service;

import java.util.ArrayList;
import java.util.List;


import qldt.data.AppUserRepository;
import qldt.data.UserRoleRepository;
import qldt.AppUser;
import qldt.UserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
//    private AppUserDAO appUserDAO;
    private AppUserRepository appUserRepository;

    @Autowired
//    private AppRoleDAO appRoleDAO;
    private UserRoleRepository uerRoleRepository;
    
 
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        AppUser appUser = this.appUserRepository.findByUserName(userName);

        if (appUser == null) {
            System.out.println("User not found! " + userName);
            throw new UsernameNotFoundException("User " + userName + " was not found in the database");
        }

        System.out.println("Found User: " + appUser);

        
        List<UserRole> userRoles = this.uerRoleRepository.findByAppUser(appUser);

        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        if (userRoles != null) {
            for (UserRole role : userRoles) {
              
                GrantedAuthority authority = new SimpleGrantedAuthority(role.getAppRole().getRoleName());
                grantList.add(authority);
            }
        }

        UserDetails userDetails = (UserDetails) new User(appUser.getUserName(), // username password phana loai
                appUser.getEncrytedPassword(), grantList);

        return userDetails;
    }

}