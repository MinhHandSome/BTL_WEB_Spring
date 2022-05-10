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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;


import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
@Entity
public class Student  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long ID;

    private String fullName;
    private String DOB;
    private String Grade;
    private String MSV;
    private String Address;
    private String phoneNumber;
    private String email;
    
    @OneToOne
    @JoinColumn(name = "appuser_id", referencedColumnName = "User_Id")
    private AppUser appUser;
//    public AppUser getAppUser() {
//        return appUser;
//    }
//
//    public void setAppUser(AppUser appUser) {
//        this.appUser = appUser;
//    }

//    @PrePersist
//	void placedAt() {
//		this.placedAt = new Date();
//	}
//    public Set<Subjects> getSubjects() {
//        return subjects;
//    }
//
//    public void setSubjects(Set<Subjects> subjects) {
//        this.subjects = subjects;
//    }
//
//    @ManyToMany
//    @JoinTable(
//            name = "Student_Subject",
//            joinColumns = @JoinColumn(name = "Student_ID"),
//            inverseJoinColumns = @JoinColumn(name = "Subject_ID")
//    )
//
//    private Set<Subjects> subjects = new HashSet<>();
}
