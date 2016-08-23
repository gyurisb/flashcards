package com.example.wordcardapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.wordcardapp.R;

/**
 * Dialog to display input fields 
 * for Category properties
 * 
 * @author Bence
 *
 */
public class CategoryEditDialog extends AbstractDialog {

	private CategoryEditorCallback editor;
	
	/**
	 * Constructor
	 * @param callback called with input values when OK is clicked
	 */
	public CategoryEditDialog(CategoryEditorCallback callback) {
		super(R.layout.dialog_category_properties);
		editor = callback;
	}
	
	@Override
	protected void onOkClicked(DialogInterface dialog) {
		// Get data from dialog
		Dialog d = (Dialog)dialog;
		EditText inputField = 
				(EditText)d.findViewById(R.id.categoryNameField);
		CheckBox checkBox = 
				(CheckBox)d.findViewById(R.id.isCategoryPublic);
		// Send data to server
		editor.edit(inputField.getText().toString(), "en",
				checkBox.isChecked());
	}

}
