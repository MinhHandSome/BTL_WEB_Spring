package qldt;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Entity
public class TimeTable {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ID;
	@ManyToOne
	@JoinColumn(name="class_id",nullable = false)
	private ClassHP classHP;
	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;
	private float diem1 = 0, diem2 = 0, diem3 = 0;
}
