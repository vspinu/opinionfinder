package logic;

import entity.Annotation;
import config.Config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;

public class AnnotationHandler {	
	
	public static ArrayList<Annotation> readAnns(File annotationFile){
		
		FileInputStream rawFile=null;
		InputStreamReader sReader=null;
		BufferedReader bReader=null;
		String line;
		String[] tokens;
		String[] parts;
		ArrayList<Annotation> output=new ArrayList<Annotation>();
		Annotation currentAnn;
		Matcher matcher;
		
		if(!annotationFile.exists()){
			System.err.println("Annotation File does not exist: "+annotationFile.getAbsolutePath());
			System.exit(-1);
		}
		
		try {
			rawFile = new FileInputStream(annotationFile);		
			sReader = new InputStreamReader(rawFile,"UTF-8");			
			bReader = new BufferedReader(sReader,8192); // default 2048
			while((line=bReader.readLine())!=null){
				line=line.trim();
				if(!(line.equals("") || line.startsWith("#"))){					
					tokens=Config.LINETOKENIZER.split(line);
					//tokens=line.split("\t");
					parts=tokens[1].split(",");
					for(int i=0;i<tokens[0].length();i++){
						if(Character.isDigit(tokens[0].charAt(i))){
							tokens[0]=tokens[0].substring(i);
						}
					}
					currentAnn=new Annotation(tokens[3],Integer.parseInt(tokens[0]),Integer.parseInt(parts[0]),Integer.parseInt(parts[1]));
					for(int i=4;i<tokens.length;i++){
						matcher=Config.ATTRIBUTETOKENIZER.matcher(tokens[i]);
	                    if(matcher.matches()){
	                    	currentAnn.addToAttributes(matcher.group(1),matcher.group(2));
						}
						else{
							System.err.println("Attribute is not well formed!"+" In file:"+annotationFile.getAbsolutePath()+" at "+tokens[i]);
						}						
					}
					output.add(currentAnn);
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
