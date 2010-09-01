package opin.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import opin.featurefinder.entity.*;

public class Writer {

	
	
	public static void writeInstancesToFile(File instanceFile, ArrayList<Match> matches){
		
		FileOutputStream rawFile=null;
		OutputStreamWriter sWriter=null;
		BufferedWriter bWriter=null;
		
		System.out.println(instanceFile.getAbsolutePath());
		Match match;
		
		try {
			rawFile = new FileOutputStream(instanceFile);		
			sWriter = new OutputStreamWriter(rawFile,"UTF-8");			
			bWriter = new BufferedWriter(sWriter,2048*5); // default 2048
			for(Iterator<Match> itr=matches.iterator();itr.hasNext();){	
				match=itr.next();
				bWriter.append(match.toString());
			    bWriter.newLine();
			    System.out.println(match.toString());
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
	
	public static void persistDictionaryDefinition(File outputFile, Object lexicon,String context, String xsdLocation) {
        try {
            JAXBContext jc = JAXBContext.newInstance(context);
            Marshaller marshaller = jc.createMarshaller();                        
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true)); 
            if(xsdLocation!=null){
            	marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, xsdLocation);
            }
            marshaller.marshal(lexicon, new FileOutputStream(outputFile));
            
        } catch (JAXBException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
	
}
