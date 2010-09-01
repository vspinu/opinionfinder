package opin.io;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import opin.dictionary.dictDef.*;
import opin.featurefinder.entity.*;

public class Reader {
	
	
	
	//private final static String J="J";
	//private final static String V="V";
	//private final static String N="N";
	//private final static String R="R";
	private final static String TAB="\t";
	private final static String EMPTY="";
	private final static String SENS="|S|";
	private final static String SENE="|/S|";
	private final static String SENT="SENT";
	private final static String SENTENCESTART="<tree>";
	private final static String SENTENCEEND="</tree>";
	private final static String EXPSPLITTER=",";
	private final static int POSITION=0;
	private final static int WORDFORM=1;
	private final static int HEAD=2;
	private final static int RELTYPE=3;
	private final static int POS=4;
	//private final static int SOMETHING=5;
	private final static int LEMMA=5;
	private final static int SPANS=6;
	private final static int SPANE=7;
	private final static String P="P";
	private final static String PP="PP";
	private final static String PP$="PP$";
	private final static String WP="WP";
	
	private final static String DEPFILEEXTENSION=".dep";
	private final static String SENSPLITEXTENSION=".sentsplit";
	
	private final static java.util.regex.Pattern EXPREGEX = java.util.regex.Pattern.compile("(@.+?)=\\{(.+?)\\}");
	
	
	
	public static ArrayList<Entry> createPatternsFromLexicon(File dictFile){
		Lexicon lex = (Lexicon)readDictFile(dictFile, "opin.dictionary.dictDef");

		ArrayList<Entry> output=new ArrayList<Entry>();
		Entry p;
		Element e;
		ElementType et;
		OrderRelation r;
		DependencyRelation d;
		MorphoSyntaxType m;
		List<MorphoSyntaxType> ms;
		RelationBaseType relation;
		Order order;
		Syntactic syn;
		List<ElementType> elements;
		List<RelationBaseType> relations;
		List<EntryBaseType> entries = lex.getEntry();
		
		
		WordType word;
		LemmaType lemma;
		PosType pos;
		MajorClassType mC;

		
		for (Iterator<EntryBaseType> iterator = entries.iterator(); iterator.hasNext();) {
			
			EntryBaseType entry =  iterator.next();
			
			if(entry.getClass().equals(Sw.class)){	
				p = new Entry(entry.getId(),true);
				m=((Sw)entry).getEntryDefinition().getMorphoSyntax();
				
				word=m.getWord();
				lemma=m.getLemma();
				pos=m.getPos();
				mC=m.getMC();
				
				MorphoSyntax morpho=new MorphoSyntax();
				
				if(word!=null){
					morpho.setWord(word.getValue());
					morpho.setWordForce(word.getForce()==ForceType.POSITIVE);
				}
				if(lemma!=null){
					morpho.setLemma(lemma.getValue());
					morpho.setLemmaForce(lemma.getForce()==ForceType.POSITIVE);
				}
				if(pos!=null){
					morpho.setPos(pos.getValue());
					morpho.setPosForce(pos.getForce()==ForceType.POSITIVE);	
				}
				if(mC!=null){
					morpho.setMC(mC.getValue().value());
					morpho.setMCForce(mC.getForce()==ForceType.POSITIVE);
				}
				
				
				e=new Element(1);
				e.add(morpho);
				e.setForce(true);
				p.addElement(e);
				
			}
			else{
				p = new Entry(entry.getId(),false);
				elements=((Mw)entry).getEntryDefinition().getElement();
				
				CompositionType comp = ((Mw)entry).getEntryDefinition().getComposition();
				if(comp!=null){
					relations=((Mw)entry).getEntryDefinition().getComposition().getRelation();
				}
				else{
					relations=new ArrayList<RelationBaseType>();
				}
				
				
				for (Iterator<ElementType> itrE = elements.iterator(); itrE.hasNext();) {
					et=itrE.next();
					ms = et.getMorphoSyntax();
					e=new Element(ms.size());
					
					for (Iterator<MorphoSyntaxType> itrM = ms.iterator(); itrM.hasNext();) {
						m= itrM.next();
						word=m.getWord();
						lemma=m.getLemma();
						pos=m.getPos();
						mC=m.getMC();
						
						MorphoSyntax morpho=new MorphoSyntax();
						if(word!=null){
							morpho.setWord(word.getValue());
							morpho.setWordForce(word.getForce()==ForceType.POSITIVE);
						}
						if(lemma!=null){
							morpho.setLemma(lemma.getValue());
							morpho.setLemmaForce(lemma.getForce()==ForceType.POSITIVE);
						}
						if(pos!=null){
							morpho.setPos(pos.getValue());
							morpho.setPosForce(pos.getForce()==ForceType.POSITIVE);	
						}
						if(mC!=null){
							morpho.setMC(mC.getValue().value());
							morpho.setMCForce(mC.getForce()==ForceType.POSITIVE);
						}
						
						
						e.add(morpho);
					}
					
					
					e.setForce(et.getForce()==ForceType.POSITIVE);
					p.addElement(e);
					
				}
				
				for (Iterator<RelationBaseType> itrE = relations.iterator(); itrE.hasNext();) {
					relation = itrE.next();
					if(relation.getClass().equals(Order.class)){
						order=(Order)relation;
						r=new OrderRelation(order.getPre()-1,order.getFol()-1, order.getForce()==ForceType.POSITIVE);
						if(order.getEqual()!=null){
							r.setEqual(order.getEqual());
						}
						else{
							if(order.getMax()!=null){
								r.setMax(order.getMax());
							}
							if(order.getMin()!=null){
								r.setMin(order.getMin());
							}
						}
						p.addRelation(r);
					}
					else{
						syn=(Syntactic)relation;
						d=new DependencyRelation(syn.getDep()-1, syn.getHead()-1, syn.getType()!=null ? syn.getType().value() : null, syn.getForce()==ForceType.POSITIVE);
						p.addRelation(d);
					}										
				}					
			}
			
			output.add(p);		
		}
		
			
		return output;
	}
	

	
	public static ArrayList<Entry> createPatternsFromLexiconWithTffAttributes(File dictFile, File attFile){
		Lexicon lex = (Lexicon)readDictFile(dictFile, "opin.dictionary.dictDef");
		
		opin.dictionary.dictAttTff.Lexicon attLex =  (opin.dictionary.dictAttTff.Lexicon)readDictFile(attFile, "opin.dictionary.dictAttTff");
		
		ArrayList<Entry> output=new ArrayList<Entry>();
		Entry p;
		Element e;
		ElementType et;
		OrderRelation r;
		DependencyRelation d;
		MorphoSyntaxType m;
		List<MorphoSyntaxType> ms;
		RelationBaseType relation;
		Order order;
		Syntactic syn;
		List<ElementType> elements;
		List<RelationBaseType> relations;
		List<EntryBaseType> entries = lex.getEntry();
		
		
		WordType word;
		LemmaType lemma;
		PosType pos;
		MajorClassType mC;
		
		int i=0;
		
		for (Iterator<EntryBaseType> iterator = entries.iterator(); iterator.hasNext();) {
			i++;
			opin.dictionary.dictAttTff.Tff att = ((opin.dictionary.dictAttTff.Tff)attLex.getEntryAttributes().get(i-1));
			int ref = att.getEntryRef();
			
			if(ref != i){
				System.err.println("Mismatch between def and att file");
				System.exit(-1);
			}
			
			EntryBaseType entry =  iterator.next();
			
			if(entry.getClass().equals(Sw.class)){	
				p = new Entry(entry.getId(),true);
				m=((Sw)entry).getEntryDefinition().getMorphoSyntax();
				
				word=m.getWord();
				lemma=m.getLemma();
				pos=m.getPos();
				mC=m.getMC();
				
				MorphoSyntax morpho=new MorphoSyntax();
				
				if(word!=null){
					morpho.setWord(word.getValue());
					morpho.setWordForce(word.getForce()==ForceType.POSITIVE);
				}
				if(lemma!=null){
					morpho.setLemma(lemma.getValue());
					morpho.setLemmaForce(lemma.getForce()==ForceType.POSITIVE);
				}
				if(pos!=null){
					morpho.setPos(pos.getValue());
					morpho.setPosForce(pos.getForce()==ForceType.POSITIVE);	
				}
				if(mC!=null){
					morpho.setMC(mC.getValue().value());
					morpho.setMCForce(mC.getForce()==ForceType.POSITIVE);
				}
				
				
				e=new Element(1);
				e.add(morpho);
				e.setForce(true);
				p.addElement(e);
				
			}
			else{
				p = new Entry(entry.getId(),false);
				elements=((Mw)entry).getEntryDefinition().getElement();
				
				CompositionType comp = ((Mw)entry).getEntryDefinition().getComposition();
				if(comp!=null){
					relations=((Mw)entry).getEntryDefinition().getComposition().getRelation();
				}
				else{
					relations=new ArrayList<RelationBaseType>();
				}
				
				
				for (Iterator<ElementType> itrE = elements.iterator(); itrE.hasNext();) {
					et=itrE.next();
					ms = et.getMorphoSyntax();
					e=new Element(ms.size());
					
					for (Iterator<MorphoSyntaxType> itrM = ms.iterator(); itrM.hasNext();) {
						m= itrM.next();
						word=m.getWord();
						lemma=m.getLemma();
						pos=m.getPos();
						mC=m.getMC();
						
						MorphoSyntax morpho=new MorphoSyntax();
						if(word!=null){
							morpho.setWord(word.getValue());
							morpho.setWordForce(word.getForce()==ForceType.POSITIVE);
						}
						if(lemma!=null){
							morpho.setLemma(lemma.getValue());
							morpho.setLemmaForce(lemma.getForce()==ForceType.POSITIVE);
						}
						if(pos!=null){
							morpho.setPos(pos.getValue());
							morpho.setPosForce(pos.getForce()==ForceType.POSITIVE);	
						}
						if(mC!=null){
							morpho.setMC(mC.getValue().value());
							morpho.setMCForce(mC.getForce()==ForceType.POSITIVE);
						}
						
						
						e.add(morpho);
					}
					
					
					e.setForce(et.getForce()==ForceType.POSITIVE);
					p.addElement(e);
					
				}
				
				for (Iterator<RelationBaseType> itrE = relations.iterator(); itrE.hasNext();) {
					relation = itrE.next();
					if(relation.getClass().equals(Order.class)){
						order=(Order)relation;
						r=new OrderRelation(order.getPre()-1,order.getFol()-1, order.getForce()==ForceType.POSITIVE);
						if(order.getEqual()!=null){
							r.setEqual(order.getEqual());
						}
						else{
							if(order.getMax()!=null){
								r.setMax(order.getMax());
							}
							if(order.getMin()!=null){
								r.setMin(order.getMin());
							}
						}
						p.addRelation(r);
					}
					else{
						syn=(Syntactic)relation;
						d=new DependencyRelation(syn.getDep()-1, syn.getHead()-1, syn.getType()!=null ? syn.getType().value() : null, syn.getForce()==ForceType.POSITIVE);
						p.addRelation(d);
					}										
				}					
			}
			p.addAttributes("priorpolarity",att.getPriorpolarity().value());
			p.addAttributes("type",att.getType());
			output.add(p);		
		}
		
			
		return output;
	}
	
	
	
	public static ArrayList<Entry> createPatternsFromLexiconWithRuppenhoferAttributes(File dictFile, File attFile){
		Lexicon lex = (Lexicon)readDictFile(dictFile, "opin.dictionary.dictDef");
		
		opin.dictionary.dictAtt.Lexicon attLex =  (opin.dictionary.dictAtt.Lexicon)readDictFile(attFile, "opin.dictionary.dictAtt");
		
		ArrayList<Entry> output=new ArrayList<Entry>();
		Entry p;
		Element e;
		ElementType et;
		OrderRelation r;
		DependencyRelation d;
		MorphoSyntaxType m;
		List<MorphoSyntaxType> ms;
		RelationBaseType relation;
		Order order;
		Syntactic syn;
		List<ElementType> elements;
		List<RelationBaseType> relations;
		List<EntryBaseType> entries = lex.getEntry();
		
		
		WordType word;
		LemmaType lemma;
		PosType pos;
		MajorClassType mC;
		
		int i=0;
		
		for (Iterator<EntryBaseType> iterator = entries.iterator(); iterator.hasNext();) {
			i++;
			opin.dictionary.dictAtt.Ruppenhofer att = ((opin.dictionary.dictAtt.Ruppenhofer)attLex.getEntryAttributes().get(i-1));
			int ref = att.getEntryRef();
			
			if(ref != i){
				System.err.println("Mismatch between def and att file");
				System.exit(-1);
			}
			
			EntryBaseType entry =  iterator.next();
			
			if(entry.getClass().equals(Sw.class)){	
				p = new Entry(entry.getId(),true);
				m=((Sw)entry).getEntryDefinition().getMorphoSyntax();
				
				word=m.getWord();
				lemma=m.getLemma();
				pos=m.getPos();
				mC=m.getMC();
				
				MorphoSyntax morpho=new MorphoSyntax();
				
				if(word!=null){
					morpho.setWord(word.getValue());
					morpho.setWordForce(word.getForce()==ForceType.POSITIVE);
				}
				if(lemma!=null){
					morpho.setLemma(lemma.getValue());
					morpho.setLemmaForce(lemma.getForce()==ForceType.POSITIVE);
				}
				if(pos!=null){
					morpho.setPos(pos.getValue());
					morpho.setPosForce(pos.getForce()==ForceType.POSITIVE);	
				}
				if(mC!=null){
					morpho.setMC(mC.getValue().value());
					morpho.setMCForce(mC.getForce()==ForceType.POSITIVE);
				}
				
				
				e=new Element(1);
				e.add(morpho);
				e.setForce(true);
				p.addElement(e);
				
			}
			else{
				p = new Entry(entry.getId(),false);
				elements=((Mw)entry).getEntryDefinition().getElement();
				
				CompositionType comp = ((Mw)entry).getEntryDefinition().getComposition();
				if(comp!=null){
					relations=((Mw)entry).getEntryDefinition().getComposition().getRelation();
				}
				else{
					relations=new ArrayList<RelationBaseType>();
				}
				
				
				for (Iterator<ElementType> itrE = elements.iterator(); itrE.hasNext();) {
					et=itrE.next();
					ms = et.getMorphoSyntax();
					e=new Element(ms.size());
					
					for (Iterator<MorphoSyntaxType> itrM = ms.iterator(); itrM.hasNext();) {
						m= itrM.next();
						word=m.getWord();
						lemma=m.getLemma();
						pos=m.getPos();
						mC=m.getMC();
						
						MorphoSyntax morpho=new MorphoSyntax();
						if(word!=null){
							morpho.setWord(word.getValue());
							morpho.setWordForce(word.getForce()==ForceType.POSITIVE);
						}
						if(lemma!=null){
							morpho.setLemma(lemma.getValue());
							morpho.setLemmaForce(lemma.getForce()==ForceType.POSITIVE);
						}
						if(pos!=null){
							morpho.setPos(pos.getValue());
							morpho.setPosForce(pos.getForce()==ForceType.POSITIVE);	
						}
						if(mC!=null){
							morpho.setMC(mC.getValue().value());
							morpho.setMCForce(mC.getForce()==ForceType.POSITIVE);
						}
						
						
						e.add(morpho);
					}
					
					
					e.setForce(et.getForce()==ForceType.POSITIVE);
					p.addElement(e);
					
				}
				
				for (Iterator<RelationBaseType> itrE = relations.iterator(); itrE.hasNext();) {
					relation = itrE.next();
					if(relation.getClass().equals(Order.class)){
						order=(Order)relation;
						r=new OrderRelation(order.getPre()-1,order.getFol()-1, order.getForce()==ForceType.POSITIVE);
						if(order.getEqual()!=null){
							r.setEqual(order.getEqual());
						}
						else{
							if(order.getMax()!=null){
								r.setMax(order.getMax());
							}
							if(order.getMin()!=null){
								r.setMin(order.getMin());
							}
						}
						p.addRelation(r);
					}
					else{
						syn=(Syntactic)relation;
						d=new DependencyRelation(syn.getDep()-1, syn.getHead()-1, syn.getType()!=null ? syn.getType().value() : null, syn.getForce()==ForceType.POSITIVE);
						p.addRelation(d);
					}										
				}					
			}
			p.addAttributes("priorpolarity",att.getSpPolarity());
			p.addAttributes("type",att.getPatterntype());
			output.add(p);		
		}
		
			
		return output;
	}
	
	
	
	
	public static ArrayList<Sentence> createSentencesFromDepFile(File depFile){
		
		FileInputStream rawFile = null;
		InputStreamReader sReader = null;
		BufferedReader bReader = null;
		ArrayList<Sentence> output = new ArrayList<Sentence>();
		String line;
		String[] lineSplitted;
		Sentence sentence=null;
		Token token;
		int sentenceID=0;

		try {
			rawFile = new FileInputStream(depFile);
			sReader = new InputStreamReader(rawFile, "UTF-8");
			bReader = new BufferedReader(sReader, 2048*30); // default 2048

			while ((line = bReader.readLine()) != null) {
				line=line.trim();
				if (!line.equals("")) {                    
					lineSplitted=line.split(TAB);
					if(lineSplitted[WORDFORM].contains(SENTENCESTART)){
						sentence= new Sentence(sentenceID);
						sentence.addToken(new Token(0,0,0,EMPTY,SENS,SENS,SENS,0,SENS));
						sentenceID++;
					}
					else if(lineSplitted[WORDFORM].contains(SENTENCEEND)){	
						ArrayList<Token> tokens=sentence.getTokenList();
						for (int i=1;i< tokens.size()-1; i++) {
							token = tokens.get(i);
							token.setHead(tokens.get(token.getHeadPosition()));							
						}
						output.add(sentence);
					}
					else if(lineSplitted[POS].equals(SENT)){
						sentence.addToken(new Token(Integer.parseInt(lineSplitted[POSITION]),Integer.parseInt(lineSplitted[SPANS]),Integer.parseInt(lineSplitted[SPANE]),lineSplitted[WORDFORM],SENE,lineSplitted[POS],SENE,Integer.parseInt(lineSplitted[HEAD]),lineSplitted[RELTYPE]));						
					}
					else {	
						String mC;
					
						
						if(lineSplitted[POS].length()>=1){
							if(lineSplitted[POS].equals(PP) || lineSplitted[POS].equals(PP$) || lineSplitted[POS].equals(WP)){
								mC=P;
							}
							else{
								mC=lineSplitted[POS].substring(0, 1);
							}
						}
						else{
							mC=lineSplitted[POS];
						}						
						sentence.addToken(new Token(Integer.parseInt(lineSplitted[POSITION]),Integer.parseInt(lineSplitted[SPANS]),Integer.parseInt(lineSplitted[SPANE]),lineSplitted[WORDFORM].toLowerCase(),lineSplitted[LEMMA].toLowerCase(),lineSplitted[POS],mC,Integer.parseInt(lineSplitted[HEAD]),lineSplitted[RELTYPE]));						
					}															
				}
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
		return output;				
		
	}
	
	
	public static Object readDictFile(File dictFile, String context) {
		Object root = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(context);
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			root = unmarshaller.unmarshal(dictFile);

		} catch (JAXBException ex) {
			ex.printStackTrace();
		}
		return root;
	}
	
	
	public static HashMap<String, HashSet<String>> readExpansionFile(File expansionFile) {
		HashMap<String, HashSet<String>> output=new HashMap<String, HashSet<String>>();
		
		java.util.regex.Matcher m;
		
		FileInputStream rawFile = null;
		InputStreamReader sReader = null;
		BufferedReader bReader = null;

		String line;
		String[] tokens;
		HashSet<String> l;

		try {
			rawFile = new FileInputStream(expansionFile);
			sReader = new InputStreamReader(rawFile, "UTF-8");
			bReader = new BufferedReader(sReader, 2048*4); // default 2048

			while ((line = bReader.readLine()) != null) {
				line=line.trim();
				if (!line.equals("") && !line.startsWith("#")) {                    
					m=EXPREGEX.matcher(line);
					if(m.matches()){
						tokens=m.group(2).split(EXPSPLITTER);
						l=new HashSet<String>();
						for (int i = 0; i < tokens.length; i++) {
							l.add(tokens[i].trim());
						}						
						output.put(m.group(1), l);
					}
					else{
						System.out.println("Incorrect Expansion: "+line);
					}
				}
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
		
		return output;
	}
	
	public static void readDocList(File doclistFile, String instanceFileName, ArrayList<File> docs, ArrayList<File> deps, ArrayList<File> senSplits, ArrayList<File> instanceFiles) {
		FileInputStream rawFile = null;
		InputStreamReader sReader = null;
		BufferedReader bReader = null;
		File file;
		String docName;
		String line;
		String autoAnnsBase;

		try {
			rawFile = new FileInputStream(doclistFile);
			sReader = new InputStreamReader(rawFile, "UTF-8");
			bReader = new BufferedReader(sReader, 2048*20); // default 2048

			while ((line = bReader.readLine()) != null) {
				line=line.trim();
				if (!line.equals("")) {                    
					file=new File(line);	
					docName=file.getName();
					autoAnnsBase=file.getAbsolutePath().replace("docs", "auto_anns");
					if(docs!=null){
						docs.add(file);
					}
					if(deps!=null){
						deps.add(new File(autoAnnsBase+File.separator+docName+DEPFILEEXTENSION));
					}
					if(senSplits!=null){
						senSplits.add(new File(autoAnnsBase+File.separator+docName+SENSPLITEXTENSION));
					}
					if(instanceFiles!=null){
						instanceFiles.add(new File(autoAnnsBase+File.separator+instanceFileName));
					}
				}
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
	}

}
