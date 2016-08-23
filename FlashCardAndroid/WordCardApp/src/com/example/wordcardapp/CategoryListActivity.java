package com.example.wordcardapp;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.wordcardapp.model.Category;
import com.example.wordcardapp.utils.Toaster;

/**
 * Abstract activity class for listing categories
 * @author Bence
 *
 */
public abstract class CategoryListActivity extends ListActivity {
	
	protected ArrayAdapter<Category> adapter;

	protected abstract Call<Category[]> callAPI();
	
	/**
	 * Get categories from server
	 */
	protected void getCategories() {
		callAPI().enqueue(new Callback<Category[]>() {

			@Override
			public void onFailure(Throwable arg0) {
				Toaster.error(getApplicationContext(), 
						getString(R.string.failed_to_access_categories));
			}

			@Override
			public void onResponse(Response<Category[]> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					// Bind data to layout
					adapter = new ArrayAdapter<Category>(
							CategoryListActivity.this,
							R.layout.rowlayout, arg0.body());
					setListAdapter(adapter);
				}
				else {
					Toaster.error(getApplicationContext(), 
							getString(R.string.failed_to_access_categories));
				}
			}
			
		});
	}
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		getCategories();
		
	}

}
