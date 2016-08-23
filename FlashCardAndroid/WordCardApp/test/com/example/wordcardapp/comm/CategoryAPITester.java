package com.example.wordcardapp.comm;

import com.example.wordcardapp.model.Category;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import android.widget.TextView;

public class CategoryAPITester {

	private CategoryAPI api;
	
	private TextView tv;
	
	public CategoryAPITester(Retrofit retrofit, TextView resultTV) {
		// Init API to use
    	// prepare call in Retrofit 2.0
    	api = retrofit.create(CategoryAPI.class);
    	
		tv = resultTV;
	}
	
	/**
	 * Get category1
     * TESTED OK
	 */
	public void getCategory() {
		final int id = 1;
		Call<Category> call = api.getCategory(id);
    	//asynchronous call
    	call.enqueue(new Callback<Category>(){

			@Override
			public void onFailure(Throwable arg0) {
				tv.append("\nonFailure: Failed to get category " + id);
			}

			@Override
			public void onResponse(Response<Category> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					tv.append("\nName of category " + id + ": " + arg0.body().Name);
				}
				else {
					tv.append("\nonResponse: Failed to get category " + id);
				}
			}
    		
    	});
	}
	
	/**
	 * Create new category
     * TESTED
     *  - more categories with the same name can be created
     *  - call should return the id of the new category
	 */
	public void createCategory() {
		final String name = "Idõjárás";
    	Category cat = new Category(name, "en", true, "user1");
    	Call<Void> catCall = api.createCategory(cat);
    	//asynchronous call
    	catCall.enqueue(new Callback<Void>(){

			@Override
			public void onFailure(Throwable arg0) {
				tv.append("\nonFailure: Failed to create category " + name);
			}

			@Override
			public void onResponse(Response<Void> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					tv.append("\nCategory " + name + " created. Message: " + arg0.message());
				}
				else {
					tv.append("\nonResponse: Failed to create category " + name);
				}
			}
    		
    	});
	}
	
	/**
	 * Modify category
	 * TESTED
	 *  - ID must be specified twice!
	 */
	public void modifyCategory() {
		final int id = 5;
		// ID must be specified twice!
    	Category cat = new Category(id, "Tárgyak", "en", true, "user1");
    	Call<Void> call = api.modifyCategory(id, cat);
    	//asynchronous call
    	call.enqueue(new Callback<Void>(){

			@Override
			public void onFailure(Throwable arg0) {
				tv.append("\nonFailure: Failed to modify category " + id);
			}

			@Override
			public void onResponse(Response<Void> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					tv.append("\nCategory " + id + " modified. Message: " + arg0.message());
				}
				else {
					tv.append("\nonResponse: Failed to modify category " + id);
				}
			}
    		
    	});
	}
	
	/**
	 * Remove category
	 * TESTED OK
	 */
	public void removeCategory() {
		final int id = 5;
		Call<Void> call = api.removeCategory(id);
    	//asynchronous call
    	call.enqueue(new Callback<Void>(){

			@Override
			public void onFailure(Throwable arg0) {
				tv.append("\nonFailure: Failed to remove category " + id);
			}

			@Override
			public void onResponse(Response<Void> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					tv.append("\nCategory " + id + " removed. Message: " + arg0.message());
				}
				else {
					tv.append("\nonResponse: Failed to remove category " + id);
				}
			}
    		
    	});
	}
	
	/**
	 * Share category
	 * TESTED OK
	 */
	public void shareCategory() {
		final int id = 4;
		Call<String> call = api.shareCategory(id);
    	//asynchronous call
    	call.enqueue(new Callback<String>(){

			@Override
			public void onFailure(Throwable arg0) {
				tv.append("\nonFailure: Failed to share category " + id);
			}

			@Override
			public void onResponse(Response<String> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					tv.append("\nCategory " + id + " shared. Key: " + arg0.body());
				}
				else {
					tv.append("\nonResponse: Failed to share category " + id);
				}
			}
    		
    	});
	}
	
	/**
	 * Unlock category
	 * TESTED OK
	 */
	public void unlockCategory() {
		final int id = 6;
    	Call<Void> call = api.unlockCategory(id, "Share" + id);
    	//asynchronous call
    	call.enqueue(new Callback<Void>(){

			@Override
			public void onFailure(Throwable arg0) {
				tv.append("\nonFailure: Failed to unlock category " + id);
			}

			@Override
			public void onResponse(Response<Void> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					tv.append("\nCategory " + id + " unlocked. Message: " + arg0.message());
				}
				else {
					tv.append("\nonResponse: Failed to unlock category " + id);
				}
			}
    		
    	});
	}
	
	public void getCategories() {
		Call<Category[]> call = api.getCategories();
    	//asynchronous call
    	call.enqueue(new Callback<Category[]>(){

			@Override
			public void onFailure(Throwable arg0) {
				tv.append("\nonFailure: Failed to get categories");
			}

			@Override
			public void onResponse(Response<Category[]> arg0, Retrofit arg1) {
				if(arg0.isSuccess()) {
					tv.append("\n" + arg0.body().length + " categories found");
				}
				else {
					tv.append("\nonResponse: Failed to get categories");
				}
			}
    		
    	});
	}
	
	// synchronous call would be with execute, in this case you
	// would have to perform this outside the main thread
	// call.execute()

	// to cancel a running request
	// call.cancel();
	// calls can only be used once but you can easily clone them
	//Call<StackOverflowQuestions> c = call.clone();
	//c.enqueue(this);
	
}
