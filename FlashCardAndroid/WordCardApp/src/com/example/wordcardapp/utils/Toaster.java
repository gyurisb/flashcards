package com.example.wordcardapp.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class Toaster {

	private static void appendLog(String level, String text)
	{
	    File logFile = new File("FlashCardAppLog.txt");
	    if (!logFile.exists())
	    {
	        try
	        {
	            logFile.createNewFile();
	        } catch (IOException e)
	        {
	            e.printStackTrace();
	        }
	    }
	    try
	    {
	        // BufferedWriter for performance, true to set append to file flag
	        BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
	        buf.append(level + ": " + text);
	        buf.newLine();
	        buf.close();
	    } catch (IOException e)
	    {
	        e.printStackTrace();
	    }
	}
	
	private static void logAndToast(Context ctx, String level, String message) {
		Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
		appendLog(level, message);
	}
	
	public static void info(Context ctx, String message) {
		logAndToast(ctx, "info", message);
	}
	
	public static void error(Context ctx, String message) {
		logAndToast(ctx, "error", message);
	}
	
	public static void toastFromUIThread(final Activity parent, final String what){
		parent.runOnUiThread(new Runnable(){
			public void run(){
				Toast.makeText(parent, what, Toast.LENGTH_LONG).show();
			}
		});
	}
	
}
