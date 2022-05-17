package qldt.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import qldt.Classs;

@Repository
public interface ClassRepo extends JpaRepository<Classs,Long>{
    
}
