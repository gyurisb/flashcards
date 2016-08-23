package com.example.wordcardapp.comm;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

import com.example.wordcardapp.model.Category;

public interface CategoryAPI {
	
	@GET("/categories")
	public Call<Category[]> getCategories();
	
	@GET("/categories?own=true")
	public Call<Category[]> getOwnCategories();

	@GET("/categories/{id}")
	public Call<Category> getCategory(@Path("id") int id);
	
	@POST("/categories")
	public Call<Void> createCategory(@Body Category cat);
	
	@PUT("/categories/{id}")
	public Call<Void> modifyCategory(@Path("id") int id, @Body Category cat);
	
	@DELETE("/categories/{id}")
	public Call<Void> removeCategory(@Path("id") int id);
	
	@GET("/categories/{id}?share")
	public Call<String> shareCategory(@Path("id") int id);
	
	@PUT("/categories/{id}?unlock")
	public Call<Void> unlockCategory(@Path("id") int id, @Body String key);
}
