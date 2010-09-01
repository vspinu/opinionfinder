package supervised;
import io.IO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.TreeMap;

import logic.ClassificationHandler;
import logic.ClueHandler;
import logic.DocHandler;
import logic.FeatureHandler;
import config.Config;
import entity.Doc;
import entity.Span;


public class FeatureBuilder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
				
		Config.initialize();
		ArrayList<Doc> docs = DocHandler.readDocList(Config.DOCLISTFILE);
		FeatureHandler fh = new FeatureHandler();
		TreeMap<String,ArrayList<Span>> excludedSpans=new TreeMap<String,ArrayList<Span>>();
		TreeMap<String,ArrayList<Span>> consideredSpans=new TreeMap<String,ArrayList<Span>>();
		
		if(Config.EXCLUDESPANFILE!=null){
			IO.readExcludedSpans(Config.EXCLUDESPANFILE,excludedSpans,consideredSpans);
		}
		
		int docID=1;
		for(Doc doc : docs){
	    	
			DocHandler.fillDocWithSentences(doc);	    	
	    	ClueHandler.insertAllClueTypes(doc);
	    	ClueHandler.removeClueSpans(doc,excludedSpans);
			ClueHandler.handleFeatures(doc);
			
	    	ClassificationHandler.classifyGold(doc);
	    	
	    	fh.storeFeatures(doc);
	        	    		    	
			if(Config.LOG){
				IO.writeLog(doc, !(docID==1));
			}
			if(Config.WRITEGOLD){
				IO.writeGoldClassification(doc);
			}
			if(Config.WRITESPLIT){
				IO.writeSentenceSplit(doc);
			}
			
			System.out.println("DOC with ID: " + docID + " is processed");
			docID++;
	    }
		
		fh.writeArffNominalFile(new File("data"+File.separator+"SenNominal.arff"));
		fh.writeArffNumericFile(new File("data"+File.separator+"SenNumeric.arff"));
		fh.writeArffNumericFile(new File("data"+File.separator+"SenSparse.dat"));
		
		
		
		try {
			persistObject(docs);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void persistObject(ArrayList<Doc> docs) throws Exception{
		
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("docs.dat"));
		 oos.writeObject(docs);
		 oos.close();
		
	}

}
