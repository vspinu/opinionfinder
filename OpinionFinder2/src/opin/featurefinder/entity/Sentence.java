package opin.featurefinder.entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class Sentence {
	
	int id;
	private ArrayList<Token> tokenList;
	private HashMap<String,ArrayList<Token>> wordMap;
	private HashMap<String,ArrayList<Token>> lemmaMap;
	private HashMap<String,ArrayList<Token>> posMap;
	private HashMap<String,ArrayList<Token>> mCMap;
	
	public Sentence(int id) {
		this.id=id;
		tokenList=new ArrayList<Token>();
		wordMap=new HashMap<String, ArrayList<Token>>(100);
		lemmaMap=new HashMap<String, ArrayList<Token>>(100);
		posMap=new HashMap<String, ArrayList<Token>>(50);
		mCMap=new HashMap<String, ArrayList<Token>>(8);
	}
	
	
	public void addToken(Token token){
		tokenList.add(token);
		addToMap(wordMap,token.getWord(),token);
		addToMap(lemmaMap,token.getLemma(),token);
		addToMap(posMap,token.getPos(),token);
		addToMap(mCMap,token.getMC(),token);		
	}
	
	private void addToMap(HashMap<String, ArrayList<Token>> map, String key, Token token){
		if(map.containsKey(key)){
			map.get(key).add(token);
		}
		else{
			ArrayList<Token> tokenList=new ArrayList<Token>();
			tokenList.add(token);
			map.put(key,tokenList);
		}		
	}
	
	public boolean wordMapContains(String key){
		return wordMap.containsKey(key);
	}
	
	public boolean lemmaMapContains(String key){
		return lemmaMap.containsKey(key);
	}
	
	public boolean posMapContains(String key){
		return posMap.containsKey(key);
	}
	
	public boolean mCMapContains(String key){
		return mCMap.containsKey(key);
	}
	
	public ArrayList<Token> getFromWordMap(String key){
		return wordMap.get(key);
	}
	
	public ArrayList<Token> getFromLemmaMap(String key){
		return lemmaMap.get(key);
	}
	
	public ArrayList<Token> getFromPosMap(String key){
		return posMap.get(key);
	}
	
	public ArrayList<Token> getFromMCMap(String key){
		return mCMap.get(key);
	}


	public ArrayList<Token> getTokenList() {
		return tokenList;
	}


	public HashMap<String, ArrayList<Token>> getWordMap() {
		return wordMap;
	}


	public HashMap<String, ArrayList<Token>> getLemmaMap() {
		return lemmaMap;
	}


	public HashMap<String, ArrayList<Token>> getPosMap() {
		return posMap;
	}


	public HashMap<String, ArrayList<Token>> getMCMap() {
		return mCMap;
	}


	public int getId() {
		return id;
	}
	
	@Override
	public String toString() {
		String output="";
		for (Iterator<Token> itr = tokenList.iterator(); itr.hasNext();) {
			Token t = itr.next();
			output+=t.getWord()+" ";
		}
		return output;
	}

}
