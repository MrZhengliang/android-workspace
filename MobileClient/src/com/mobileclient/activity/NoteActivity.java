/**
 * 
 */
package com.mobileclient.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
 * 
 * @author fuzl
 *
 */
public class NoteActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("欢迎访问H5 UI界面");
		setContentView(R.layout.note_main);
	}
}
