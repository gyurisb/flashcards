package com.example.wordcardapp.comm;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

import com.example.wordcardapp.model.FlashCard;
import com.example.wordcardapp.model.FlashCards;

public interface CardAPI {

	@GET("/cards")
	public Call<FlashCards> getCards(@Query("category") int categoryId);
	
	@GET("/cards")
	public Call<FlashCards> getRandomCards(@Query("rand") int numberOfCards);
	
	@GET("/cards/{id}")
	public Call<FlashCard> getCard(@Path("id") int id);
	
	@POST("/cards")
	public Call<Void> createCard(@Body FlashCard card);
	
	@PUT("/cards/{id}")
	public Call<Void> modifyCard(@Path("id") int id, @Body FlashCard card);
	
	@DELETE("/cards/{id}")
	public Call<Void> removeCard(@Path("id") int id);
	
}
