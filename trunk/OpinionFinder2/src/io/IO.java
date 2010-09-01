package io;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;


import entity.Sentence;
import entity.Doc;
import entity.Span;
import config.Config;



 
public class IO {
	
	
	public IO() {
		
	}
	

	public static void writeGoldClassification(Doc doc) {
		FileOutputStream rawFile=null;
		OutputStreamWriter sWriter=null;
		BufferedWriter bWriter=null;
		ArrayList<Sentence> sentences=doc.getSentences();
		Sentence sen;		
		
		try {
			rawFile = new FileOutputStream(doc.getCorrespondingGoldClassFile());		
			sWriter = new OutputStreamWriter(rawFile,"UTF-8");			
			bWriter = new BufferedWriter(sWriter,4096); // default 2048
			for(Iterator<Sentence> itr=sentences.iterator();itr.hasNext();){
				sen=itr.next();				
				bWriter.append(sen.getGOutputString());
				bWriter.newLine();
			}
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

	public static void writeHpClassification(Doc doc) {
		FileOutputStream rawFile=null;
		OutputStreamWriter sWriter=null;
		BufferedWriter bWriter=null;
		ArrayList<Sentence> sentences=doc.getSentences();
		Sentence sen;		
		
		try {
			rawFile = new FileOutputStream(doc.getCorrespondingSentClassFile());		
			sWriter = new OutputStreamWriter(rawFile,"UTF-8");			
			bWriter = new BufferedWriter(sWriter,4096); // default 2048
			for(Iterator<Sentence> itr=sentences.iterator();itr.hasNext();){
				sen=itr.next();				
				bWriter.append(sen.getHOutputString());
				bWriter.newLine();
			}
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
	
	
	public static void writeSentenceSplit(Doc doc) {
		FileOutputStream rawFile=null;
		OutputStreamWriter sWriter=null;
		BufferedWriter bWriter=null;
		ArrayList<Sentence> sentences=doc.getSentences();
		Sentence sen;		
		
		try {
			rawFile = new FileOutputStream(doc.getCorrespondingSentSplitFile());		
			sWriter = new OutputStreamWriter(rawFile,"UTF-8");			
			bWriter = new BufferedWriter(sWriter,4096); // default 2048
			for(Iterator<Sentence> itr=sentences.iterator();itr.hasNext();){
				sen=itr.next();				
				bWriter.append(sen.getSplitString());
				bWriter.newLine();
			}
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
	


	public static void writeLog(Doc doc,boolean append) {
		FileOutputStream rawFile=null;
		OutputStreamWriter sWriter=null;
		BufferedWriter bWriter=null;
		//HashMap<String,String> attpairs;
		ArrayList<Sentence> sentences=doc.getSentences();
		Sentence current;
		
		try {
			rawFile = new FileOutputStream(Config.LOGFILE,append);		
			sWriter = new OutputStreamWriter(rawFile,"UTF-8");			
			bWriter = new BufferedWriter(sWriter,4096*5); // default 2048
			bWriter.append("************************************************************");
			bWriter.newLine();
			for(int i=0;i<sentences.size();i++){
				current=sentences.get(i);
				bWriter.append("document="+doc.getCorrespondingRawFile().getParentFile().getName()+File.separator+doc.getCorrespondingRawFile().getName()+" ");
				bWriter.append("span="+current.getSpanS()+","+current.getSpanE()+" ");
				bWriter.append("strongcount="+current.getStrongCount()+" ");
				bWriter.append("weakcount="+current.getWeakCount()+" ");
				bWriter.append("subjpattern="+current.getSubjPatternCount()+" ");
				bWriter.append("objpattern="+current.getObjPatternCount()+" ");
				bWriter.append("modal="+current.getModal().size()+" ");
				bWriter.append("pronoun="+current.getPronoun().size()+" ");
				bWriter.append("cardinal="+current.getCardinal().size()+" ");
				bWriter.append("adj="+current.getAdjective().size()+" ");
				bWriter.append("adverb="+current.getAdverb().size()+" ");
				bWriter.append("HpC="+current.getHpC()+" ");
				bWriter.append("GC="+current.getGoldC()+" ");
				
				
				TreeMap<Span,LinkedList<String>> clues = current.getClues();
				
				
				for(Span s : clues.keySet()){
					bWriter.newLine();
		    		bWriter.append(doc.getRawSpan(s.getStart(), s.getEnd())+" ");
		    		bWriter.append(s.getStart()+","+s.getEnd()+" ");
		    		bWriter.append(clues.get(s).toString());
				}
				
				bWriter.newLine();
				bWriter.newLine();
			}
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


	public static void readExcludedSpans(File f, TreeMap<String, ArrayList<Span>> excludedSpans, TreeMap<String, ArrayList<Span>> consideredSpans) {
		FileInputStream rawFile=null;
		InputStreamReader sReader=null;
		BufferedReader bReader=null;
		String line;
		String[] tokens;
		
		
		try {
			rawFile = new FileInputStream(f);		
			sReader = new InputStreamReader(rawFile,"UTF-8");			
			bReader = new BufferedReader(sReader,2048*16); // default 2048
			while((line=bReader.readLine())!=null){
				line=line.trim();
				if(!line.equals("")){			
					String[] parts = line.split("\t");
					
					tokens=parts[0].split("\\|");
					String key=tokens[0]+"|"+tokens[1];
					Span s = new Span(Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]));
					s.setType(parts[1]);
					
					if(!consideredSpans.containsKey(key)){
						consideredSpans.put(key, new ArrayList<Span>());
					}	
					consideredSpans.get(key).add(s);
					
					if(parts[1].equals("obj")){
						if(!excludedSpans.containsKey(key)){
							excludedSpans.put(key, new ArrayList<Span>());
						}	
						excludedSpans.get(key).add(s);						
					}
				}				
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
	
	
	public static TreeMap<String,ArrayList<Span>> readSubjSpans(File f) {
		FileInputStream rawFile=null;
		InputStreamReader sReader=null;
		BufferedReader bReader=null;
		TreeMap<String,ArrayList<Span>> output= new TreeMap<String,ArrayList<Span>>(); 
		String line;
		String[] tokens;
		
		
		try {
			rawFile = new FileInputStream(f);		
			sReader = new InputStreamReader(rawFile,"UTF-8");			
			bReader = new BufferedReader(sReader,2048*16); // default 2048
			while((line=bReader.readLine())!=null){
				line=line.trim();
				if(!line.equals("")){			
					String[] parts = line.split("\t");					
					tokens=parts[0].split("\\|");
					String key=tokens[0]+"|"+tokens[1];
					Span s = new Span(Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]));
					s.setType(parts[1]);
					
					if(!output.containsKey(key)){
						output.put(key, new ArrayList<Span>());
					}	
					output.get(key).add(s);
				}				
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
		
		return output;
		
	}

	
	public static TreeMap<String,ArrayList<Span>> readPolSpans(File f) {
		FileInputStream rawFile=null;
		InputStreamReader sReader=null;
		BufferedReader bReader=null;
		TreeMap<String,ArrayList<Span>> output= new TreeMap<String,ArrayList<Span>>(); 
		String line;
		String[] tokens;
		
		
		try {
			rawFile = new FileInputStream(f);		
			sReader = new InputStreamReader(rawFile,"UTF-8");			
			bReader = new BufferedReader(sReader,2048*16); // default 2048
			while((line=bReader.readLine())!=null){
				line=line.trim();
				if(!line.equals("")){			
					String[] parts = line.split("\t");					
					tokens=parts[0].split("\\|");
					String key=tokens[0]+"|"+tokens[1];
					Span s = new Span(Integer.parseInt(tokens[2]),Integer.parseInt(tokens[3]));
					s.setPol(parts[1]);
					
					if(!output.containsKey(key)){
						output.put(key, new ArrayList<Span>());
					}	
					output.get(key).add(s);
				}				
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
		
		return output;
		
	}
	
}
