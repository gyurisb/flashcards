package com.example.wordcardapp;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import com.example.wordcardapp.comm.RetrofitWrapper;
import com.example.wordcardapp.model.LoginResponse;
import com.example.wordcardapp.utils.Toaster;
import com.example.wordcardapp.utils.Validator;


public class LoginActivity extends Activity {
	
	private boolean loggedIn;
	private boolean justCreated;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		checkAccount();
		checkUrlIntent();
		this.justCreated = true;
	}    

	@Override
	protected void onResume() {
		super.onResume();
		if (!justCreated && loggedIn) {
			if (RetrofitWrapper.authenticationToken != null) {
				finish();
			}
			else {
				loggedIn = false;
			}
		}
		justCreated = false;
	}
	
	private void checkAccount() {
		SharedPreferences prefs = this.getSharedPreferences(
			      "com.example.wordcardapp", Context.MODE_PRIVATE);
		if (prefs.contains("com.example.wordcardapp.auth.email")) {
			String email = prefs.getString("com.example.wordcardapp.auth.email", "");
			((EditText)findViewById(R.id.loginMailET)).setText(email);
		}
		if (prefs.contains("com.example.wordcardapp.auth.token")) {
			long expirationTime = prefs.getLong("com.example.wordcardapp.auth.expiration", 0);
			long currentTime = new Date().getTime();
			if (expirationTime > currentTime) {
				// Load token for session
				RetrofitWrapper.authenticationToken = prefs.getString("com.example.wordcardapp.auth.token", "");
				// Open menu activity
				this.loggedIn = true;
				startActivity(new Intent(this, MenuActivity.class));
			}
		}
	}
	
	private void checkUrlIntent() {
		Intent intent = getIntent();
		// If started from a share link
		if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			try {
				Uri uri = intent.getData();
				String id = uri.getLastPathSegment();
				Log.d("Intent id", id);
				String key = uri.getQueryParameter("key");
				Log.d("Intent key", key);
				unlockCategory(Integer.parseInt(id), key);
			} catch (Exception e) {
				Toaster.error(LoginActivity.this, getString(R.string.invalid_link_format));
			}
		}
	}

	public void onLogin(View v){
		String email, passwd;
		// Get data from form
		email = ((EditText)findViewById(R.id.loginMailET)).getText().toString();
		passwd = ((EditText)findViewById(R.id.loginPasswordET)).getText().toString();
		// Validate input
		if(!Validator.isValidEmail(email)){
			Toast.makeText(getApplicationContext(), getString(R.string.email_not_valid),
					Toast.LENGTH_LONG).show();
		}
		else if(passwd.isEmpty()){
			Toast.makeText(getApplicationContext(), getString(R.string.pass_missing),
					Toast.LENGTH_LONG).show();
		}
		// All input is valid
		else{
			// Send data to server
			login(email, passwd);
		}
	}
	
	public void onRegisterUser(View v){
		startActivity(new Intent(this, RegisterUserActivity.class));
	}

	protected void onSuccessResponse(String email, LoginResponse response) {
		//persisting login token
		long currentTime = new Date().getTime();
		SharedPreferences prefs = this.getSharedPreferences(
			      "com.example.wordcardapp", Context.MODE_PRIVATE);
		prefs.edit()
			.putString("com.example.wordcardapp.auth.email", email)
			.putString("com.example.wordcardapp.auth.token", response.access_token)
			.putLong("com.example.wordcardapp.auth.expiration", currentTime + response.expires_in*1000)
			.apply();
		// Save token for session
		RetrofitWrapper.authenticationToken = response.access_token;
		// Open menu activity
		this.loggedIn = true;
		startActivity(new Intent(this, MenuActivity.class));
	}

	protected void login(final String email, String pass) {
		// Inform user
		Toast.makeText(LoginActivity.this, getString(R.string.waiting), 
				Toast.LENGTH_LONG).show();
		
		// Send request to server
		Call<LoginResponse> call = 
				RetrofitWrapper.getAccountAPI().loginUser("password", email, pass);
		
		call.enqueue(new Callback<LoginResponse>(){

			@Override
			public void onFailure(Throwable arg0) {
				Toaster.error(LoginActivity.this, getString(R.string.login_failed));
			}

			@Override
			public void onResponse(Response<LoginResponse> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					Toaster.info(LoginActivity.this, getString(R.string.login_successful));
					onSuccessResponse(email, arg0.body());
				}
				else {
					Toaster.error(LoginActivity.this, getString(R.string.login_failed));
				}
			}
    		
    	});
	}
	
	private void unlockCategory(int categoryId, String key) {
		Call<Void> call = RetrofitWrapper.getCategoryAPI().unlockCategory(categoryId, key);
		call.enqueue(new Callback<Void>() {

			@Override
			public void onFailure(Throwable arg0) {
				Toaster.error(getApplicationContext(), 
						getString(R.string.failed_to_access_categories));
			}

			@Override
			public void onResponse(Response<Void> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					Toaster.info(getApplicationContext(), 
							getString(R.string.category_unlocked));
				}
				else {
					Toaster.error(getApplicationContext(), 
							getString(R.string.failed_to_access_categories));
				}
			}
		});
	}
}
