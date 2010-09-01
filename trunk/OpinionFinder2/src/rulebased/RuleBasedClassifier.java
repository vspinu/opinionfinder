package rulebased;

import io.IO;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

import logic.ClassificationHandler;
import logic.ClueHandler;
import logic.DocHandler;
import logic.EvaluationHandler;
import logic.Utils;
import config.Config;
import entity.Doc;
import entity.Sentence;
import entity.Span;

public class RuleBasedClassifier {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//classifyModifiedSentences();
		
		originalRuleBased();
		//modifiedRuleBased(args);

	}
	
	
	public static void originalRuleBased(){
				
		Config.initialize();
		ArrayList<Doc> docs = DocHandler.readDocList(Config.DOCLISTFILE);
				
		
		int docID=1;		
		for(Doc doc : docs){
	    	
			DocHandler.fillDocWithSentences(doc);	    	
	    	ClueHandler.insertAllClueTypes(doc);
	    	
	    	ClueHandler.handleFeatures(doc);
	    	//ClassificationHandler.classifyGold(doc); // works with old terminology. Might not be correct in the saved corpus binaries.
	    	ClassificationHandler.classifyRuleBased(doc);
	    		        	    		  
			if(Config.LOG){
				IO.writeLog(doc, !(docID==1));
			}
			if(Config.WRITEHP){
				IO.writeHpClassification(doc);
			}
			if(Config.WRITEGOLD){
				IO.writeGoldClassification(doc);
			}
			if(Config.WRITESPLIT){
				IO.writeSentenceSplit(doc);
			}
			
			System.out.println("Document "+docID+" is processed");
			docID++;			
	    }
		
		if(Config.EVALUATE){
			double[][] cm = EvaluationHandler.evaluateRuleBased(docs);
			EvaluationHandler.writeEval(cm);
		}
		
		try {
			persistObject(docs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void modifiedRuleBased(String args[]){

		if(args.length!=3){
			System.err.println("Wrong Number of Arguments!");
		}
		
		String exclude = args[0]; 
		String range = args[1];
		boolean context = Boolean.parseBoolean(args[2]);
		
		if(!(exclude.equals("1") || exclude.equals("2") || exclude.equals("3") || exclude.equals("4"))){
			System.err.println("Undefined value for exclude parameter!");
		}
		
		if(!(range.equals("1") || range.equals("2") || range.equals("3"))){
			System.err.println("Undefined value for range parameter!");
		}
		
		Config.initialize();
		ArrayList<Doc> docs = DocHandler.readDocList(Config.DOCLISTFILE);
		TreeMap<String,ArrayList<Span>> excludedSpans=new TreeMap<String,ArrayList<Span>>();
		TreeMap<String,ArrayList<Span>> consideredSpans=new TreeMap<String,ArrayList<Span>>();
		
		if(Config.EXCLUDESPANFILE!=null){
			IO.readExcludedSpans(Config.EXCLUDESPANFILE, excludedSpans, consideredSpans);
		}
		
		HashSet<Sentence> evalSentences = new HashSet<Sentence>();
		
		int docID=1;		
		for(Doc doc : docs){
	    	
			DocHandler.fillDocWithSentences(doc);	    	
	    	ClueHandler.insertAllClueTypes(doc);
	    	
	    		    	
	    	if(exclude.equals("1")){
	    		ClueHandler.removeOverlappingClueSpans(doc,excludedSpans);	    		
	    	}
	    	else if(exclude.equals("2")){
	    		excludedSpans=Utils.takeRandomSubset(consideredSpans);
	    		ClueHandler.removeOverlappingClueSpans(doc,excludedSpans);
	    	}
	    	else if(exclude.equals("3")){
	    		ClueHandler.removeOverlappingClueSpans(doc,consideredSpans);
	    	}
	    	else{ //none
	    		
	    	}
	    	
	    	ClueHandler.handleFeatures(doc);
	    	ClassificationHandler.classifyGold(doc);
	    	ClassificationHandler.classifyRuleBased(doc);
	    	
	    	if(range.equals("1")){
	    		evalSentences.addAll(ClueHandler.getEvaluationSentences(doc, excludedSpans, context));
	    	}	    	
	    	else if(range.equals("2")){
	    		evalSentences.addAll(ClueHandler.getEvaluationSentences(doc, consideredSpans, context));
	    	}
	    	else{ //all
	    		evalSentences.addAll(doc.getSentences());
	    	}
	    	
			
	    	
			
			
	    		        	    		  
			if(Config.LOG){
				IO.writeLog(doc, !(docID==1));
			}
			if(Config.WRITEHP){
				IO.writeHpClassification(doc);
			}
			if(Config.WRITEGOLD){
				IO.writeGoldClassification(doc);
			}
			if(Config.WRITESPLIT){
				IO.writeSentenceSplit(doc);
			}
			for(Sentence sen : doc.getSentences()){
				//System.out.println("DOC with ID: " + docID + " SEN with ID: "+sen.getSenID()+" class: "+sen.getHpC()+" strong: "+sen.getStrongCount()+" weak: "+sen.getWeakCount());
			}
			//System.out.println("DOC with ID: " + docID + " is processed");
			docID++;
	    }
		
		
		//double[][] cm = EvaluationHandler.evaluateRuleBased(docs);
		double[][] cm = EvaluationHandler.evaluateRuleBased(evalSentences);
		EvaluationHandler.writeEval(cm);		
		EvaluationHandler.printAverageClueNumber(evalSentences);
		
		try {
			//persistObject(docs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	/*
	
    public static void classifyModifiedSentences(){
		
		Config.initialize();
		ArrayList<Doc> docs = DocHandler.readDocList(Config.DOCLISTFILE);
		TreeMap<String,ArrayList<Span>> excludedSpans=new TreeMap<String,ArrayList<Span>>();
		TreeMap<String,ArrayList<Span>> consideredSpans=new TreeMap<String,ArrayList<Span>>();
		
		if(Config.EXCLUDESPANFILE!=null){
			IO.readExcludedSpans(Config.EXCLUDESPANFILE, excludedSpans, consideredSpans);
		}
		
		HashSet<Sentence> modSentences = new HashSet<Sentence>();
		
		int docID=1;		
		for(Doc doc : docs){
	    	
			DocHandler.fillDocWithSentences(doc);	    	
	    	ClueHandler.insertAllClueTypes(doc);
	    	modSentences.addAll(ClueHandler.getEvaluationSentences(doc, excludedSpans, true));
	    	ClueHandler.removeOverlappingClueSpans(doc,excludedSpans);
			ClueHandler.handleFeatures(doc);
			ClassificationHandler.classifyGold(doc);
			ClassificationHandler.classifyRuleBased(doc);
	    		        	    		  
			if(Config.LOG){
				IO.writeLog(doc, !(docID==1));
			}
			if(Config.WRITEHP){
				IO.writeHpClassification(doc);
			}
			if(Config.WRITEGOLD){
				IO.writeGoldClassification(doc);
			}
			if(Config.WRITESPLIT){
				IO.writeSentenceSplit(doc);
			}
			for(Sentence sen : doc.getSentences()){
				//System.out.println("DOC with ID: " + docID + " SEN with ID: "+sen.getSenID()+" class: "+sen.getHpC()+" strong: "+sen.getStrongCount()+" weak: "+sen.getWeakCount());
			}
			//System.out.println("DOC with ID: " + docID + " is processed");
			docID++;
	    }
		
		
		//double[][] cm = EvaluationHandler.evaluateRuleBased(docs);
		double[][] cm = EvaluationHandler.evaluateRuleBased(docs,modSentences);
		EvaluationHandler.writeEval(cm);
		
		try {
			//persistObject(docs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	
	public static void persistObject(ArrayList<Doc> docs) throws Exception{
		
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("docsCompleteEMNLP05NewTerminology2.dat"));
		 oos.writeObject(docs);
		 oos.close();
		
	}

}
