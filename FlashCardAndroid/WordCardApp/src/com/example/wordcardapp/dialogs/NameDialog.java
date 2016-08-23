package com.example.wordcardapp.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.widget.EditText;

import com.example.wordcardapp.R;

public class NameDialog extends AbstractDialog {

	private NameCallback nameCallback;
	
	public NameDialog(NameCallback callback) {
		super(R.layout.dialog_name);
		nameCallback = callback;
	}

	@Override
	protected void onOkClicked(DialogInterface dialog) {
		// Get data from dialog
		Dialog d = (Dialog)dialog;
		EditText inputField = 
				(EditText)d.findViewById(R.id.nameField);
		// Send data to server
		nameCallback.callback(inputField.getText().toString());
	}

}
