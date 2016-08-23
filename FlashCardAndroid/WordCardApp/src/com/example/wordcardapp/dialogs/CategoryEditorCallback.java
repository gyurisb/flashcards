package com.example.wordcardapp.dialogs;

/**
 * Interface for callback methods 
 * called from EditCategoryDialog
 * defined in CategoriesActivity
 * 
 * @author Bence
 *
 */
public interface CategoryEditorCallback {

	public void edit(String name, String lang, boolean isPublic);
}
