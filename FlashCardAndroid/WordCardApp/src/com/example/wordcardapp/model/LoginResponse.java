package com.example.wordcardapp.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
	
	public String access_token;
	
	public String token_type;
	
	public int expires_in;
	
	public String userName;
	
	@SerializedName(".issued")
	public String issued;
	
	@SerializedName(".expires")
	public String expires;
}
