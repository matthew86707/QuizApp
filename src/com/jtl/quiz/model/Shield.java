package com.jtl.quiz.model;

public class Shield extends Heart {
	private static final long serialVersionUID = 571406169560173770L;
	
	public Shield() {
		super(0);
	}

	@Override
	public String getResource() {
		return "shield";
	}

	@Override
	public int getValue() {
		return 0;
	}
}
