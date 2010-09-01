package logic;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


import entity.Doc;
import entity.Annotation;
import entity.Sentence;
import config.Config;



public class DocHandler {

	
	
	public static void fillDocWithSentences(Doc doc) {
		
		ArrayList<Annotation> manAnns=AnnotationHandler.readAnns(doc.getCorrespondingManAnn());
		sortAnnsBySpanS(manAnns);
		ArrayList<Sentence> sentences= SentenceHandler.createSentencesFromManAnns(manAnns,doc.getDocID());
		SentenceHandler.fillSentencesWithManAnns(manAnns, sentences);

		if(Config.READAUTOANN){
			ArrayList<Annotation> autoAnns = new ArrayList<Annotation>();
			for(File f : doc.getCorrespondingAutoAnns()){
				autoAnns.addAll(AnnotationHandler.readAnns(f));
			}			
			sortAnnsBySpanS(autoAnns);
			SentenceHandler.fillSentencesWithAutoAnns(autoAnns, sentences);
		}
		if(Config.READGATEDEFAULT){
			ArrayList<Annotation> gatedefaultAnns=AnnotationHandler.readAnns(doc.getCorrespondingGateDefault());			
			sortAnnsBySpanS(gatedefaultAnns);
			SentenceHandler.fillSentencesWithGatedefaultAnns(gatedefaultAnns, sentences);
		}
		
		doc.setSentences(sentences);
	}
	

	
	public static ArrayList<Doc> readDocList(File doclistFile){
		FileInputStream rawFile=null;
		InputStreamReader sReader=null;
		BufferedReader bReader=null;
		ArrayList<Doc> output = new ArrayList<Doc>();
		
		try {
			rawFile = new FileInputStream(doclistFile);		
			sReader = new InputStreamReader(rawFile,"UTF-8");			
			bReader = new BufferedReader(sReader); // default 2048
			
			String line;
			int id=1;
			while((line=bReader.readLine())!=null){
				line=line.trim();
				if(!line.equals("")){
					File file=new File(line);
					String f=file.getName();
					String d=file.getParentFile().getName();
					File newFile=new File(Config.DATABASE.getAbsolutePath()+File.separator+Config.DOCDIRECTORY+File.separator+d+File.separator+f);
					Doc doc= new Doc(readRawDoc(newFile));
					doc.setDir(d);
					doc.setFile(f);
					doc.setCorrespondingRawFile(newFile);
					if(Config.READAUTOANN){
						for(int i=0;i<Config.AUTOANNFILES.length;i++){
							doc.addToCorrespondingAutoAnns(new File(Config.DATABASE.getAbsolutePath()+File.separator+Config.AUTOANNDIRECTORY+File.separator+d+File.separator+f+File.separator+Config.AUTOANNFILES[i]));
						}
					}
					if(Config.READGATEDEFAULT){
						doc.setCorrespondingGateDefault(new File(Config.DATABASE.getAbsolutePath()+File.separator+Config.AUTOANNDIRECTORY+File.separator+d+File.separator+f+File.separator+Config.GATEDEFAULT));
					}
					//if(Config.READMANANN){
						doc.setCorrespondingManAnn(new File(Config.DATABASE.getAbsolutePath()+File.separator+Config.MANANNDIRECTORY+File.separator+d+File.separator+f+File.separator+Config.MANANNFILE));
					//}
					if(Config.WRITEHP){
						doc.setCorrespondingSentClassFile(new File(Config.DATABASE.getAbsolutePath()+File.separator+Config.AUTOANNDIRECTORY+File.separator+d+File.separator+f+File.separator+Config.AUTOCLASSFILE));
					}
					if(Config.WRITEGOLD){
						doc.setCorrespondingGoldClassFile(new File(Config.DATABASE.getAbsolutePath()+File.separator+Config.AUTOANNDIRECTORY+File.separator+d+File.separator+f+File.separator+Config.GOLDCLASSFILE));
					}
					if(Config.WRITESPLIT){
						doc.setCorrespondingSentSplitFile(new File(Config.DATABASE.getAbsolutePath()+File.separator+Config.AUTOANNDIRECTORY+File.separator+d+File.separator+f+File.separator+Config.SENTENCESPLITFILE));
					}
					doc.setDocID(id);
					output.add(doc);
					id++;
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
	
	public static String readRawDoc(File doc){
		FileInputStream rawFile=null;
		InputStreamReader sReader=null;
		BufferedReader bReader=null;
		
		
		StringBuilder builder=new StringBuilder();
		
		if(!doc.exists()){
			System.err.println("Doc File does not exist: "+doc.getAbsolutePath());
			System.exit(-1);
		}
		
		try {
			rawFile = new FileInputStream(doc);		
			sReader = new InputStreamReader(rawFile);
			bReader = new BufferedReader(sReader,4096*10);
			
			String line;
			while((line=bReader.readLine())!=null){
				builder.append(line);
				builder.append("\n");
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
				if(bReader!=null){
					bReader.close();	
				}
				if(sReader!=null){
					sReader.close();	
				}
				if(rawFile!=null){
					rawFile.close();	
				}				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return builder.toString();
	}
	
	
	public static void sortAnnsBySpanS(ArrayList<Annotation> anns){
    	Collections.sort(anns,new AnnComparator());
    }
        
	static class AnnComparator implements Comparator<Annotation> { 
		public int compare(Annotation a1,Annotation a2){ 
			return a1.getSpanS()-a2.getSpanS(); 
		} 
	}
	
	
	
	
}
