import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import entity.Annotation;
import entity.Doc;
import entity.Sentence;


public class PatternOccurrences {

	/**
	 * @param args
	 */
	
	public static String[] patterns = {"amaze","bother","bother","brilliant","brilliant","consume","generous","giant","giant","modest","slight","slight","deaf","promise","simple","nature","sense","feeling","bum","restraint","stress","fatique","spade","blind","collaborate","faithful","fine","fit","free","graceful","natural","oblique","solemn","strike","vital","work","judgment","argument","shelter","difference","difficulty","interest","appear","decide","hot","important","lose","mean","miss","solid"};
	public static String[] AnnotatedWords = {"amaze#v","bother#v","bother#n","brilliant#a","brilliant#n","consume#v","generous#a","giant#a","giant#n","modest#a","slight#a","slight#n","deaf#a","promise#n","simple#a","nature#n","sense#n","feeling#n","bum#n","restraint#n","stress#n","fatique#n","spade#n","blind#a","collaborate#v","faithful#a","fine#a","fit#a","free#a","graceful#a","natural#a","oblique#a","solemn#a","strike#v","vital#a","work#v","judgment#n","argument#n","shelter#n","difference#n","difficulty#n","interest#n","appear#v","decide#v","hot#a","important#a","lose#v","mean#v","miss#v","solid#a"};
	//public static String[] AnnotatedWordPatterns = {"amaze","bother","bother","brilliant","brilliant","consume","generous","giant","giant","modest","slight","slight","deaf","promise","simple","nature","sense","feeling","bum","restraint","stress","fatique","spade","blind","collaborate","faithful","fine","fit","free","graceful","natural","oblique","solemn","strike","vital","work","judgment","argument","shelter","difference","difficulty","interest","appear","decide","hot","important","lose","mean","miss","solid"};
	public static String[] AnnotatedWordPatterns = {"amaze", "bother||v", "bother||n", "brilliant||a","brilliant||n","consume","generous","giant||a","giant||n-giants||n","modest","slight||a","slight||n","deaf","promise||n", "simple","nature","sense||n","feeling||n-feelings||n","bum","restraint","stress||n","fatique","spade","blind","collaborate","faithful||a","fine||a","fit||a","free||a","graceful||a","natural||a","oblique","solemn","strike","vital||a","work||v","judgment","argument","shelter||n","difference||n","difficulty","interests||n-interest||n","appear||","decide||","hot","important||a","lose||v","mean||v","miss||v","solid||a"};
	public static ArrayList<Doc> docs=null;
	
	static{
		try {
			//MaxentTagger.init("/home/cem/tools/stanford-postagger-full-2008-06-06/models/bidirectional-wsj-0-18.tagger");
			System.out.println("Loaded Parser!");
			docs = readObject();
			System.out.println("Read Objects!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
        
	/*
	public static void main(String[] args) {		
		explore();
		//createPPS();				
	}

	public static void createPPS(){
		
		for(int i=0;i<AnnotatedWordPatterns.length;i++){
			System.out.println("Processing Word: "+AnnotatedWords[i]);
			StringBuilder builder = new StringBuilder();
			String pattern=AnnotatedWordPatterns[i];
			String[] ps = pattern.split("\\-");
			for(String p : ps){				
				for(Doc doc : docs){
					for(int j=0;j< doc.getSentences().size();j++){
						Sentence sen= doc.getSentences().get(j);
						HashMap<String,ArrayList<Annotation>> autoAnns = sen.getAutoAnns();			
						if(autoAnns.containsKey("matchfeat")){				
							ArrayList<Annotation> annotations=autoAnns.get("matchfeat");				
							for(Iterator<Annotation> itr_j=annotations.iterator();itr_j.hasNext();){
								Annotation ann=itr_j.next();
								HashMap<String,String> attpairs=ann.getAttributes();
								if(attpairs.containsKey("pattern") && attpairs.get("pattern").startsWith("%"+p)){
									String inst=doc.getDir()+"|"+doc.getFile()+"|"+ann.getSpanS()+"|"+ann.getSpanE();
									builder.append(getPPLine(doc, j, ann.getSpanS(), ann.getSpanE(), inst));
								}
							}
						}
					}
				}
			}
			if(builder.length()>0){
				writePP(AnnotatedWords[i],builder.toString());
			}
		}
				
	}

	
	
	public static void writePP(String word, String content){
		System.out.println("Writing To File: "+"data"+File.separator+"ppEMNLP05"+File.separator+word+".pp");
		
		FileOutputStream rawFile=null;
		OutputStreamWriter sWriter=null;
		BufferedWriter bWriter=null;		
		
		try {
			rawFile = new FileOutputStream("data"+File.separator+"ppEMNLP05"+File.separator+word+".pp");		
			sWriter = new OutputStreamWriter(rawFile,"UTF-8");			
			bWriter = new BufferedWriter(sWriter,4096*10); // default 2048
			bWriter.append(content);
			bWriter.flush();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		finally{
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
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static String getPPLine(Doc doc, int senID, int spanS, int spanE, String id){
		StringBuilder line = new StringBuilder(); 
		
		String word = doc.getRawSpan(spanS, spanE);
		Sentence current = doc.getSentences().get(senID);
		Sentence pre = null;
		Sentence fol = null;
		if(senID>0){
			pre = doc.getSentences().get(senID-1);
		}
		if(senID<doc.getSentences().size()-1){
			fol = doc.getSentences().get(senID+1);
		}
		
		// find previous occurrences of the word in context
		int index=0;
		if(pre!=null){
		for(Annotation a : pre.getGatedefaultAnns().get("GATE_Token")){
			if(a.getAttributes().get("string").equals(word)){
				index++;
			}
		}
		}
		
		for(Annotation a : current.getGatedefaultAnns().get("GATE_Token")){
			if(a.getAttributes().get("string").equals(word)){
				if(a.getSpanS()<spanS){
					index++;
				}
				else{
					break;
				}
			}
		}
		
		//postag context
		int position=0;
		edu.stanford.nlp.ling.Sentence<TaggedWord> taggedWords = posTagContext(doc.getRawSpan(pre == null ? current.getSpanS() : pre.getSpanS(), fol== null ? current.getSpanE() : fol.getSpanE()));
		for(int i=0;i<taggedWords.size();i++){
			if(taggedWords.get(i).word().equals(word)){
				if(index==0){
					position=i;
					break;
			    }
				else{
					index--;
				}
			}			
		}
		
		
		line.append(id);
		line.append(" ");
		line.append(word);
		line.append(" ");
		line.append("?");
		line.append(" ");
		line.append(position);
		
		for(TaggedWord t : taggedWords){
			line.append(" ");
			line.append(t.word());
			line.append("/");
			line.append(t.tag());
		}
		
		line.append("\n");
		
		return line.toString();
	}
	
	
	public static edu.stanford.nlp.ling.Sentence<TaggedWord> posTagContext(String context){
		edu.stanford.nlp.ling.Sentence<TaggedWord> output = new edu.stanford.nlp.ling.Sentence<TaggedWord>(); 
		List<edu.stanford.nlp.ling.Sentence<TaggedWord>> sentences = MaxentTagger.tokenizeText(new StringReader(context));
		for(int i=0;i<sentences.size();i++){
			output.addAll(MaxentTagger.tagSentence(sentences.get(i)));
		}
		return output;
	}
	
	
	public static void explore(){
		ArrayList<Doc> docs=null;
		try{
			docs = readObject();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		TreeMap<String,Integer> counts = new TreeMap<String, Integer>();
		TreeMap<String,TreeSet<String>> origpaths = new TreeMap<String, TreeSet<String>>();
		
		for(Doc doc : docs){
			for(Sentence sen : doc.getSentences()){
				HashMap<String,ArrayList<Annotation>> autoAnns = sen.getAutoAnns();
				String senID=doc.getDir()+"-"+doc.getFile()+"-"+sen.getSpanS()+"-"+sen.getSpanE()+" wc:"+sen.getWeakCount()+" sc:"+sen.getStrongCount()+" spc:"+sen.getSubjPatternCount()+" opc:"+sen.getObjPatternCount();
				int sac=0;				
				if(autoAnns.containsKey("matchfeat")){				
					ArrayList<Annotation> annotations=autoAnns.get("matchfeat");				
					for(Iterator<Annotation> itr_j=annotations.iterator();itr_j.hasNext();){
						Annotation ann=itr_j.next();
						HashMap<String,String> attpairs=ann.getAttributes();
						if(attpairs.containsKey("pattern")){
							for(int i=0;i<AnnotatedWordPatterns.length;i++){
								String pattern=AnnotatedWordPatterns[i];
								String[] ps = pattern.split("\\-");
								for(String p : ps){
									//if(attpairs.get("pattern").startsWith("%"+patterns[i])){
									if(attpairs.get("pattern").startsWith("%"+p)){
										sac++;	
										if(!origpaths.containsKey(AnnotatedWords[i])){
											origpaths.put(AnnotatedWords[i], new TreeSet<String>());
											counts.put(AnnotatedWords[i], 0);
										}
										origpaths.get(AnnotatedWords[i]).add(attpairs.get("pattern"));
										counts.put(AnnotatedWords[i],counts.get(AnnotatedWords[i]).intValue()+1);
									}					
								}
							}

						}
					}
				}
				if(sac>0){
					System.out.println(senID+"found: "+sac);
				}
			}
		}
		
		
		System.out.println(origpaths);
		System.out.println(counts);
		System.out.println(counts.size());
		

		for(int index=120;index>0;index--){
		for(String key : counts.keySet()){
			int count = counts.get(key);
			if(count==index){
				System.out.println(key+"\t"+counts.get(key));
			}			
		}
		}
	}
        */
	
	
	public static void persistObject(ArrayList<Doc> docs) throws Exception{
		
		 ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("docs.dat"));
		 oos.writeObject(docs);
		 oos.close();
		
	}
	
	public static ArrayList<Doc> readObject() throws Exception{
		
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/home/cem/workspace/DATA/corpus/docsCompleteEMNLP05OldTerminology.dat"));
		ArrayList<Doc> docs= (ArrayList<Doc>)ois.readObject();		
		ois.close();
		return docs;
		
	}
}
