package entity;

public class FeatureVector {
	
	private String dir;
	private String document;
	private int spanS;
	private int spanE;
	private double confidence;

	private int strong;
	private int weak;
	private int patternsubj;
	private int patternobj;
	private int modal;
	private int cardinal;
	private int pronoun;
	private int adjective;
	private int adverb;
	
	private int strongA;
	private int weakA;
	private int patternsubjA;
	private int patternobjA;
	private int modalA;
	private int cardinalA;
	private int pronounA;
	private int adjectiveA;
	private int adverbA;
	
	private String c;
	
	public int getStrong() {
		return strong;
	}
	public void setStrong(int strong) {
		this.strong = strong;
	}
	public int getWeak() {
		return weak;
	}
	public void setWeak(int weak) {
		this.weak = weak;
	}
	public int getPatternsubj() {
		return patternsubj;
	}
	public void setPatternsubj(int patternsubj) {
		this.patternsubj = patternsubj;
	}
	public int getPatternobj() {
		return patternobj;
	}
	public void setPatternobj(int patternobj) {
		this.patternobj = patternobj;
	}
	public int getModal() {
		return modal;
	}
	public void setModal(int modal) {
		this.modal = modal;
	}
	public int getCardinal() {
		return cardinal;
	}
	public void setCardinal(int cardinal) {
		this.cardinal = cardinal;
	}
	public int getPronoun() {
		return pronoun;
	}
	public void setPronoun(int pronoun) {
		this.pronoun = pronoun;
	}
	public int getAdjective() {
		return adjective;
	}
	public void setAdjective(int adjective) {
		this.adjective = adjective;
	}
	public int getAdverb() {
		return adverb;
	}
	public void setAdverb(int adverb) {
		this.adverb = adverb;
	}
	public int getStrongA() {
		return strongA;
	}
	public void setStrongA(int strongA) {
		this.strongA = strongA;
	}
	public int getWeakA() {
		return weakA;
	}
	public void setWeakA(int weakA) {
		this.weakA = weakA;
	}
	public int getPatternsubjA() {
		return patternsubjA;
	}
	public void setPatternsubjA(int patternsubjA) {
		this.patternsubjA = patternsubjA;
	}
	public int getPatternobjA() {
		return patternobjA;
	}
	public void setPatternobjA(int patternobjA) {
		this.patternobjA = patternobjA;
	}
	public int getModalA() {
		return modalA;
	}
	public void setModalA(int modalA) {
		this.modalA = modalA;
	}
	public int getCardinalA() {
		return cardinalA;
	}
	public void setCardinalA(int cardinalA) {
		this.cardinalA = cardinalA;
	}
	public int getPronounA() {
		return pronounA;
	}
	public void setPronounA(int pronounA) {
		this.pronounA = pronounA;
	}
	public int getAdjectiveA() {
		return adjectiveA;
	}
	public void setAdjectiveA(int adjectiveA) {
		this.adjectiveA = adjectiveA;
	}
	public int getAdverbA() {
		return adverbA;
	}
	public void setAdverbA(int adverbA) {
		this.adverbA = adverbA;
	}
	
	public String toArffNominalString(){
		StringBuilder builder= new StringBuilder();	
		builder.append(dir);
		builder.append("|");
		builder.append(document);
		builder.append("|");
		builder.append(spanS);
		builder.append("|");
		builder.append(spanE);
		builder.append(",");
		builder.append(fromNumericToNominal(strong, 2));		
		builder.append(",");
		builder.append(fromNumericToNominal(weak, 2));
		builder.append(",");
		builder.append(fromNumericToNominal(patternsubj, 2));
		builder.append(",");
		builder.append(fromNumericToNominal(patternobj, 2));
		builder.append(",");
		builder.append(fromNumericToNominal(modal, 2));
		builder.append(",");
		builder.append(fromNumericToNominal(cardinal, 2));
		builder.append(",");
		builder.append(fromNumericToNominal(pronoun, 2));
		builder.append(",");
		builder.append(fromNumericToNominal(adjective, 2));
		builder.append(",");
		builder.append(fromNumericToNominal(adverb, 2));
		builder.append(",");
		builder.append(fromNumericToNominal(strongA, 2));		
		builder.append(",");
		builder.append(fromNumericToNominal(weakA, 2));
		builder.append(",");
		builder.append(fromNumericToNominal(patternsubjA, 2));
		builder.append(",");
		builder.append(fromNumericToNominal(patternobjA, 2));
		builder.append(",");
		builder.append(fromNumericToNominal(modalA, 2));
		builder.append(",");
		builder.append(fromNumericToNominal(cardinalA, 2));
		builder.append(",");
		builder.append(fromNumericToNominal(pronounA, 2));
		builder.append(",");
		builder.append(fromNumericToNominal(adjectiveA, 2));
		builder.append(",");
		builder.append(fromNumericToNominal(adverbA, 2));
		builder.append(",");
		builder.append(c);
		return builder.toString();
	}
	
	public String toArffNumericString(){
		StringBuilder builder= new StringBuilder();
		builder.append(dir);
		builder.append("|");
		builder.append(document);
		builder.append("|");
		builder.append(spanS);
		builder.append("|");
		builder.append(spanE);
		builder.append(",");
		builder.append(strong);
		builder.append(",");
		builder.append(weak);
		builder.append(",");
		builder.append(patternsubj);
		builder.append(",");
		builder.append(patternobj);
		builder.append(",");
		builder.append(modal);
		builder.append(",");
		builder.append(cardinal);
		builder.append(",");
		builder.append(pronoun);
		builder.append(",");
		builder.append(adjective);
		builder.append(",");
		builder.append(adverb);
		builder.append(",");
		builder.append(strongA);
		builder.append(",");
		builder.append(weakA);
		builder.append(",");
		builder.append(patternsubjA);
		builder.append(",");
		builder.append(patternobjA);
		builder.append(",");
		builder.append(modalA);
		builder.append(",");
		builder.append(cardinalA);
		builder.append(",");
		builder.append(pronounA);
		builder.append(",");
		builder.append(adjectiveA);
		builder.append(",");
		builder.append(adverbA);
		builder.append(",");
		builder.append(c);
		return builder.toString();
	}

	public String toSparseNumericString(){
		StringBuilder builder= new StringBuilder();
		builder.append(c.equals("subj") ? "1 " : "-1 ");
		builder.append(strong>0 ? "1:"+strong+" " : "");
		builder.append(weak>0 ? "2:"+weak+" " : "");
		builder.append(patternsubj>0 ? "3:"+patternsubj+" " : "");
		builder.append(patternobj>0 ? "4:"+patternobj+" " : "");
		builder.append(modal>0 ? "5:"+modal+" " : "");
		builder.append(cardinal>0 ? "6:"+cardinal+" " : "");
		builder.append(pronoun>0 ? "7:"+pronoun+" " : "");
		builder.append(adjective>0 ? "8:"+adjective+" " : "");
		builder.append(adverb>0 ? "9:"+adverb+" " : "");
		builder.append(strong>0 ? "10:"+strongA+" " : "");
		builder.append(weak>0 ? "11:"+weakA+" " : "");
		builder.append(patternsubj>0 ? "12:"+patternsubjA+" " : "");
		builder.append(patternobj>0 ? "13:"+patternobjA+" " : "");
		builder.append(modal>0 ? "14:"+modalA+" " : "");
		builder.append(cardinal>0 ? "15:"+cardinalA+" " : "");
		builder.append(pronoun>0 ? "16:"+pronounA+" " : "");
		builder.append(adjective>0 ? "17:"+adjectiveA+" " : "");
		builder.append(adverb>0 ? "18:"+adverbA+" " : "");
		return builder.toString();
	}
	
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
	
	private String fromNumericToNominal(int i, int threshold){
		if(i<threshold){
			return "="+i;
		}
		else{
			return ">="+threshold;
		}
		
	}
	public String getDocument() {
		return document;
	}
	public void setDocument(String document) {
		this.document = document;
	}
	public int getSpanS() {
		return spanS;
	}
	public void setSpanS(int spanS) {
		this.spanS = spanS;
	}
	public int getSpanE() {
		return spanE;
	}
	public void setSpanE(int spanE) {
		this.spanE = spanE;
	}
	public double getConfidence() {
		return confidence;
	}
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	
	
	
}
