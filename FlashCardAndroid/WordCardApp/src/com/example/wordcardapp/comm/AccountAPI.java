package com.example.wordcardapp.comm;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

import com.example.wordcardapp.model.LoginResponse;
import com.example.wordcardapp.model.RegisterParams;

public interface AccountAPI {

	@POST("/account/register")
	public Call<Void> registerUser(@Body RegisterParams params);
	
	/**
	 * @param grantType must be "password"
	 */
	@FormUrlEncoded
	@POST("/token")
	public Call<LoginResponse> loginUser(
			@Field("grant_type") String grantType,
			@Field("username") String email,
			@Field("password") String pass
			);
	
}
