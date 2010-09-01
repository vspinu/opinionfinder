package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import config.Config;
import entity.Annotation;
import entity.Doc;
import entity.Sentence;

public class ClassificationHandler {

	
	// OLD MPQA Attributes
	public static void classifyGold(Doc doc){
		Sentence sen;		
		for(Iterator<Sentence> itr_i=doc.getSentences().iterator();itr_i.hasNext();){
			sen=itr_i.next();
			sen.setGoldC(markAsSubjectiveGold(sen,"GATE_on"));
			if(sen.getGoldC()!=Config.SUBJ){
				sen.setGoldC(markAsSubjectiveGold(sen,"GATE_expressive-subjectivity"));
				if(sen.getGoldC()!=Config.SUBJ){
					sen.setGoldC(Config.OBJ);
				}
			}
		}
	} 
	// OLD MPQA Attributes
	public static void classifyGold(ArrayList<Sentence> sentences){
		Sentence sen;		
		for(Iterator<Sentence> itr_i=sentences.iterator();itr_i.hasNext();){
			sen=itr_i.next();
			sen.setGoldC(markAsSubjectiveGold(sen,"GATE_on"));
			if(sen.getGoldC()!=Config.SUBJ){
				sen.setGoldC(markAsSubjectiveGold(sen,"GATE_expressive-subjectivity"));
				if(sen.getGoldC()!=Config.SUBJ){
					sen.setGoldC(Config.OBJ);
				}
			}
		}
	} 
	
	public static String markAsSubjectiveGold(Sentence sen, String annName){
		String output=Config.OBJ;
		HashMap<String,ArrayList<Annotation>> manAnns=sen.getManAnns();
		ArrayList<Annotation> annotations;
		HashMap<String,String> attpairs;
		if(manAnns.containsKey(annName)){				
			annotations=manAnns.get(annName);				
			for(Iterator<Annotation> itr_j=annotations.iterator();itr_j.hasNext();){
				attpairs=itr_j.next().getAttributes();
				if(attpairs.containsKey("strength") && Config.GOLDSTRENGTHFORSUBJ.contains(attpairs.get("strength"))){  
					output=Config.SUBJ;
					break;
				}
				if(attpairs.containsKey("on-strength") && Config.GOLDSTRENGTHFORSUBJ.contains(attpairs.get("on-strength"))){  
					output=Config.SUBJ;
					break;
				}
				if(attpairs.containsKey("overall-strength") && Config.GOLDSTRENGTHFORSUBJ.contains(attpairs.get("overall-strength"))){  
					output=Config.SUBJ;
					break;
				}
			}
		}
		return output;
	}
	
	public static void classifyRuleBased(Doc doc){
		ArrayList<Sentence> sentences=doc.getSentences();
		Sentence current;
		Sentence prev;
		Sentence following;
		int totalStrongCount;
		int totalWeakCount;
		
		
		for(int i=0;i<sentences.size();i++){
			totalStrongCount=0;
			totalWeakCount=0;
			current=sentences.get(i);
			if((current.getStrongCount()+current.getSubjPatternCount())>1){
				current.setHpC(Config.SUBJ);
			}
			else{
				if(i>0 && i<sentences.size()-1){
					prev=sentences.get(i-1);
					following=sentences.get(i+1);
					totalStrongCount=prev.getStrongCount()+prev.getSubjPatternCount()+following.getStrongCount()+following.getSubjPatternCount();
					totalWeakCount=prev.getWeakCount()+following.getWeakCount()+current.getWeakCount();
					if((current.getStrongCount()+current.getSubjPatternCount())==0 && totalStrongCount<Config.SIMPLEOBJTESTSTRONG && totalWeakCount<Config.SIMPLEOBJTESTWEAK){
						current.setHpC(Config.OBJ);
					}
					else{
						current.setHpC(Config.UNKNOWN);
					}
				}
				else{
					current.setHpC(Config.UNKNOWN);
				}
				//if(i<sentences.size()){
				//	following=sentences.get(i+1);
				//	totalStrongCount+=following.getStrongCount();
				//	totalWeakCount+=following.getWeakCount();
				//}
			}			
		}		
	}
	
	
	public static void classifyRuleBased(ArrayList<Sentence> sentences){
		Sentence current;
		Sentence prev;
		Sentence following;
		int totalStrongCount;
		int totalWeakCount;
		
		
		for(int i=0;i<sentences.size();i++){
			totalStrongCount=0;
			totalWeakCount=0;
			current=sentences.get(i);
			if((current.getStrongCount()+current.getSubjPatternCount())>1){
				current.setHpC(Config.SUBJ);
			}
			else{
				if(i>0 && i<sentences.size()-1){
					prev=sentences.get(i-1);
					following=sentences.get(i+1);
					totalStrongCount=prev.getStrongCount()+prev.getSubjPatternCount()+following.getStrongCount()+following.getSubjPatternCount();
					totalWeakCount=prev.getWeakCount()+following.getWeakCount()+current.getWeakCount();
					if((current.getStrongCount()+current.getSubjPatternCount())==0 && totalStrongCount<Config.SIMPLEOBJTESTSTRONG && totalWeakCount<Config.SIMPLEOBJTESTWEAK){
						current.setHpC(Config.OBJ);
					}
					else{
						current.setHpC(Config.UNKNOWN);
					}
				}
				else{
					current.setHpC(Config.UNKNOWN);
				}
				//if(i<sentences.size()){
				//	following=sentences.get(i+1);
				//	totalStrongCount+=following.getStrongCount();
				//	totalWeakCount+=following.getWeakCount();
				//}
			}			
		}		
	}
	
	
}
