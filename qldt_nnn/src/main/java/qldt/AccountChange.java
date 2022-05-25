package qldt;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AccountChange {
	private String userName;
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
	
}
