package com.example.wordcardapp.model;

public class FlashCard {
	public int ID;		//Auto-incremented by server
	public String Front;	//Hungarian
	public String Back;
	public int CategoryID;
	public String Language;//Back side language codes ISO_639-1_codes 
	
	public FlashCard(String front, String back, String lang, int categoryId) {
		Front = front;
		Back = back;
		Language = lang;
		CategoryID = categoryId;
	}
	
	public String toString() {
		return Front;
	}
}
