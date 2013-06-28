package com.thinkme.noter;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AboutNoteActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about_note);
		LinearLayout ll=(LinearLayout)findViewById(R.id.LinearLayout1);
		ll.setOnClickListener(this);
		Intent i=getIntent();
		String author=new String(i.getStringExtra("author"));
		String date=new String(i.getStringExtra("date"));
		TextView authorField=(TextView)findViewById(R.id.authorField);
		TextView dateField=(TextView)findViewById(R.id.dateField);
		authorField.setText(author);
		dateField.setText(date);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.about_note, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		finish();
	}

}
