package ru.wendex.cp.lvlcompiler;


class ParserException extends Exception {
	public ParserException(String message) {
		super(message);
	}
	
	public ParserException(String message, Throwable t) {
		super(message, t);
	}
}