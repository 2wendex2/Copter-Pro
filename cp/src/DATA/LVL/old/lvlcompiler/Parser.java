

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
					lexer.tokenizeInteger(), lexer.tokenizeInteger(), lexer.tokenizeInteger());
				while (lexer.isKeyWord())
				{
					String ss = lexer.tokenizeKeyWord();
					if (ss.equals("swall"))
						levelTree.add(0, parseSwall());
					else if (ss.equals("swall-v"))
						levelTree.add(1, parseSwallV());
					else if (ss.equals("swall-h"))
						levelTree.add(2, parseSwallH());
					else if (ss.equals("sbwall"))
						levelTree.add(3, parseSBwall());
				}
				return levelTree;
			} else
				throw new ParserException("Terrain syntax error");
		} catch (Exception e) {
			throw new ParserException("Terrain syntax error", e);
		}
	}
	
	private RectLeaf parseSwall() throws ParserException {
		try {
			return new RectLeaf(lexer.tokenizeInteger(),
				lexer.tokenizeInteger(), lexer.tokenizeInteger(), lexer.tokenizeInteger());
		} catch (Exception e) {
			throw new ParserException("Stone wall syntax error", e);
		}
	}
	
	private RectLeaf parseSBwall() throws ParserException {
		try {
			return new RectLeaf(lexer.tokenizeInteger(),
				lexer.tokenizeInteger(), lexer.tokenizeInteger(), lexer.tokenizeInteger());
		} catch (Exception e) {
			throw new ParserException("Stone broken wall syntax error", e);
		}
	}
	
	private VectorRectLeaf parseSwallV() throws ParserException {
		try {
			return new VectorRectLeaf(lexer.tokenizeInteger(), lexer.tokenizeInteger(), lexer.tokenizeInteger(),
				lexer.tokenizeInteger(), lexer.tokenizeInteger());
		} catch (Exception e) {
			throw new ParserException("Stone wall vertical syntax error", e);
		}
	}
	
	private VectorRectLeaf parseSwallH() throws ParserException {
		try {
			return new VectorRectLeaf(lexer.tokenizeInteger(), lexer.tokenizeInteger(), lexer.tokenizeInteger(),
				lexer.tokenizeInteger(), lexer.tokenizeInteger());
		} catch (Exception e) {
			throw new ParserException("Stone wall horisontal syntax error", e);
		}
	}
}