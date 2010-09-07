package opin.io;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class ReaderUtils {
	
	public static ArrayList<String> readLines(File f){
		
		FileInputStream rawFile=null;
		InputStreamReader sReader=null;
		BufferedReader bReader=null;
		
		ArrayList<String> output = new ArrayList<String>();
				
		try {
			String line;
			rawFile = new FileInputStream(f);		
			sReader = new InputStreamReader(rawFile,"UTF-8");			
			bReader = new BufferedReader(sReader,4096*100); // default 2048
			while((line=bReader.readLine())!=null){
				output.add(line);				
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
	
	public static ArrayList<String> readLinesIgnoreEmpty(File f){
		
		FileInputStream rawFile=null;
		InputStreamReader sReader=null;
		BufferedReader bReader=null;
		
		ArrayList<String> output = new ArrayList<String>();
				
		try {
			String line;
			rawFile = new FileInputStream(f);		
			sReader = new InputStreamReader(rawFile,"UTF-8");			
			bReader = new BufferedReader(sReader,4096*100); // default 2048
			while((line=bReader.readLine())!=null){
				line=line.trim();
				if(!line.equals("")){			
					output.add(line);
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
	
	public static ArrayList<String> readLinesIgnoreEmptyAndComments(File f){
		
		FileInputStream rawFile=null;
		InputStreamReader sReader=null;
		BufferedReader bReader=null;
		
		ArrayList<String> output = new ArrayList<String>();
				
		try {
			String line;
			rawFile = new FileInputStream(f);		
			sReader = new InputStreamReader(rawFile,"UTF-8");			
			bReader = new BufferedReader(sReader,4096*100); // default 2048
			while((line=bReader.readLine())!=null){
				line=line.trim();
				if(!line.equals("") && !line.startsWith("#")){			
					output.add(line);
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
	
	
	public static void emptyBuffer(Process p, boolean show){
		String line;
		BufferedReader br;
		try{			
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			if(show){
				while ( (line = br.readLine()) != null){System.out.println(line);}
			}
			else{
				while ( (line = br.readLine()) != null){}
			}
			br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			if(show){
				while ( (line = br.readLine()) != null){System.out.println(line);}
			}
			else{
				while ( (line = br.readLine()) != null){}
			}
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
        
	}
	

}
