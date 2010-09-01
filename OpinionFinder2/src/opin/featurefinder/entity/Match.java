package opin.featurefinder.entity;


public class Match {

	private final static String MATCHFEAT="matchfeat";
	private final static String STRING="string";
	private final static String SID="sid";
	private final static String MATCH="match";
	private final static String TYPE="type";
	private final static String PRIORPOLARITY="priorpolarity";
	private final static String COMMA=",";
	private final static String PATTERN="pattern";
	private final static String POSITION="position";
	private final static String ID="ID";
	private final static String TAB="\t";
	private final static String EQUALS="=\"";
	private final static String EQUALE="\"";
	private final static String SPACE=" ";
	
	private Token[] parts;
	private Entry pattern;
	private Sentence sentence;
	private int matchS;
	private int matchE;
	private int id; 
	private int position;
	
	
	
	public Match(int id, Token[] parts, Entry pattern, Sentence sentence) {
		this.id=id;
		this.parts=new Token[parts.length];
		for(int i=0;i<parts.length;i++){
			this.parts[i]=parts[i];
		}
		this.pattern=pattern;
		this.sentence=sentence;
		int i=0;
		while(parts[i]==null){
			i++;
		}
		matchS=parts[i].getSpanS();
		i=parts.length-1;
		while(parts[i]==null){
			i--;
		}
		matchE=parts[i].getSpanE();
	}
	
	public Match(int id, Token part, Entry pattern, Sentence sentence) {
		this.id=id;
		parts=new Token[1];
		parts[0]=part;
		this.pattern=pattern;
		this.sentence=sentence;		
		matchS=part.getSpanS();
		matchE=part.getSpanE();
		position=part.getPosition();
	}
		
	public Token[] getParts() {
		return parts;
	}

	public int getMatchS() {
		return matchS;
	}

	public int getMatchE() {
		return matchE;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
		builder.append(ID);
		builder.append(id);
		builder.append(TAB);
		builder.append(matchS);
		builder.append(COMMA);
		builder.append(matchE);
		builder.append(TAB);
		builder.append(STRING);
		builder.append(TAB);
		builder.append(MATCHFEAT);
		
		builder.append(TAB);
		builder.append(PATTERN);
		builder.append(EQUALS);
		builder.append(pattern.getId());
		builder.append(EQUALE);

		builder.append(TAB);
		builder.append(SID);
		builder.append(EQUALS);
		builder.append(sentence.getId());
		builder.append(EQUALE);
		
		builder.append(TAB);
		builder.append(MATCH);
		builder.append(EQUALS);
		for (int i=0;i<parts.length; i++) {
			if(parts[i]!=null){
				builder.append(parts[i].getWord());
				builder.append(SPACE);
			}
		}
		builder.append(EQUALE);
		
		builder.append(TAB);
		builder.append(POSITION);
		builder.append(EQUALS);
		builder.append(position);
		builder.append(EQUALE);
		
		for(String k : pattern.getAttributes().keySet()){
			builder.append(TAB);
			builder.append(k);
			builder.append(EQUALS);
			builder.append(pattern.getAttributes().get(k));
			builder.append(EQUALE);
		}

		return builder.toString();
	}
}
