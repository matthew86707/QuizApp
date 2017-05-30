package com.jtl.quiz.model;

public class Option {
	
	//Graphics
	public static final int STATIC_OFFSET_X = 150;
	public static final int OFFSET_X = 200;
	public static final int STATIC_Y = 500;
	public static final int SIZE_Y = 30;
	
	public float chance;
	public int deltaHearts;
	public String text;
	
	public Option(int dealtaHearts, float chance, String text){
		this.deltaHearts = dealtaHearts;
		this.chance = chance;
		this.text = text;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    Option toComp = (Option) obj;
	    if (this.chance != toComp.chance) {
	        return false;
	    }
	    if (!(this.text.equals(toComp.text))) {
	        return false;
	    }
	    if (this.deltaHearts != toComp.deltaHearts) {
	        return false;
	    }
	    return true;
	}

}
