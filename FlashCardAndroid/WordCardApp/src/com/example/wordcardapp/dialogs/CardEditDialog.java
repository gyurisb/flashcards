package com.example.wordcardapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.EditText;

import com.example.wordcardapp.R;

public class CardEditDialog extends AbstractDialog {
	
	private CardEditorCallback editor;

	public CardEditDialog(CardEditorCallback callback) {
		super(R.layout.dialog_card_properties);
		editor = callback;
	}
	
	@Override
	protected void onOkClicked(DialogInterface dialog) {
		// Get data from dialog
		Dialog d = (Dialog)dialog;
		EditText frontField = 
				(EditText)d.findViewById(R.id.cardFrontField);
		EditText backField = 
				(EditText)d.findViewById(R.id.cardBackField);
		// Send data to server
		editor.edit(frontField.getText().toString(), backField.getText().toString(), "en");
	}

}
