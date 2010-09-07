package opin.entity;

import java.io.File;
import java.util.ArrayList;

import opin.io.ReaderUtils;

public class Corpus {
	
	ArrayList<File> docs;
	

	
	public ArrayList<File> getDocs() {
		return docs;
	}


	public void setDocs(ArrayList<File> docs) {
		this.docs = docs;
	}


	public Corpus(File doclistF) {
		this.docs=new ArrayList<File>();
		ArrayList<String> lines = ReaderUtils.readLinesIgnoreEmpty(doclistF);
		for(String l :lines){
			File doc = new File(l);
			File autoannsBase = new File(doc.getAbsolutePath().replace("docs","auto_anns"));
			if(!autoannsBase.exists()){
				autoannsBase.mkdirs();
			}
			docs.add(new File(l));			
		}	
	}
	
	// note: "/docs/" must be somewhere in the path for "doc", else
        //  this method wil crash & burn !!!
	public static File getAnnotationFile(String name, File doc){		
		String docName=doc.getName();
		String autoannsBase = doc.getParent().replace("docs","auto_anns");
		return new File(autoannsBase+File.separator+docName+File.separator+name);
	}
	
	
}
