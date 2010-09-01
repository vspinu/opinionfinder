package opin.preprocessor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.WordTag;
import edu.stanford.nlp.objectbank.TokenizerFactory;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.Options;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.trees.EnglishGrammaticalStructure;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

import opin.io.*;
import opin.preprocessor.entity.*;

public class PreProcess {
	

	private final static String NEWLINE="\n";
	private final static String DUMMYREL="i";
	private final static String VERB="V";
	private final static String NOUN="N";
	private final static String ADJ="J";
	private final static String ADV="R";
	
	private final static String LRB= "-LRB-";
	private final static String LRBC= "(";
	private final static String RRB= "-RRB-";
	private final static String RRBC= ")";
	private final static String RSB= "-RSB-";
	private final static String RSBC= "]";
	private final static String LSB= "-LSB-";
	private final static String LSBC= "[";
	private final static String LCB= "-LCB-";
	private final static String LCBC= "{";
	private final static String RCB= "-RCB-";
	private final static String RCBC= "}";
	
	private final static Pattern NONWORD= Pattern.compile("[^\\w]+?");	
	
	private String tag;
	private String encoding;
	private LexicalizedParser lp;
	private DocumentPreprocessor dp;
	private WordNetDatabase wordnet; //"/home/cem/WordNet-3.0/dict"
	
	private FileOutputStream rawFile=null;
	private OutputStreamWriter sWriter=null;
	private BufferedWriter bWriter=null;
	
	

	
	public PreProcess(String model, String encoding, String tag, String wordnetLocation) {
		
		/*
		 * SP Initialization
		 */
		
		Options op=new Options();
		TreebankLanguagePack tlp = op.tlpParams.treebankLanguagePack();
		TokenizerFactory tokenizerFactory = tlp.getTokenizerFactory();
		dp = new DocumentPreprocessor();
		dp.setTokenizerFactory(tokenizerFactory);
	    dp.setSentenceFinalPuncWords(tlp.sentenceFinalPunctuationWords());
	    dp.setEncoding(encoding);
	    lp=new LexicalizedParser(model,op);
	    
	    /*
		 * SP Initialization
		 */
	    
	    
	    if(wordnetLocation==null){
	    	wordnet=null;
	    }
	    else{
	    	System.setProperty("wordnet.database.dir", wordnetLocation);
	    	wordnet = WordNetDatabase.getFileInstance();	    	
	    }
	    this.encoding=encoding;
	    this.tag=tag;
	}
	
	public static void main(String[] args){
		
		File doclistFile=null;
		File modelFile=null;
		boolean onlyPosTagging=false;
		String encoding="UTF-8";
		String tag=null;
		String wordnet=null;
		
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
				if(args[i].equals("-p")){
					onlyPosTagging=true;
				}
				else if(args[i].equals("-e")){
					if(i+1<args.length){
						encoding=args[i+1];
					}
					else{
						System.out.println("Encoding value is missing !");
						System.exit(-1);
					}
				}
				else if(args[i].equals("-t")){
					if(i+1<args.length){
						tag=args[i+1];
					}
					else{
						System.out.println("Tag value is missing !");
						System.exit(-1);
					}
				}
				else if(args[i].equals("-w")){
					if(i+1<args.length){
						wordnet=args[i+1];
					}
					else{
						System.out.println("WordNet path is missing !");
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
			System.out.println("Usage: <doclist> <model> (-p) (-e <encoding>) (-t <tag>) (-w <wordnetpath>)");
			System.exit(-1);			
		}

		
		PreProcess pre= new PreProcess(modelFile.getAbsolutePath(),encoding,tag,wordnet);
		
		
		ArrayList<File> docs = new ArrayList<File>();
		ArrayList<File> deps = new ArrayList<File>();
		ArrayList<File> senSplits = new ArrayList<File>();
		
		Reader.readDocList(doclistFile, null, docs, deps, senSplits, null);

		String content;
		if(onlyPosTagging){
				
				for (int i=0;i<docs.size();i++) {					
					pre.processWithoutParsing(docs.get(i),deps.get(i),senSplits.get(i));
				}			
				
		}
		else{
			
				for (int i=0;i<docs.size();i++) {				
					pre.processWithParsing(docs.get(i), deps.get(i), senSplits.get(i));
				}			
			
		}
		
		
		
	}
	
	

	/*
	 * 
	 * SP Parsing
	 * 
	 */

	public void processWithParsing(File doc, File dep, File senSplit){
		String content=readDocument(doc);
		
		System.out.println("Processing : "+doc.getAbsolutePath());
		
		List<List<? extends HasWord>> document=null;
		try {	    	
			if (tag != null) {
				document = dp.getSentencesFromXML(new StringReader(content), null, tag, null);
			} else {
				document = dp.getSentencesFromText(new StringReader(content), null, null, -1);
			}	  
		}
		catch(Exception ex){
			ex.printStackTrace();
			
			System.err.println("Exception while processing file :"+doc.getAbsolutePath());	
			return;
		}


		int num=0;
		
		
		openFileToWrite(dep);
		
		int lastSentenceSpanE = 0;
		for (List sentence : document) {
			num++;
		
			int len = sentence.size();
			
			System.out.println("Parsing [sent. " + num + " len. " + len + "]: " + sentence);

			Tree ansTree = null;
			try {
				if ( ! lp.parse(sentence)) {
					System.err.print("Sentence couldn't be parsed by grammar! file : "+doc.getAbsolutePath()+" sentence no : "+num);
					return;
				} else { 	             
					ansTree = lp.getBestParse();
				}

			} catch (OutOfMemoryError e) {
				System.err.println("Out of memory! file : "+doc.getAbsolutePath()+" sentence no : "+num);
				return;
				
			} catch (UnsupportedOperationException uoe) {				
				System.err.println("Sentence skipped: too long (or zero words)! file : "+doc.getAbsolutePath()+" sentence no : "+num);
				return;
			}

			EnglishGrammaticalStructure struct = new EnglishGrammaticalStructure(ansTree);
			Collection<TypedDependency> deps = struct.typedDependencies();
			Sentence<TaggedWord> s= ansTree.taggedYield();
			ArrayList<DepLine> lines=getLinesFromSentence(content, s, deps, num, lastSentenceSpanE);
			if(lines==null){				
				return;
			}
			lastSentenceSpanE = lines.get(0).getSpanE();
			writeToFile(lines);
		} 		
		closeStream();
	}

	
    public void processWithoutParsing(File doc, File dep, File senSplit){
		
		
	}
    
    /*
     * SP Traversing Parse Tree
     * 
     */
  
    public ArrayList<DepLine> getLinesFromSentence(String content, Sentence<TaggedWord> sentence, Collection<TypedDependency> deps, int senNum, int lastSentenceSpanE){
    	ArrayList<DepLine> lines=new ArrayList<DepLine>();
    	DepLine line;
    	int index=0;
    	int[] span=null;
   
    	if(sentence.size()>0){
    		line=new DepLine();
    		line.setIndex(senNum);
    		line.setWord(DepLine.SENTENCESTART);
    		lines.add(line);
    	}
    	
    	for (TaggedWord word : sentence){
    		
    		line=new DepLine();
    		line.setIndex(index+1);    		
    		line.setPos(word.tag());
    		line.setLemma(getLemma(word.value(),word.tag()));
    		int type=isPunctuation(word.value());
    		if(type==1){
    			String punc=getFirstPunctuation(content, word.value(), index==0 ? lastSentenceSpanE : lines.get(index).getSpanE());
    			if(punc!=null){
    				line.setWord(punc);
    				span=getSpan(content,punc,index==0 ? lastSentenceSpanE : lines.get(index).getSpanE());
    			}
    			else{
    				System.err.println("Punctuation Mismatch: "+word+" in the sentence --> "+sentence.toString()+". Skipping document!! Try again after fixing the original document!!");
    				return null;
    			}    			
    		}
    		else if(type==2){
    			line.setWord(mapPunctuation(word.value()));
    			span=getSpan(content,line.getWord(),index==0 ? lastSentenceSpanE : lines.get(index).getSpanE());
    			if(span[0]==-1){
    				System.err.println("Punctuation Mismatch: "+word+" in the sentence --> "+sentence.toString()+". Skipping document!! Try again after fixing the original document!!");
    				return null;
    			}
    		}
    		else if(type==3){
    			line.setWord(word.value());
    			span=getSpan(content,word.value(),index==0 ? lastSentenceSpanE : lines.get(index).getSpanE());
    			if(span[0]==-1){
    				System.err.println("Word Mismatch : "+word+" in the sentence --> "+sentence.toString()+". Skipping document!! Try again after fixing the original document!!");
    				return null;
    			}
    		}
    		
    		line.setSpanS(span[0]);
    		line.setSpanE(span[1]);
    		lines.add(line);
    		index++;
    	}
    	
    	for (TypedDependency dep : deps){
    		int dependent=dep.dep().index();
    		int head= dep.gov().index();
    		String type=dep.reln().getShortName();
  			lines.get(dependent).setHead(head);
  			lines.get(dependent).setRelation(type);
    	}
    	
    	for (DepLine l : lines){
    		if(l.getRelation()==null){
    			l.setHead(0);
    			l.setRelation(DUMMYREL);
    		}    		
    	}
    	
    	if(sentence.size()>0){
    		line=new DepLine();
    		line.setIndex(senNum);
    		line.setWord(DepLine.SENTENCEEND);
    		line.setSpanS(lines.get(1).getSpanS());
    		line.setSpanE(lines.get(lines.size()-1).getSpanE());
    		lines.add(line);
    		
    		lines.get(0).setSpanS(line.getSpanS());
    		lines.get(0).setSpanE(line.getSpanE());
    	}
    	
    	return lines;
    }
    
    public int isPunctuation(String s){
    	Matcher m =NONWORD.matcher(s);
    	if(m.matches()){
    		return 1;
    	}
    	if(s.equals(LCB) || s.equals(LRB) || s.equals(LSB) || s.equals(RCB) || s.equals(RRB) || s.equals(RSB)){
    		return 2;
    	}
    	return 3;
    	    	
    }
    
    public String mapPunctuation(String s){
    	if(s.equals(LCB)){
    		return LCBC;
    	}
    	else if(s.equals(LRB)){
    		return LRBC;
    	}
    	else if(s.equals(LSB)){
    		return LSBC;
    	}
    	else if(s.equals(RCB)){
    		return RCBC;
    	}
    	else if(s.equals(RRB)){
    		return RRBC;
    	}
    	else if(s.equals(RSB)){
    		return RSBC;
    	}
    	return s;
    }
    
    
    
    public int[] getSpan(String content, String word, int startingFrom){
    	int[] output=new int[2];
    	 		
    	output[0]=content.indexOf(word, startingFrom);
    	output[1]=output[0]+word.length();
    	
    	return output;
    }
    
    /*
    public String getFirstPunctuation(String content, int startingFrom, int length){
    	int anchor;
    	String output=null;
    		while(Character.isWhitespace(content.charAt(startingFrom))){    			    			
    			startingFrom++;
    		}
    		anchor=startingFrom;
    		for(int i=0;i<length;i++){    			
    			if(content.length()-1<=anchor+i || Character.isLetter(content.charAt(anchor+i)) || Character.isDigit(content.charAt(anchor+i))){
    				break;
        		}    	
    			startingFrom=anchor+i;
    		}
    		output=content.substring(anchor,startingFrom+1);
    		return output;    		
    }
    */
    
    public String getFirstPunctuation(String content, String word, int startingFrom){
    	String output=null;		
    	String sub = content.substring(startingFrom).trim();
    	if(sub.startsWith(word)){
    		output=word;
    	}
    	else{
    		output=sub.substring(0,1);
    	}    	
    	return output;    		
    }
    
    public String getLemma(String word, String pos){
    	String output=null;
    	if(wordnet!=null){
    		String mC=pos.substring(0,1);
    		if(mC.equals(VERB)){
    			output=getLemmaPresentInWordnet(word, SynsetType.VERB);
    		}
    		else if(mC.equals(NOUN)){
    			output=getLemmaPresentInWordnet(word, SynsetType.NOUN);
    		}
    		else if(mC.equals(ADJ)){
    			output=getLemmaPresentInWordnet(word, SynsetType.ADJECTIVE);
    			if(output==null){
    				output=getLemmaPresentInWordnet(word, SynsetType.ADJECTIVE_SATELLITE);
    			}
    		}
    		else if(mC.equals(ADV)){
    			output=getLemmaPresentInWordnet(word, SynsetType.ADVERB);
    		}    		
    	}
    	
    	if(output==null){
    		return Morphology.lemmatizeStatic(new WordTag(word,pos)).lemma();
    	}
    	else{
    		return output;
    	}
    
    }
    
    public String getLemmaPresentInWordnet(String word, SynsetType type){
    	
    
    	String[] candidates;
    	Synset[] synset;

    	if(word.endsWith("ss")){
    		synset=wordnet.getSynsets(word,type);
        	if(synset.length >0){
        		return word;
        	}
        	candidates=wordnet.getBaseFormCandidates(word, type);
        	for (int i = 0; i < candidates.length; i++) {
        		synset=wordnet.getSynsets(candidates[i], type);
        		if(synset.length>0){
        			return candidates[i];
        		}
        	}	
    	}
    	else{
    		candidates=wordnet.getBaseFormCandidates(word, type);
        	for (int i = 0; i < candidates.length; i++) {
        		synset=wordnet.getSynsets(candidates[i], type);
        		if(synset.length>0){
        			return candidates[i];
        		}
        	}		
        	synset=wordnet.getSynsets(word,type);
        	if(synset.length >0){
        		return word;
        	}
    	}
    	    	
    	return null;

    }
    
    
    public void openFileToWrite(File file){

		try {
			rawFile = new FileOutputStream(file);		
			sWriter = new OutputStreamWriter(rawFile,"UTF-8");			
			bWriter = new BufferedWriter(sWriter,2048*10); // default 2048
		}
		catch (Exception e) {
			e.printStackTrace();
			try {
				if(rawFile!=null){
					rawFile.close();	
				}
				if(sWriter!=null){
					sWriter.close();	
				}
				if(bWriter!=null){
					bWriter.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} 
    }
    
    public void closeStream(){
    	
    	try {
    		bWriter.flush();
			if(rawFile!=null){
				rawFile.close();	
			}
			if(sWriter!=null){
				sWriter.close();	
			}
			if(bWriter!=null){
				bWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();			
		}    	
    }

    public void writeToFile(ArrayList<?> lines){

		try {
			for(Object line : lines){	
				bWriter.append(line.toString());
			    bWriter.newLine();
			}			
		} 
		catch (Exception e) {
			e.printStackTrace();
			try {
				if(rawFile!=null){
					rawFile.close();	
				}
				if(sWriter!=null){
					sWriter.close();	
				}
				if(bWriter!=null){
					bWriter.close();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} 
		
    } 
    
    
    public String readDocument(File doc){
		
    	FileInputStream rawFile = null;
        InputStreamReader sReader = null;
        BufferedReader bReader = null;
        StringBuilder builder = new StringBuilder();
        String line;

        try {

            rawFile = new FileInputStream(doc);
            sReader = new InputStreamReader(rawFile, "UTF-8");
            bReader = new BufferedReader(sReader, 4*2048); // default 2048

           
            while ((line = bReader.readLine()) != null) {
                    builder.append(line);
                    builder.append(NEWLINE);                                   
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        } finally {
            try {
                if (rawFile != null) {
                    rawFile.close();
                }
                if (sReader != null) {
                    sReader.close();
                }
                if (bReader != null) {
                    bReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        return builder.toString();
	}
	
}
