package com.thinkme.noter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Note implements Serializable{

	private static final long serialVersionUID = 5827900854403039672L;
		
	private String title;
	private String text;
	private Date createDate;
	private String author;
	
	public Note(){
		title=new String("");
		text=new String("");
		createDate=new Date();
		author=new String("");
	}
	
	public void setTitle(String str){
		title=new String(str);
	}
	
	public String getTitle(){
		return title;
	}
	
	public void setText(String str){
		text=new String(str);
	}
	
	public void setAuthor(String str){
		author=new String(str);
	}	
	
	public Map<String,String> getDetails(){
		Map<String,String> notemap = new HashMap<String,String>();
		notemap.put("title", title);
		notemap.put("text", text);
		notemap.put("createDate", createDate.toString());
		notemap.put("author", author);
		return notemap;
	}
}
