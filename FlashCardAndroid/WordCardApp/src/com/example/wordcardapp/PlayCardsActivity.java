package com.example.wordcardapp;

import retrofit.Call;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.wordcardapp.comm.RetrofitWrapper;
import com.example.wordcardapp.model.Category;

public class PlayCardsActivity extends CategoryListActivity {
	
	@Override
	protected Call<Category[]> callAPI() {
		return RetrofitWrapper.getCategoryAPI().getCategories();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Category selected = (Category) getListAdapter().getItem(position);
		// Start CardGameActivity with selected Category
		Intent intent = new Intent(this, CardGameActivity.class);
		intent.putExtra("RandomCards", false);
		intent.putExtra("CategoryId", selected.ID);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_cards, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.random_cards_menu) {
			Intent intent = new Intent(this, CardGameActivity.class);
			intent.putExtra("RandomCards", true);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
