package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import entity.Annotation;
import entity.Sentence;

public class SentenceHandler {

	public static ArrayList<Sentence> createSentencesFromManAnns(ArrayList<Annotation> Anns,int docID){
		ArrayList<Sentence> output=new ArrayList<Sentence>();
		HashMap<String,String> atts;
		Annotation ann;
		int senID=1;
		for(Iterator<Annotation> itr=Anns.iterator();itr.hasNext();){
			ann=itr.next();
			if(ann.getName().equals("GATE_inside")){
				atts=ann.getAttributes();
				if(atts.containsKey("nested-source") && atts.get("nested-source").equals("w")){
					if(!atts.containsKey("error")){
						if(ann.getSpanS()<=ann.getSpanE()){
							output.add(new Sentence(ann.getSpanS(),ann.getSpanE(),senID,docID));
							senID++;
						}
					}
				}
			}
		}		
		return output;
	}
	
	
	public static ArrayList<Sentence> createSentencesFromGateDefault(ArrayList<Annotation> Anns,int docID){
		ArrayList<Sentence> output=new ArrayList<Sentence>();
		int senID=1;
		for(Iterator<Annotation> itr=Anns.iterator();itr.hasNext();){
			Annotation ann=itr.next();
			if(ann.getName().equals("GATE_Sentence")){
				output.add(new Sentence(ann.getSpanS(),ann.getSpanE(),senID,docID));
				senID++;
			}
		}		
		return output;
	}
	
	
	public static void fillSentencesWithAutoAnns(ArrayList<Annotation> autoAnns,ArrayList<Sentence> sentences){
		Annotation aann;
		Sentence sen;
		int index=0;
		for(Iterator<Annotation> itr=autoAnns.iterator();itr.hasNext();){
			sen=sentences.get(index);
			aann=itr.next();
			while(aann.getSpanS()>=sen.getSpanE() && index<sentences.size()-1){
				index++;
				sen=sentences.get(index);
			}
			if(aann.getSpanS()>=sen.getSpanS() && aann.getSpanE()<=sen.getSpanE()){
				sen.addToAutoAnns(aann.getName(), aann);
			}
		}				
	}
	
	
	public static void fillSentencesWithManAnns(ArrayList<Annotation> manAnns,ArrayList<Sentence> sentences){
		Annotation mann;
		Sentence sen;
		int index=0;
		for(Iterator<Annotation> itr=manAnns.iterator();itr.hasNext();){
			sen=sentences.get(index);
			mann=itr.next();
			while(mann.getSpanS()>=sen.getSpanE() && index<sentences.size()-1){
				index++;
				sen=sentences.get(index);
			}
			sen.addToManAnns(mann.getName(), mann);
		}				
	}
			
	public static void fillSentencesWithGatedefaultAnns(ArrayList<Annotation> gatedefaultAnns,ArrayList<Sentence> sentences){
		Annotation gatedefault;
		Sentence sen;
		int index=0;
		for(Iterator<Annotation> itr=gatedefaultAnns.iterator();itr.hasNext();){
			sen=sentences.get(index);
			gatedefault=itr.next();
			while(gatedefault.getSpanS()>=sen.getSpanE() && index<sentences.size()-1){
				index++;
				sen=sentences.get(index);
			}
			sen.addToGatedefaultAnns(gatedefault.getName(), gatedefault);
		}				
	}
}
