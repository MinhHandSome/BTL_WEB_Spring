package qldt.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import qldt.Teacher;

@Repository
public interface TeacherRepo extends JpaRepository<Teacher,Long> {

}
