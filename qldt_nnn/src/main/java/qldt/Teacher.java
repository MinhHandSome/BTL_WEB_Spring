package qldt;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
@Entity
public class Teacher {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    
    private Long ID;

    private String fullName;
    private String DOB;
    private String Address;
    private String phoneNumber;
    private String email;
    @OneToOne
    @JoinColumn(name = "appuser_id", referencedColumnName = "User_Id")
    private AppUser appUser;
}
