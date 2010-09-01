package opin.featurefinder.entity;
import java.util.HashSet;


public class Token implements Comparable<Token>{

	private int spanS;
	private int spanE;
	
	private String word;
	private String lemma;
	private String pos;
	private String mC;
	
	private int headPosition;
	private Token head;
	private String relTypeToHead;
	
	private HashSet<Connection> outgoingConCandidates;
	private HashSet<Connection> incomingConCandidates;
	
	private int position;
		
	public Token(int position,int spanS, int spanE, String word, String lemma, String pos, String mC, int headPosition, String relTypeToHead) {
		this.spanS=spanS;
		this.spanE=spanE;
		this.word=word;
		this.lemma=lemma;
		this.pos=pos;
		this.mC=mC;
		this.headPosition=headPosition;
		this.relTypeToHead=relTypeToHead;
		this.position=position;
	}

	public String getWord() {
		return word;
	}

	public String getLemma() {
		return lemma;
	}

	public String getPos() {
		return pos;
	}

	public String getMC() {
		return mC;
	}

	public Token getHead() {
		return head;
	}

	public String getRelTypeToHead() {
		return relTypeToHead;
	}
	
	public void addOutgoingConCandidates(Connection t) {
		if(outgoingConCandidates==null){
			outgoingConCandidates=new HashSet<Connection>(2);
			outgoingConCandidates.add(t);
		}
		else{
			outgoingConCandidates.add(t);
		}
	}
	
	public void addIncomingConCandidates(Connection t) {
		if(incomingConCandidates==null){
			incomingConCandidates=new HashSet<Connection>(2);
			incomingConCandidates.add(t);
		}
		else{
			incomingConCandidates.add(t);
		}
	}

	public int getPosition() {
		return position;
	}

	public int getSpanS() {
		return spanS;
	}

	public int getSpanE() {
		return spanE;
	}
	
	@Override
	public int compareTo(Token o) {
		return o.spanE-this.spanE;
	}

	public HashSet<Connection> getOutgoingConCandidates() {
		return outgoingConCandidates;
	}

	public HashSet<Connection> getIncomingConCandidates() {
		return incomingConCandidates;
	}

	public void setHead(Token head) {
		this.head = head;
	}

	public int getHeadPosition() {
		return headPosition;
	}

	public void setHeadPosition(int headPosition) {
		this.headPosition = headPosition;
	}
	
	@Override
	public String toString() {
		return word+"-"+lemma+"-"+pos+"-"+mC+" "+position;
	}
	
}
