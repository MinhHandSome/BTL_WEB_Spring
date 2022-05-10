package qldt.data;
//import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import qldt.AppRole;
import qldt.AppUser;
import qldt.UserRole;


public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
	List<AppRole> findByRoleName(String roleName);

}


