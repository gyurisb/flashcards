package com.example.wordcardapp;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.example.wordcardapp.comm.RetrofitWrapper;
import com.example.wordcardapp.dialogs.CategoryEditDialog;
import com.example.wordcardapp.dialogs.CategoryEditorCallback;
import com.example.wordcardapp.dialogs.CategoryUnlockCallback;
import com.example.wordcardapp.dialogs.NameCallback;
import com.example.wordcardapp.dialogs.NameDialog;
import com.example.wordcardapp.model.Category;
import com.example.wordcardapp.utils.Toaster;

public class CategoriesActivity extends CategoryListActivity {
	
	@Override
	protected Call<Category[]> callAPI() {
		return RetrofitWrapper.getCategoryAPI().getOwnCategories();
	}
	
	/**
	 * Callback for category creation in dialog
	 */
	protected CategoryEditorCallback createCategory = new CategoryEditorCallback() {

		@Override
		public void edit(String name, String lang, boolean isPublic) {
			// Create category object
			Category cat = new Category(name, lang, isPublic, "");
			// Send object to server
			Call<Void> call = RetrofitWrapper.getCategoryAPI().createCategory(cat);
			refreshCategories(call);
		}
		
	};
	
	/**
	 * @return first Category from adapter with @param name
	 * 			or null if not found
	 */
	protected Category searchCategory(String name) {
		if(adapter==null) {
			return null;
		}
		for(int i=0;i<adapter.getCount();i++) {
			Category cat = adapter.getItem(i);
			if(cat!=null && cat.Name!=null && cat.Name.equals(name)) {
				return cat;
			}
		}
		return null;
	}

	/**
	 * Enque call and get categories
	 * @param call Call to enque
	 */
	protected void refreshCategories(Call<Void> call) {
		call.enqueue(new Callback<Void>() {

			@Override
			public void onFailure(Throwable arg0) {
				Toaster.error(getApplicationContext(), 
						getString(R.string.failed_to_access_categories));
			}

			@Override
			public void onResponse(Response<Void> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					// Refresh categories
					getCategories();
				}
				else {
					Toaster.error(getApplicationContext(), 
							getString(R.string.failed_to_access_categories));
				}
			}

		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getCategories();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Category selected = (Category) getListAdapter().getItem(position);
		// Start CardListActivity with selected Category
		Intent intent = new Intent(this, CardListActivity.class);
		intent.putExtra("CategoryId", selected.ID);
		intent.putExtra("Category", (Serializable)selected);
		startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.categories, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.create_category_menu) {
			(new CategoryEditDialog(createCategory))
				.show(getFragmentManager(), getString(R.string.create_category_menu));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
