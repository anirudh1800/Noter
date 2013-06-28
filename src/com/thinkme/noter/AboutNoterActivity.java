package com.thinkme.noter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class AboutNoterActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_about_noter);
		TextView tv=(TextView)findViewById(R.id.about);
		tv.setText("Noter App is a notes taking app developed by budding thinkme developers.\n\nVersion 1.0\n\n\n\nDisclaimer:This app uses icons from http://iconleak.com and http://www.visualpharm.com.");
		tv.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.about_noter, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		finish();		
	}

}
