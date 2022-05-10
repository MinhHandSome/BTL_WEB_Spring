package qldt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import qldt.AppRole;

import qldt.data.AppRoleRepository;

@Service
public class AppRoleSer {
	@Autowired
	private AppRoleRepository appRoleRepo;

	public AppRole findAppRole(String roleName) {
		List<AppRole> approles = appRoleRepo.findByRoleName(roleName);
		return approles.get(0);

	}
}
