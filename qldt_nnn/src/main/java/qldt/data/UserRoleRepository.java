package qldt.data;
//import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import qldt.AppUser;
import qldt.UserRole;


public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
	List<UserRole> findByAppUser(AppUser appUser);
}


