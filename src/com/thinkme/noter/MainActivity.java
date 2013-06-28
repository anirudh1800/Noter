package com.thinkme.noter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;
import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity implements OnGesturePerformedListener, OnClickListener, TextWatcher, OnMenuItemClickListener{
	
	private GestureLibrary gestureLib=null;
	private NoteArray na=null;
	private Note n=null;
	// private String selection=null; for future implementations of custom action mode in edit text
    private AccountManager am;
    private Account[] accounts;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    gestureLib = GestureLibraries.fromRawResource(this, R.raw.gestures);
	    if (!gestureLib.load()) {
	      finish();
	    }
	    GestureOverlayView gestureOverlayView=new GestureOverlayView(this);
	    View inflate=getLayoutInflater().inflate(R.layout.activity_main,null);
	    gestureOverlayView.addView(inflate);
	    setContentView(gestureOverlayView);
	    gestureOverlayView.addOnGesturePerformedListener(this);
		read();
		if(na.size()==0){
			n=new Note();
			na.addNote(n);
			setNotesToActivity();
		}
		else{
			n=na.note();
			setNotesToActivity();
		}
		ImageButton saveButton=(ImageButton)findViewById(R.id.saveButton);
		ImageButton settingsButton=(ImageButton)findViewById(R.id.searchButton);
		TextView title=(TextView)findViewById(R.id.editText2);
		TextView contents=(TextView)findViewById(R.id.editText1);
		saveButton.setOnClickListener(this);
		settingsButton.setOnClickListener(this);
		title.addTextChangedListener(this);
		contents.addTextChangedListener(this);
		am = AccountManager.get(this);
		accounts = am.getAccountsByType("com.google");
	}

	@Override
	protected void onDestroy() {
		System.gc();
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem md=menu.findItem(R.id.menu_delete);
		MenuItem me=menu.findItem(R.id.menu_exit);
		md.setOnMenuItemClickListener(this);
		me.setOnMenuItemClickListener(this);
		return true;
	}
	
	@Override
	public void onGesturePerformed(GestureOverlayView arg0, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLib.recognize(gesture);
		Prediction prediction=predictions.get(0);
		if (prediction.score> 5.0) {
		         if(prediction.name.equals("next")){
		        	 if(na.cur()==na.size()){
		        		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        		builder.setMessage("Wanna create new note?");
		        		builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
		        		           public void onClick(DialogInterface dialog, int id) {
		        		        	   n=new Note();
		        		        	   na.addNote(n);
		        		        	   setNotesToActivity();
		        		           }
		        		       });
		        		builder.setNegativeButton("no",new DialogInterface.OnClickListener(){
		        					public void onClick(DialogInterface dialog,int id){
		        							
		        					}
		        	   
		        				});
		        		builder.show();
		        	 }
		        	 else{
		        		na.next();
		        		n=na.note();
		        		setNotesToActivity();
		        	 }
		         }
		         else if(prediction.name.equals("prev")){ 
		        	 na.prev();
		        	 n=na.note();
		        	 setNotesToActivity();
			     }
		         else if(prediction.name.equals("menu")){
		        	 openOptionsMenu ();
		         }
		}
	}
	
	public void read(){
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
		if(na==null)
			na=new NoteArray();
	}
	
	public void save() {
		try{
			int i=0;
			if(accounts.length>1){
				final CharSequence[] items=new CharSequence[accounts.length];
				for(Account ac:accounts){
					items[i++]=ac.name;
				}
				AlertDialog.Builder alert = new AlertDialog.Builder(this);
				alert.setTitle("Select Author");
				alert.setSingleChoiceItems(items,-1,new DialogInterface.OnClickListener(){
				@Override
					public void onClick(DialogInterface dialog, int which) {
						if(which!=-1){
							n.setAuthor((String) items[which]);
						}
					}
					});
			}
			else if(accounts.length==1){
				n.setAuthor(accounts[0].name);
			}
			else	n.setAuthor("default");
			FileOutputStream fos=openFileOutput("appdata.noter",Context.MODE_PRIVATE);
			ObjectOutputStream oos=new ObjectOutputStream(fos);
			oos.writeObject(na);
			TextView serial=(TextView)findViewById(R.id.editText3);
			serial.setText(na.cur()+"/"+na.size());
			Toast.makeText(this,"note saved",Toast.LENGTH_SHORT).show();
			fos.close();
			oos.close();
			System.gc();
		}catch(IOException io){
			Log.e(ACTIVITY_SERVICE, "IOException");
		}
	}
	
	public void setNotesToActivity(){
		Map<String, String> s=n.getDetails();
        TextView contents=(TextView)findViewById(R.id.editText1);
        TextView  title=(TextView)findViewById(R.id.editText2);
        TextView serial=(TextView)findViewById(R.id.editText3);
		contents.setText(s.get("text"));
		title.setText(s.get("title"));
		serial.setText(na.cur()+"/"+na.size());
	}
	
	public void getNotesFromActivity(){
		TextView contents=(TextView)findViewById(R.id.editText1);
	    TextView  title=(TextView)findViewById(R.id.editText2);
	    if(title.getText().length()==0){
	    	n.setTitle("note "+na.cur());
	    }else{
	    	n.setTitle(title.getText().toString());
	    }
		n.setText(contents.getText().toString());
	}

	@Override
	public void onClick(View v1) {
		int id=v1.getId();
		switch(id){
		
		case R.id.saveButton:	getNotesFromActivity();
								save();break;
		case R.id.searchButton: Intent i=new Intent(this,SettingsActivity.class);
								Map<String,String> mp=n.getDetails();
								i.putExtra("date",mp.get("createDate"));
								i.putExtra("author",mp.get("author"));
								startActivityForResult(i,1);
								break;
		default:break;
		
		}
	}
	
	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		if(requestCode==1){
			int pos=data.getIntExtra("index", -1);
			if(pos>=0){
				pos++;
				n=na.getNote(pos);
				setNotesToActivity();
			}
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		TextView serial=(TextView)findViewById(R.id.editText3);
		serial.setText(na.cur()+"*/"+na.size());
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		if(item.getTitle().equals("Exit")){
			finish();
		}
		else if(item.getTitle().equals("Delete")){
			switch(na.size()){
				case 0:n=new Note();
				        na.addNote(n);
						setNotesToActivity();
						break;
				case 1:na.remove();
						save();
						n=new Note();
				   		na.addNote(n);
				   		setNotesToActivity();
				   		break;
				default:na.remove();
						n=na.note();
						setNotesToActivity();
						save();
						break;
			}
		}
		/* Future implementations of custom action mode for edit text
		else if(item.getTitle().equals("Copy")){
			EditText contents=(EditText)findViewById(R.id.editText1);
			int start=contents.getSelectionStart();
			int end=contents.getSelectionEnd();
			selection=new String(contents.getEditableText().toString().substring(start, end));
		}
		else if(item.getTitle().equals("Paste")){
			EditText contents=(EditText)findViewById(R.id.editText1);
			int start=contents.getSelectionStart();
			int end=contents.getSelectionEnd();
			if(selection!=null){
				contents.append(selection, start, end);
			}
		}
		*/
		return false;
	}
}
