package logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import config.Config;

import entity.Doc;
import entity.FeatureVector;
import entity.Sentence;

public class FeatureHandler {

	ArrayList<FeatureVector> features = new ArrayList<FeatureVector>();
	
	
	public void storeFeatures(Doc doc){
		
		ArrayList<Sentence> sentences=doc.getSentences();
		
				
		if(sentences.size()>1){
			features.add(addFeatureVector(sentences.get(0), null, sentences.get(1), doc));		
			for(int i=1;i<sentences.size()-1;i++){
				features.add(addFeatureVector(sentences.get(i), sentences.get(i-1), sentences.get(i+1), doc));
			}
			features.add(addFeatureVector(sentences.get(sentences.size()-1), sentences.get(sentences.size()-2), null, doc));
		}
		else{
			features.add(addFeatureVector(sentences.get(0), null, null, doc));
		}
	}
	
	public FeatureVector addFeatureVector(Sentence sen, Sentence pre, Sentence fol, Doc doc){
		FeatureVector output= new FeatureVector();
		
		output.setDir(doc.getDir());
		output.setDocument(doc.getFile());
		output.setSpanS(sen.getSpanS());
		output.setSpanE(sen.getSpanE());
		
		output.setStrong(sen.getStrongCount());
		output.setWeak(sen.getWeakCount());
		output.setPatternsubj(sen.getSubjPatternCount());
		output.setPatternobj(sen.getObjPatternCount());
		
		output.setAdjective(sen.getAdjective().size());
		output.setAdverb(sen.getAdverb().size());
		output.setModal(sen.getModal().size());
		output.setPronoun(sen.getPronoun().size());
		output.setCardinal(sen.getCardinal().size());
		
		if(pre!=null){
			output.setStrongA(pre.getStrongCount());
			output.setWeakA(pre.getWeakCount());
			output.setPatternsubjA(pre.getSubjPatternCount());
			output.setPatternobjA(pre.getObjPatternCount());

			output.setAdjectiveA(pre.getAdjective().size());
			output.setAdverbA(pre.getAdverb().size());
			output.setModalA(pre.getModal().size());
			output.setPronounA(pre.getPronoun().size());
			output.setCardinalA(pre.getCardinal().size());
		}

		if(fol!=null){
			output.setStrongA(output.getStrongA()+fol.getStrongCount());
			output.setWeakA(output.getWeakA()+fol.getWeakCount());
			output.setPatternsubjA(output.getPatternsubjA()+fol.getSubjPatternCount());
			output.setPatternobjA(output.getPatternobjA()+fol.getObjPatternCount());

			output.setAdjectiveA(output.getAdjectiveA()+fol.getAdjective().size());
			output.setAdverbA(output.getAdverbA()+fol.getAdverb().size());
			output.setModalA(output.getModalA()+fol.getModal().size());
			output.setPronounA(output.getPronounA()+fol.getPronoun().size());
			output.setCardinalA(output.getCardinalA()+fol.getCardinal().size());
		}
		
		output.setC(sen.getGoldC());
		return output;
	}
	
	public void writeArffNominalFile(File f) {
		FileOutputStream rawFile=null;
		OutputStreamWriter sWriter=null;
		BufferedWriter bWriter=null;	
		
		try {
			rawFile = new FileOutputStream(f);		
			sWriter = new OutputStreamWriter(rawFile,"UTF-8");			
			bWriter = new BufferedWriter(sWriter,4096*20); // default 2048
			bWriter.append(Config.ARFFHEADERNOMINAL);
			bWriter.newLine();
			for(FeatureVector fv : features){				
				bWriter.append(fv.toArffNominalString());
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
	
	public void writeArffNumericFile(File f) {
		FileOutputStream rawFile=null;
		OutputStreamWriter sWriter=null;
		BufferedWriter bWriter=null;	
		
		try {
			rawFile = new FileOutputStream(f);		
			sWriter = new OutputStreamWriter(rawFile,"UTF-8");			
			bWriter = new BufferedWriter(sWriter,4096*20); // default 2048
			bWriter.append(Config.ARFFHEADERNUMERIC);
			bWriter.newLine();
			for(FeatureVector fv : features){				
				bWriter.append(fv.toArffNumericString());
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
	
	public void writeSparseFile(File f) {
		FileOutputStream rawFile=null;
		OutputStreamWriter sWriter=null;
		BufferedWriter bWriter=null;	
		
		try {
			rawFile = new FileOutputStream(f);		
			sWriter = new OutputStreamWriter(rawFile,"UTF-8");			
			bWriter = new BufferedWriter(sWriter,4096*20); // default 2048
			for(FeatureVector fv : features){				
				bWriter.append(fv.toSparseNumericString());
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
}
