package com.example.wordcardapp.model;

public class RegisterParams {
	public String Email;
	public String DisplayName;
	public String Password;
	public String ConfirmPassword;
	
	public RegisterParams(String email, String name, String pass, String confirmPass) {
		Email = email;
		DisplayName = name;
		Password = pass;
		ConfirmPassword = confirmPass;
	}
}
