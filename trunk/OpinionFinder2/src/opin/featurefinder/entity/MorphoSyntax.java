package opin.featurefinder.entity;

import java.util.ArrayList;

public class MorphoSyntax {
	private String word;
	private String lemma;
	private String pos;
	private String mC;
	
	private ArrayList<String> wordlemma;
	private ArrayList<String> wordposmc;
	
	
	private boolean wordForce;
	private boolean lemmaForce;
	private boolean posForce;
	private boolean mCForce;
	
	public MorphoSyntax() {

	}
	
	public MorphoSyntax(String word, String lemma, String pos, String mC) {
			
		this.word=word;
		this.lemma=lemma;
		this.pos=pos;
		this.mC=mC; 

		this.wordForce=true;
		this.lemmaForce=true;
		this.posForce=true;
		this.mCForce=true;
		
	}

	public boolean isWordForce() {
		return wordForce;
	}

	public void setWordForce(boolean wordForce) {
		this.wordForce = wordForce;
	}

	public boolean isLemmaForce() {
		return lemmaForce;
	}

	public void setLemmaForce(boolean lemmaForce) {
		this.lemmaForce = lemmaForce;
	}

	public boolean isPosForce() {
		return posForce;
	}

	public void setPosForce(boolean posForce) {
		this.posForce = posForce;
	}

	public boolean isMCForce() {
		return mCForce;
	}

	public void setMCForce(boolean force) {
		mCForce = force;
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

	public void setWord(String word) {
		this.word = word;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public void setMC(String mc) {
		mC = mc;
	}
	
	@Override
	public String toString() {
		return word+"-"+lemma+"-"+pos+"-"+mC;
	}

	public ArrayList<String> getWordlemma() {
		return wordlemma;
	}

	public void setWordlemma(ArrayList<String> wordlemma) {
		this.wordlemma = wordlemma;
	}

	public ArrayList<String> getWordposmc() {
		return wordposmc;
	}

	public void setWordposmc(ArrayList<String> wordposmc) {
		this.wordposmc = wordposmc;
	}
	
	
}
