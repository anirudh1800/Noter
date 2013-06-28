package com.thinkme.noter;

import java.util.ArrayList;

public class NoteArray extends ArrayList<Note> {

	private static final long serialVersionUID = 4637453506735927218L;
	private int cur=0;
	
	NoteArray(){
	}
	
	public int cur(){
		return cur;
	}
	
	public int prev(){
		cur-=1;
		if(cur==0)
			cur=1;
		return cur;
	}
	
	public int next(){
		cur+=1;
		if(cur>size())
			cur=1;
		return cur;
	}
	
	public Note note(){
			if(cur==0)
				return null;
			return get(cur-1);
	}
	
	public boolean addNote(Note notetoadd){
		cur+=1;
		return add(notetoadd);	
	}
	
	public Note getNote(int index){
		if(index<=size()){
			cur=index;
			return get(cur-1);
		}
		return null;
	}
	
	public Note remove(){
		if(cur==0)
			return null;
		Note removed=remove(cur-1);
		if(cur>size()){
			cur-=1;
		}
		return removed;
	}
	
}
