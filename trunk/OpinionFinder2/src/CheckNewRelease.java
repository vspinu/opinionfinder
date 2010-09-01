import io.IO;

import java.util.ArrayList;
import java.util.HashMap;

import logic.ClassificationHandler;
import logic.ClueHandler;
import logic.DocHandler;
import logic.EvaluationHandler;
import config.Config;
import entity.Annotation;
import entity.Doc;
import entity.Sentence;


public class CheckNewRelease {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Config.initialize();
		ArrayList<Doc> docs = DocHandler.readDocList(Config.DOCLISTFILE);			
		
		int docID=1;		
		for(Doc doc : docs){	    	
				    			
			//System.out.println("Document "+docID+" is processed");
			
			//if(doc.getCorrespondingManAnn().getAbsolutePath().contains("ula") && docID!=573){
			//if(doc.getCorrespondingManAnn().getAbsolutePath().contains("ula")){
				System.out.println("STARTING doc : "+doc.getCorrespondingRawFile().getAbsoluteFile());
				System.out.println("");
				System.out.println("");
				DocHandler.fillDocWithSentences(doc);
				printManAnns(doc);
			//}
				
			//}
			docID++;			
	    }			
		
	}
	
	
	private static void printManAnns(Doc doc){
		int senID=1;	
		ArrayList<Sentence> sentences  = doc.getSentences();		
		for(Sentence s : sentences){
			System.out.println("Processing Sentence "+senID);
			System.out.println(doc.getRawSpan(s.getSpanS(),s.getSpanE()).replaceAll("\n", " "));
			HashMap<String,ArrayList<Annotation>> manAnns=s.getManAnns();
			for(String key : manAnns.keySet()){
				if(!key.equals("GATE_split")){
					for(Annotation ann : manAnns.get(key)){
						System.out.println("\t"+key+":"+ann.getSpanS()+"-"+ann.getSpanE()+"\t"+doc.getRawSpan(ann.getSpanS(),ann.getSpanE()).replaceAll("\n", " "));
					}
				}
			}
			senID++;
		}
				
	}
	
	
	
	
	
}
