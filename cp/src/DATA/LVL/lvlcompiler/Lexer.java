class Lexer {
	private String src;
	private int i;

	public Lexer(String str) {
		src = str;
	}
	
	public boolean hasNext() {
		while (i < src.length()) {
			while (src.charAt(i) == ';')
				while (!(src.charAt(i) == '\n' || src.charAt(i) == '\r'))
					i++;
			if (!(src.charAt(i) == ' ' || src.charAt(i) == '\t' || src.charAt(i) == '\r' ||
				src.charAt(i) == '\n' || src.charAt(i) == '\u000B' || src.charAt(i) == '\f'))
				break;
			
			i++;
		}
		return i < src.length();
	}
	
	public boolean isInteger() {
		return hasNext() && (Character.isDigit(src.charAt(i)) || src.charAt(i) == '-');
	}
	
	public boolean isFlags() {
		return hasNext() && Character.isDigit(src.charAt(i)) && Character.digit(src.charAt(i), 2) >= 0;
	}
	
	public boolean isKeyWord() {
		return hasNext() && src.charAt(i) == '%';
	}
	
	public int tokenizeInteger() throws LexerException {
		if (isInteger()) {
			int res;
			boolean neg = false;
			if (src.charAt(i) == '-') {
				neg = true;
				i++;
			}
			res = Character.digit(src.charAt(i++), 10);
			while (i < src.length() && Character.isDigit(src.charAt(i))) 
				res = res * 10 + Character.digit(src.charAt(i++), 10);
			if (i < src.length() && src.charAt(i) == 't') {
				i++;
				res *= 32;
			}
			return neg ? -res : res;
		} else
			throw new LexerException("Token is not integer");
	}
	
	public long tokenizeFlags() throws LexerException {
		if (isFlags()) {
			long res;
			res = Character.digit(src.charAt(i++), 2);
			while (i < src.length() && Character.isDigit(src.charAt(i))) 
				res = res * 2 + Character.digit(src.charAt(i++), 2);
			return res;
		} else
			throw new LexerException("Token is not flags");
	}
	
	public String tokenizeKeyWord() throws LexerException {
		if (isKeyWord()) {
			i++;
			StringBuilder newStr = new StringBuilder();
			while (i < src.length() && (Character.isLetterOrDigit(src.charAt(i)) || src.charAt(i) == '-'))
				newStr.append(src.charAt(i++));
			return newStr.toString();
		} else
			throw new LexerException("Token is not keyword");
	}
}