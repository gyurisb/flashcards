package com.example.wordcardapp.utils;

import android.text.TextUtils;

public class Validator {

	public final static boolean isValidEmail(CharSequence target) {
		if (TextUtils.isEmpty(target)) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
		}
	}
}
