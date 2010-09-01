package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeMap;

public class Sentence implements Serializable{

	private int spanS;
	private int spanE;
	private int strongCount;
	private int weakCount;
	private int subjPatternCount;
	private int objPatternCount;
	private String hpC;
	private String goldC;
	private int senID;
	private int docID;
	private HashMap<String,ArrayList<Annotation>> manAnns;
	private HashMap<String,ArrayList<Annotation>> autoAnns;
	private HashMap<String,ArrayList<Annotation>> gatedefaultAnns;
	
	
	
	private TreeMap<Span,LinkedList<String>> clues;	
	private HashSet<Span> modal;
	private HashSet<Span> adverb;
	private HashSet<Span> cardinal;
	private HashSet<Span> pronoun;
	private HashSet<Span> adjective;
	
	
	
	public Sentence(int spanS, int spanE,int senID, int docID) {
		this.manAnns= new HashMap<String, ArrayList<Annotation>>(6);
		this.autoAnns=new HashMap<String, ArrayList<Annotation>>(6);
		this.gatedefaultAnns=new HashMap<String, ArrayList<Annotation>>(80);
		this.clues= new TreeMap<Span,LinkedList<String>>();
		this.docID=docID;
		this.senID=senID;
		this.spanS=spanS;
		this.spanE=spanE;
		this.strongCount=0;
		this.weakCount=0;
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

	public String getHpC() {
		return hpC;
	}


	public void setHpC(String hpC) {
		this.hpC = hpC;
	}


	public String getGoldC() {
		return goldC;
	}

	public void setGoldC(String goldC) {
		this.goldC = goldC;
	}

	public HashMap<String, ArrayList<Annotation>> getManAnns() {
		return manAnns;
	}

	public ArrayList<Annotation> getManAnnsForKey(String key) {
		return manAnns.get(key);
	}

	public void addToManAnns(String key,Annotation value) {
		if(manAnns.containsKey(key)){
			manAnns.get(key).add(value);
		}
		else{
			ArrayList<Annotation> valueList=new ArrayList<Annotation>(6);
			valueList.add(value);
			manAnns.put(key,valueList);
		}
	}


	public HashMap<String, ArrayList<Annotation>> getAutoAnns() {
		return autoAnns;
	}

	public ArrayList<Annotation> getAutoAnnsForKey(String key) {
		return autoAnns.get(key);
	}
		
	public void addToAutoAnns(String key,Annotation value) {
		if(autoAnns.containsKey(key)){
			autoAnns.get(key).add(value);
		}
		else{
			ArrayList<Annotation> valueList=new ArrayList<Annotation>(6);
			valueList.add(value);
			autoAnns.put(key,valueList);
		}
	}

	public HashMap<String, ArrayList<Annotation>> getGatedefaultAnns() {
		return gatedefaultAnns;
	}

	public ArrayList<Annotation> getGatedefaultAnnsForKey(String key) {
		return gatedefaultAnns.get(key);
	}
		
	public void addToGatedefaultAnns(String key,Annotation value) {
		if(gatedefaultAnns.containsKey(key)){
			gatedefaultAnns.get(key).add(value);
		}
		else{
			ArrayList<Annotation> valueList=new ArrayList<Annotation>(6);
			valueList.add(value);
			gatedefaultAnns.put(key,valueList);
		}
	}
	

	public int getDocID() {
		return docID;
	}


	public void setDocID(int docID) {
		this.docID = docID;
	}


	public int getStrongCount() {
		return strongCount;
	}


	public void setStrongCount(int strongCount) {
		this.strongCount = strongCount;
	}


	public int getWeakCount() {
		return weakCount;
	}


	public void setWeakCount(int weakCount) {
		this.weakCount = weakCount;
	}

	public int getSenID() {
		return senID;
	}


	public void setSenID(int senID) {
		this.senID = senID;
	}

	public String toString(){
		return "spanS:"+spanS+" spanE:"+spanE+" sC:"+strongCount+" wC:"+weakCount+" hC:"+hpC+" gC:"+goldC+" sID:"+senID+" dID:"+docID;
	}
	
	public String getGOutputString(){
		return "ID"+senID+"\t"+String.valueOf(spanS)+","+String.valueOf(spanE)+"\tstring\tMPQA\tgoldclass=\""+goldC+"\"";
	}
	
	public String getHOutputString(){
		return "ID"+senID+"\t"+String.valueOf(spanS)+","+String.valueOf(spanE)+"\tstring\tMPQA\tautoclass=\""+hpC+"\"";
	}
	
	public String getSplitString(){
		return "ID"+senID+"\t"+String.valueOf(spanS)+","+String.valueOf(spanE)+"\tstring\tGATE_Sentence";
	}


	public TreeMap<Span, LinkedList<String>> getClues() {
		return clues;
	}


	public void setClues(TreeMap<Span, LinkedList<String>> clues) {
		this.clues = clues;
	}


	public HashSet<Span> getModal() {
		if(modal==null){
			modal = new HashSet<Span>();
		} 
		
			return modal;
				
	}


	public void setModal(HashSet<Span> modal) {
		this.modal = modal;
	}


	public HashSet<Span> getAdverb() {
		if(adverb==null){
			adverb = new HashSet<Span>();
		} 
		
		return adverb;
		
	}


	public void setAdverb(HashSet<Span> adverb) {
		this.adverb = adverb;
	}


	public HashSet<Span> getCardinal() {
		if(cardinal==null){
			cardinal = new HashSet<Span>();
		} 
		
			return cardinal;
		
	}


	public void setCardinal(HashSet<Span> cardinal) {
		this.cardinal = cardinal;
	}


	public HashSet<Span> getPronoun() {
		if(pronoun==null){
			pronoun = new HashSet<Span>();
		} 
			return pronoun;

	}


	public void setPronoun(HashSet<Span> pronoun) {
		this.pronoun = pronoun;
	}


	public HashSet<Span> getAdjective() {
		if(adjective==null){
			adjective = new HashSet<Span>();
		} 
		return adjective;		
	}


	public void setAdjective(HashSet<Span> adjective) {
		this.adjective = adjective;
	}
	
	public void addToAdjective(Span s) {
		this.adjective.add(s);
	}

	public void addToAdverb(Span s) {
		this.adverb.add(s);
	}

	public void addToModal(Span s) {
		this.modal.add(s);
	}
	
	public void addToPronoun(Span s) {
		this.pronoun.add(s);
	}
	
	public void addToCardinal(Span s) {
		this.cardinal.add(s);
	}


	public void addToEndClues(Span s, String type) {
		if(clues.containsKey(s)){
			clues.get(s).add(type);
		}
		else{
			LinkedList<String> typeList=new LinkedList<String>();
			typeList.add(type);
			clues.put(s,typeList);
		}		
	}

	public void addToClues(Span s, String type){
		if(clues.containsKey(s)){
			clues.get(s).add(0,type);
		}
		else{
			LinkedList<String> typeList=new LinkedList<String>();
			typeList.add(type);
			clues.put(s,typeList);
		}
	}

	public int getSubjPatternCount() {
		return subjPatternCount;
	}


	public void setSubjPatternCount(int subjPatternCount) {
		this.subjPatternCount = subjPatternCount;
	}


	public int getObjPatternCount() {
		return objPatternCount;
	}


	public void setObjPatternCount(int objPatternCount) {
		this.objPatternCount = objPatternCount;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		Sentence s =(Sentence)obj;
		if(docID==s.docID && spanS==s.spanS && spanE==s.spanE){
		//if(spanS==s.spanS && spanE==s.spanE){
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		//String s=String.valueOf(spanS)+"-"+String.valueOf(spanE);
		String s=String.valueOf(docID)+"-"+String.valueOf(spanS)+"-"+String.valueOf(spanE);
		return s.hashCode();
	}

}
