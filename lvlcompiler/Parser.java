

class Parser {
	private Lexer lexer;
	
	public static LevelTree parse(String src) throws ParserException {
		Parser parser = new Parser(src);
		LevelTree levelTree = parser.parseTerrain();
		if (parser.lexer.hasNext())
			throw new ParserException("Syntax error: Not ended");
		return levelTree;
	}
	
	private Parser(String src) {
		lexer = new Lexer(src);
	}
	
	private LevelTree parseTerrain() throws ParserException {
		try {
			if (lexer.tokenizeKeyWord().equals("define-terrain"))
			{
				LevelTree levelTree = new LevelTree(lexer.tokenizeInteger(),
					lexer.tokenizeInteger(), lexer.tokenizeInteger(), lexer.tokenizeInteger(), lexer.tokenizeFlags(),
					lexer.tokenizeInteger(), lexer.tokenizeInteger());
				while (lexer.isKeyWord())
				{
					String ss = lexer.tokenizeKeyWord();
					if (ss.equals("swall"))
						levelTree.add(0, parseSwall());
					else if (ss.equals("dwall"))
						levelTree.add(1, parseDwall());
					else if (ss.equals("enemy"))
						levelTree.add(2, parseEnemy());
					else if (ss.equals("item"))
						levelTree.add(3, parseItem());
				}
				return levelTree;
			} else
				throw new ParserException("Terrain syntax error");
		} catch (Exception e) {
			throw new ParserException("Terrain syntax error", e);
		}
	}
	
	private Swall parseSwall() throws ParserException {
		try {
			return new Swall(lexer.tokenizeInteger(),
				lexer.tokenizeInteger(), lexer.tokenizeInteger(), lexer.tokenizeInteger(),
				(byte)lexer.tokenizeFlags(), lexer.tokenizeInteger());
		} catch (Exception e) {
			throw new ParserException("Static wall syntax error", e);
		}
	}
	
	private Dwall parseDwall() throws ParserException {
		try {
			return new Dwall(lexer.tokenizeInteger(),
				lexer.tokenizeInteger(), lexer.tokenizeInteger(), lexer.tokenizeInteger(),
				(byte)lexer.tokenizeFlags(), lexer.tokenizeInteger(), (byte)lexer.tokenizeFlags(),
				lexer.tokenizeInteger(), lexer.tokenizeInteger(), lexer.tokenizeInteger(), lexer.tokenizeInteger());
		} catch (Exception e) {
			throw new ParserException("Dynamic wall syntax error", e);
		}
	}
	
	private Enemy parseEnemy() throws ParserException {
		try {
			return new Enemy(lexer.tokenizeInteger(), lexer.tokenizeInteger(), lexer.tokenizeInteger());
		} catch (Exception e) {
			throw new ParserException("Enemy syntax error", e);
		}
	}
	
	private Item parseItem() throws ParserException {
		try {
			return new Item(lexer.tokenizeInteger(), lexer.tokenizeInteger(), lexer.tokenizeInteger(),
				lexer.tokenizeInteger(), lexer.tokenizeInteger(), lexer.tokenizeInteger());
		} catch (Exception e) {
			throw new ParserException("Item syntax error", e);
		}
	}
}