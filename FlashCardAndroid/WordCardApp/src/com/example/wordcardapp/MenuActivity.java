package com.example.wordcardapp;

import com.example.wordcardapp.comm.RetrofitWrapper;
import com.example.wordcardapp.utils.Toaster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
	}
	
	public void onPlayCards(View v) {
		startActivity(new Intent(this, PlayCardsActivity.class));
	}
	
	public void onEditCards(View v) {
		startActivity(new Intent(this, CategoriesActivity.class));
	}
	
	public void onShowAccounts(View v) {
		startActivity(new Intent(this, LoginActivity.class));
	}

	public void onLogout(View v) {
		// Forget auth token from memory
		RetrofitWrapper.authenticationToken = null;
		// Forget auth token from storage
		SharedPreferences prefs = this.getSharedPreferences(
			      "com.example.wordcardapp", Context.MODE_PRIVATE);
		prefs.edit()
			.remove("com.example.wordcardapp.auth.token")
			.remove("com.example.wordcardapp.auth.expiration")
			.apply();
		// Inform user
		Toaster.info(getApplicationContext(), getString(R.string.logout_successful));
		finish();
	}
}
