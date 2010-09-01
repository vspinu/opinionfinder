package opin.preprocessor.entity;

public class GateDefaultLine {

	public final static String COMMA=",";
	public final static String STRING="string";
	public final static String GATETOKEN="GATE_TOKEN";
	public final static String GATESENTENCE="GATE_SENTENCE";
	public final static String CATEGORY="category";
	public final static String KIND="kind";
	public final static String WORD="word";
	public final static String PUNCTUATION="punctuation";
	public final static String LOWER="lowercase";
	public final static String UPPER="upperInitial";
	public final static String EQUAL="=\"";
	public final static String QUOTA="\"";

	int id;

	int spanS;
	int spanE;

	String category;
	String string;

	int length;
	String kind;
	String orth;




	public void setId(int id) {
		this.id = id;
	}

	public void setSpanS(int spanS) {
		this.spanS = spanS;
	}

	public void setSpanE(int spanE) {
		this.spanE = spanE;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setString(String string) {
		this.string = string;
	}

}