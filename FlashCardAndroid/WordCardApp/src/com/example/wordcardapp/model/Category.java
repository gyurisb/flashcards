package com.example.wordcardapp.model;

import java.io.Serializable;

public class Category implements Serializable {
	public int ID;			//Auto-incremented by server
	public String Name;
	public String Language;
	public boolean IsPublic; 	//láthatóság
	public String UserID; 		//tulajdonos
	
	public Category(String name, String lang, boolean isPublic, String userId) {
		Name = name;
		Language = lang;
		IsPublic = isPublic;
		UserID = userId;
	}
	
	public Category(int id, String name, String lang, boolean isPublic, String userId) {
		this(name,lang,isPublic,userId);
		ID = id;
	}
	
	@Override
	public String toString() {
		return Name;
	}
}
