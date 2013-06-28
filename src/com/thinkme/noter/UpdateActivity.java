package com.thinkme.noter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class UpdateActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_update);
		TextView tv=(TextView)findViewById(R.id.update);
		tv.setText("Thanks for using app!.\nUpdates can be installed from Google Play store by reinstalling the app.\nWe value your feedback");
		tv.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.update, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		finish();		
	}

}
