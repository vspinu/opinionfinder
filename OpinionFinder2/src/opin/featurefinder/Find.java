package opin.featurefinder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import opin.featurefinder.entity.*;
import opin.io.*;

public class Find {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Matcher matcher = new Matcher();

		File dictFile=null;
		File attFile=null;
		File doclistFile=null;
		File expansionFile=null;
		String type = null;

		if(args.length==5){
			dictFile=new File(args[0]);
			attFile = new File(args[1]);
			doclistFile=new File(args[2]);
			expansionFile=new File(args[3]);
			type=args[4];
						
			
			if(!dictFile.exists()){
				System.out.println("Dictionary does not exist!");
				System.exit(-1);					
			}
			if(!attFile.exists()){
				System.out.println("Attribute file does not exist!");
				System.exit(-1);					
			}
			if(!doclistFile.exists()){
				System.out.println("Doclist does not exist!");
				System.exit(-1);
			}
			if(!expansionFile.exists()){
				System.out.println("Expansion does not exist!");
				System.exit(-1);
			}
			if(!type.equals("tff") && !type.equals("ruppenhofer")){
				System.out.println("Unknown attribute type!");
				System.exit(-1);
			}
		}
		else{
			System.out.println("Wrong number of arguments !");
			System.exit(-1);			
		}
	
		String instanceFileName=dictFile.getName().split("\\.")[0];
		
		
		ArrayList<File> docs = new ArrayList<File>();
		ArrayList<File> deps = new ArrayList<File>();
		ArrayList<File> senSplits = new ArrayList<File>();
		ArrayList<File> instances = new ArrayList<File>();
		Reader.readDocList(doclistFile, instanceFileName, docs, deps, senSplits, instances);
		
		ArrayList<Entry> patterns = null;
		if(type.equals("tff")){
			patterns = Reader.createPatternsFromLexiconWithTffAttributes(dictFile,attFile);
		}
		else if(type.equals("ruppenhofer")){
			patterns = Reader.createPatternsFromLexiconWithRuppenhoferAttributes(dictFile,attFile);
		}
		
		HashMap<String, HashSet<String>> expansions=Reader.readExpansionFile(expansionFile);
		matcher.setExpansions(expansions);
		
		ArrayList<Sentence> sentences;
		ArrayList<Match> matches;
		for (int i=0;i<docs.size();i++) {
			File depFile=deps.get(i);
			
			if(depFile.exists()){
				matches=new ArrayList<Match>(); 
				sentences=Reader.createSentencesFromDepFile(depFile);
				for (Iterator<Sentence> itr = sentences.iterator();itr.hasNext();) {
					matcher.setSentence(itr.next());
					//System.out.println(j++);
					for (Iterator<Entry> itrP = patterns.iterator(); itrP.hasNext();) {
						matcher.setPattern(itrP.next());
						matcher.match(matches);
					}					
				}
				Writer.writeInstancesToFile(instances.get(i), matches);
			}
			else{
				System.out.println("Dependency file does not exist: "+depFile.getAbsolutePath());
			}			
		}

	}

}
