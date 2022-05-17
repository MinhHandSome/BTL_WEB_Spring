package qldt;

import java.util.Date;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;


import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
@Entity
public class Classs  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @ManyToOne
    @JoinColumn(name = "teacher_Id", nullable = false)
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "subject_Id", nullable = false)
    private Subject subject;
    private String room;
    private String Time;
    
}