package opin.preprocessor.entity;

public class DepLine {

	int index;
	String word;
	int head;
	String relation;
	String pos;
	String lemma;
	int spanS;
	int spanE;
	
	
	public final static String SENTENCESTART="<tree>";
	public final static String SENTENCEEND="</tree>";
	public final static String TAB="\t";
	
	public DepLine() {
		
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public void setHead(int head) {
		this.head = head;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}
	public void setSpanS(int spanS) {
		this.spanS = spanS;
	}
	public void setSpanE(int spanE) {
		this.spanE = spanE;
	}
	
	@Override
	public String toString() {	
		if(SENTENCESTART.equals(word) || SENTENCEEND.equals(word)){
			return index+TAB+word+TAB+spanS+TAB+spanE;
		}
		else{
			return index+TAB+word+TAB+head+TAB+relation+TAB+pos+TAB+lemma+TAB+spanS+TAB+spanE;
		}
	}

	public int getIndex() {
		return index;
	}

	public String getWord() {
		return word;
	}

	public int getHead() {
		return head;
	}

	public String getRelation() {
		return relation;
	}

	public String getPos() {
		return pos;
	}

	public String getLemma() {
		return lemma;
	}

	public int getSpanS() {
		return spanS;
	}

	public int getSpanE() {
		return spanE;
	}

	public static String getSENTENCESTART() {
		return SENTENCESTART;
	}

	public static String getSENTENCEEND() {
		return SENTENCEEND;
	}

	public static String getTAB() {
		return TAB;
	}
	
	
}
