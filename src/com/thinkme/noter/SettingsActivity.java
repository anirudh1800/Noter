package com.thinkme.noter;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class SettingsActivity extends Activity implements OnItemClickListener, OnClickListener {
	private int searchpos=-1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_settings);
		ListView settings=(ListView)findViewById(R.id.listView1);
	    String values[]={
	    		"Search",
	    		"About this note",
	    		"Controls",
	    		"Updates",
	    		"Noter"
	    	};
	    final ArrayList<String> list=new ArrayList<String>();
	    for(int i=0;i<values.length;i++){
	    	list.add(values[i]);
	    }
	    ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
	    settings.setAdapter(adapter);
	    settings.setOnItemClickListener(this);
	    Button back=(Button)findViewById(R.id.backButton);
	    back.setOnClickListener(this);
	    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
		searchpos=-1;
		Intent i;
		switch(position){
			case 0:i=new Intent(this,SearchActivity.class);
				    startActivityForResult(i,2);
					break;
			case 1:i=getIntent();
		  		    Intent i1=new Intent(this,AboutNoteActivity.class);
		  			i1.putExtra("author",i.getStringExtra("author"));
		  			i1.putExtra("date",i.getStringExtra("date"));
		  			startActivity(i1);
		  			break;
			case 2:i=new Intent(this,ControlsActivity.class);
					startActivity(i);
					break;
			case 3:i=new Intent(this,UpdateActivity.class);
					startActivity(i);
					break;
			case 4:i=new Intent(this,AboutNoterActivity.class);
					startActivity(i);
					break;
			default:break;
			}
	}

	@Override
	public void onClick(View arg0) {
		int id=arg0.getId();
		if(id==R.id.backButton){
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if(requestCode==2){
			searchpos=data.getIntExtra("index",0);
			finish();
		}
	}

	@Override
	public void finish() {
		Intent data=new Intent();
		data.putExtra("index",searchpos);
		setResult(RESULT_OK, data);
		super.finish();
	}
}
