package opin.preprocessor.entity;

public class SentenceLine {

	public final static String COMMA=",";
	
	int id;
	
	int spanS;
	int spanE;
	
	
	
	@Override
	public String toString() {
		return spanS+COMMA+spanE;
	}



	public void setId(int id) {
		this.id = id;
	}



	public void setSpanS(int spanS) {
		this.spanS = spanS;
	}



	public void setSpanE(int spanE) {
		this.spanE = spanE;
	}
	
	
	
}
