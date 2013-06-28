package com.thinkme.noter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity implements OnClickListener, OnItemClickListener {
    private String searchString=null;
    private NoteArray na=null;
    private Note n=null;
    private String[] items;
    private int[] itempos;
    private int selectpos=-1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		Button go=(Button)findViewById(R.id.go);
		go.setOnClickListener(this);
		Button finish=(Button)findViewById(R.id.finish);
		finish.setOnClickListener(this);
		ListView lv=(ListView)findViewById(R.id.noteSearchList);
		lv.setOnItemClickListener(this);	
		try{
			FileInputStream fis=openFileInput("appdata.noter");
			ObjectInputStream ois=new ObjectInputStream(fis);
			na=(NoteArray)ois.readObject();
			fis.close();
			ois.close();
		}catch(FileNotFoundException fnf){
			Log.e(ACTIVITY_SERVICE, "FileNotFoundException");
		}catch(ClassNotFoundException cnf){
			Log.e(ACTIVITY_SERVICE, "ClassNotFoundException");
			
		}catch(IOException io){
			Log.e(ACTIVITY_SERVICE, "IOException");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public void onClick(View v1) {
		int id=v1.getId();
		selectpos=-1;
		switch(id){
			case R.id.go:
				int i,j;
				EditText searchField=(EditText)findViewById(R.id.searchField);
				searchString=new String(searchField.getText().toString());
				if(searchString.length()==0)
					break;
				int size=na.size();
				itempos=new int[size];
				for(i=0,j=0;i<size;i++){
						n=na.get(i);
						if(n.getTitle().contains(searchString))
							itempos[j++]=i;
				}
				if(j==0){
					Toast.makeText(this,"'0' items found",Toast.LENGTH_SHORT).show();
					break;
				}
				items=new String[j];
				for(i=0;i<j;i++){
					n=na.get(itempos[i]);
					items[i]=new String(n.getTitle());
				}
				ListView lv=(ListView)findViewById(R.id.noteSearchList);
				MyAdapter myAdapter=new MyAdapter(this,items);
				myAdapter.setSearchString(searchString);
				lv.setAdapter(myAdapter);
				break;
			case R.id.finish:
				finish();
				break;
			default:
				break;
		}
	}		
	
	@Override
	protected void onDestroy() {
		System.gc();
		super.onDestroy();
	}

	public class MyAdapter extends  ArrayAdapter<String>{
		private final Context context;
		private final String[] values;
		private String searchString=null;
		
		public MyAdapter(Context context, String[] values) {
		    super(context, R.layout.row_layout, values);
		    this.context = context;
		    this.values = values;
		  }
		public void setSearchString(String search){
			searchString=new String(search);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
		   LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		   View rowView = inflater.inflate(R.layout.row_layout, parent, false);
		   TextView textView = (TextView) rowView.findViewById(R.id.listitem);
		   int start=values[position].indexOf(searchString);
		   Spannable spannedValue=new SpannableString(values[position]);
		   spannedValue.setSpan(new ForegroundColorSpan(Color.BLUE),start,searchString.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
		   textView.setText(spannedValue);
		   return rowView;
		}
	}

	@Override
	public void finish() {
		Intent data = new Intent();
		data.putExtra("index",selectpos);
		setResult(RESULT_OK, data);
		super.finish();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		selectpos=itempos[arg2];
		finish();
	}
}
