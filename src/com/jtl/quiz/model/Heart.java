package com.jtl.quiz.model;

import java.io.Serializable;

public class Heart implements Serializable{
	
	private static final long serialVersionUID = 3602045431438889907L;
	
	//Graphics
	public static final int STATIC_OFFSET_X = 100;
	public static final int STATIC_OFFSET_Y = -10;
	public static final int OFFSET_X = 50;
	public static final int SIZE_X = 20;
	public static final int SIZE_Y = 20;
	
	public float health;
	public boolean isArmored;

}
