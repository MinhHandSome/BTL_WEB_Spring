package qldt.data;
//import org.springframework.data.repository.CrudRepository;

import org.springframework.data.jpa.repository.JpaRepository;

import qldt.AppUser;


public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	AppUser findByUserName(String userName);
}


