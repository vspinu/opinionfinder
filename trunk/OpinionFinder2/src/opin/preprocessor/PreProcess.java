package opin.preprocessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.WordTag;

import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import opin.entity.Corpus;
import opin.io.*;
import opin.preprocessor.entity.*;

/**
 * This class reads in documents from a doclist and generates the gate_default
 * files. the stanford parser is used for lemmatization. The functionality of
 * module 2 is encompassed by this class.
 *
 * @author cem (minor modifications and comments by conrada)
 */
public class PreProcess {
	
	/**
         * encoding of the documents specified in doclist
         */
	private String encoding;

        /**
         * stanford model file; ie, models/left3words-wsj-0-18.tagger
         */
	private File posModel;
	
	
	public PreProcess(File posModel, String encoding) {
			    
	    this.encoding=encoding;
	    this.posModel=posModel;
	    
	}
	
	public static void main(String[] args){
		
		File doclistFile=null;
		File modelFile=null;
		String encoding="UTF-8";
		
		if(args.length==2){						
			doclistFile=new File(args[0]);
			modelFile=new File(args[1]);
			if(!doclistFile.exists()){
				System.out.println("Doclist does not exist!");
				System.exit(-1);
			}
			if(!modelFile.exists()){
				System.out.println("Model does not exist!");
				System.exit(-1);
			}
			
		}
		else if(args.length>2){
			doclistFile=new File(args[0]);
			modelFile=new File(args[1]);
			if(!doclistFile.exists()){
				System.out.println("Doclist does not exist!");
				System.exit(-1);
			}
			if(!modelFile.exists()){
				System.out.println("Model does not exist!");
				System.exit(-1);
			}
			int i=2;
			while(i<args.length){
				if(args[i].equals("-e")){
					if(i+1<args.length){
						encoding=args[i+1];
					}
					else{
						System.out.println("Encoding value is missing !");
						System.exit(-1);
					}
				}				
				else{
					System.out.println("Unknown argument "+args[i]+" !");
					System.exit(-1);
				}
				i+=2;
			}			
		}
		else{
			System.out.println("Usage: <doclist> <model> (-e <encoding>)");
			System.exit(-1);			
		}

		
		Corpus corpus = new Corpus(doclistFile);
		PreProcess pre= new PreProcess(modelFile,encoding);		
		pre.process(corpus);
					
		
	}
	
	
	public void process(Corpus corpus){
		
            // eventually, process should be modified to return this, so that
            //  future parts of the pipeline don't need to reread it from disk
            ArrayList<ArrayList<GateDefaultLine>> gateDefaultContents =
                    new ArrayList<ArrayList<GateDefaultLine>>();

		FileInputStream rawFile=null;
		InputStreamReader sReader=null;
		BufferedReader bReader=null;

		try {
			MaxentTagger tagger = new MaxentTagger(posModel.getAbsolutePath());
			
			ArrayList<File> docs = corpus.getDocs();
			
			for(File doc : docs){			
				rawFile = new FileInputStream(doc);		
				sReader = new InputStreamReader(rawFile,encoding);			
				bReader = new BufferedReader(sReader,40960);

				int characterOffSet=0; 
				ArrayList<GateDefaultLine> gls = new ArrayList<GateDefaultLine>();
				ArrayList<String> ntls = new ArrayList<String>();

				List<ArrayList<? extends HasWord>> sentences = tagger.tokenizeText(bReader);
				for (ArrayList<? extends HasWord> sentence : sentences) {
					ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
					String text = formatPTB(tSentence);
					gls.addAll(getGateDefaultLines(text, tSentence, characterOffSet));
					ntls.add(text);
					characterOffSet+=text.length()+1;
				}
				
				WriterUtils.writeLinesWithId(Corpus.getAnnotationFile("gate_default", doc), gls);
				WriterUtils.writeLines(Corpus.getAnnotationFile("new_content", doc), ntls);
				
				bReader.close();
				sReader.close();
				rawFile.close();
				
			}

		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(rawFile!=null){
					rawFile.close();	
				}
				if(sReader!=null){
					sReader.close();	
				}
				if(bReader!=null){
					bReader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	    
	}
	
	private ArrayList<GateDefaultLine> getGateDefaultLines(String sentenceText,  ArrayList<TaggedWord> tSentence, int characterOffset){
		
		ArrayList<GateDefaultLine> output = new ArrayList<GateDefaultLine>();
		output.add(new GateDefaultLine(characterOffset, characterOffset+sentenceText.length()));
		
		int lastIndex=0;
		int currentIndex;
		int spanS;
		int spanE;
		for (TaggedWord tWord : tSentence){
			String w= PTBTokenizer.ptbToken2Text(tWord.word());
			currentIndex = sentenceText.indexOf(w,lastIndex);
			if(currentIndex==-1){
				System.out.println("Cannot find "+w+" in sentence : "+sentenceText);
				System.out.println("Skipping sentence");
				return new ArrayList<GateDefaultLine>();
			}
			spanS = characterOffset+currentIndex;
			spanE = spanS+w.length();
			String pos = tWord.tag();
			output.add(new GateDefaultLine(w, Morphology.lemmatizeStatic(new WordTag(w,pos)).lemma(), pos, spanS, spanE));
			
			lastIndex=currentIndex+w.length();
			
    	}
		
		return output;

	} 
	
	private String formatPTB(ArrayList<TaggedWord> sentence){
		
		StringBuilder sb = new StringBuilder();
		
		for (TaggedWord word : sentence){
			sb.append(" "+PTBTokenizer.ptbToken2Text(word.word()));
    	}
		
		return PTBTokenizer.ptb2Text(sb.toString().trim());

	} 
    
 
}
