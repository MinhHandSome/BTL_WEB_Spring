package qldt.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import qldt.ClassHP;

@Repository
public interface ClassRepo extends JpaRepository<ClassHP,Long>{
    
}
