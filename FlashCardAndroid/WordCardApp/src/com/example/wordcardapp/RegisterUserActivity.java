package com.example.wordcardapp;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wordcardapp.comm.RetrofitWrapper;
import com.example.wordcardapp.model.RegisterParams;
import com.example.wordcardapp.utils.Toaster;
import com.example.wordcardapp.utils.Validator;

public class RegisterUserActivity extends Activity {
	
	private static final int minPassLength = 6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_user);
	}
	
	public void onRegisterUser(View v){
		String email, userName, passwd, confPasswd;
		// Get data from form
		email = ((EditText)findViewById(R.id.regMailET)).getText().toString();
		userName = ((EditText)findViewById(R.id.regNameET)).getText().toString();
		passwd = ((EditText)findViewById(R.id.regPassET)).getText().toString();
		confPasswd = ((EditText)findViewById(R.id.regConfPassET)).getText().toString();
		// Validate input
		if(userName.isEmpty()){
			Toast.makeText(getApplicationContext(), 
					getString(R.string.name_missing), Toast.LENGTH_LONG)
				.show();
		}
		else if(!Validator.isValidEmail(email)){
			Toast.makeText(getApplicationContext(), 
					getString(R.string.email_not_valid), Toast.LENGTH_LONG)
				.show();
		}
		else if(passwd.length() < minPassLength){
			Toast.makeText(getApplicationContext(), 
					getString(R.string.min_pass_length_is) + " " + minPassLength
					+ getString(R.string.characters), Toast.LENGTH_LONG)
				.show();
		}
		else if(!passwd.equals(confPasswd)){
			Toast.makeText(getApplicationContext(), 
					getString(R.string.pass_unmatch), Toast.LENGTH_LONG)
				.show();
		}
		// All input is valid
		else{
			// Send data to server
			register(email, userName, passwd, confPasswd);
		}
	}

	protected void register(String email, String name, String pass, String confirmPass) {
		// Assemble request body
		RegisterParams params = new RegisterParams(email, name, pass, confirmPass);
		// Inform user
		Toast.makeText(RegisterUserActivity.this, getString(R.string.waiting), 
				Toast.LENGTH_LONG).show();
		// Send request to server
		Call<Void> call = RetrofitWrapper.getAccountAPI().registerUser(params);
		call.enqueue(new Callback<Void>(){

			@Override
			public void onFailure(Throwable arg0) {
				Toaster.error(RegisterUserActivity.this, 
						getString(R.string.register_failed));
			}

			@Override
			public void onResponse(Response<Void> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					Toaster.info(RegisterUserActivity.this, 
							getString(R.string.register_successful));
					// Jump back to last screen
					RegisterUserActivity.this.finish();
				}
				else {
					Toaster.error(RegisterUserActivity.this, 
							getString(R.string.register_failed));
				}
			}

		});
	}
}
