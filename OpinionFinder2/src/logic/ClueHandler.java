package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeMap;
import java.util.regex.Matcher;

import config.Config;
import entity.Annotation;
import entity.Doc;
import entity.Sentence;
import entity.Span;

public class ClueHandler {
	
	private static String[] modalPOSArray={"MD"};
	private static String[] modalNOTArray={"will"};
	private static String[] pronounPOSArray={"PRP","PRP$","WP","WP$"};
	private static String[] adjectivePOSArray={"JJ","JJR","JJS"};
	private static String[] adverbPOSArray={"RB","RBR","RBS","WRB"};
	private static String[] adverbNOTArray={"n't","not"};
	private static String[] cardinalPOSArray={"CD"};
	
	private static HashSet<String> modalPOS=new HashSet<String>();
	private static HashSet<String> modalNOT=new HashSet<String>();
	private static HashSet<String> pronounPOS=new HashSet<String>();
	private static HashSet<String> adjectivePOS=new HashSet<String>();
	private static HashSet<String> adverbPOS=new HashSet<String>();
	private static HashSet<String> adverbNOT=new HashSet<String>();
	private static HashSet<String> cardinalPOS=new HashSet<String>();
	
	static {
		modalPOS.addAll(Arrays.asList(modalPOSArray));
		modalNOT.addAll(Arrays.asList(modalNOTArray));
		pronounPOS.addAll(Arrays.asList(pronounPOSArray));
		adjectivePOS.addAll(Arrays.asList(adjectivePOSArray));
		adverbPOS.addAll(Arrays.asList(adverbPOSArray));
		adverbNOT.addAll(Arrays.asList(adverbNOTArray));
		cardinalPOS.addAll(Arrays.asList(cardinalPOSArray));
	}
	
	
	public static void insertAllClueTypes(Doc doc){
		ArrayList<Sentence> sentences = doc.getSentences();
		
		for(Sentence s : sentences){
			if(Config.STRONGSUBJTYPES.size()>0 || Config.WEAKSUBJTYPES.size()>0){
				storeClues(s,doc,Config.ANNOTATIONNAME,Config.ANNOTATIONATT);
			}
			if(Config.SUNDANCESUBJTYPES.size()>0){
				storeClues(s,doc,Config.ANNOTATIONNAMEFORSUBJPATTERN);
			}
			if(Config.SUNDANCEOBJTYPES.size()>0){
				storeClues(s,doc,Config.ANNOTATIONNAMEFOROBJPATTERN);
			}
		}		
		
	}
	
	public static void removeClueSpans(Doc doc, TreeMap<String,ArrayList<Span>> exclude){
		String key=doc.getCorrespondingRawFile().getParentFile().getName()+"|"+doc.getCorrespondingRawFile().getName();
		if(exclude.containsKey(key)){
			ArrayList<Span> spans = exclude.get(key);
			ArrayList<Sentence> sentences = doc.getSentences();

			for(Span sp : spans){
				for(Sentence sen : sentences){
					sen.getClues().remove(sp);
				}
			}
		}			
	}
	
	

	public static void removeOverlappingClueSpans(Doc doc, TreeMap<String,ArrayList<Span>> exclude){

		String key=doc.getCorrespondingRawFile().getParentFile().getName()+"|"+doc.getCorrespondingRawFile().getName();
		if(exclude.containsKey(key)){
			ArrayList<Span> spans = exclude.get(key);
			ArrayList<Sentence> sentences = doc.getSentences();

			for(Span sp : spans){
				for(Sentence sen : sentences){
					if(sp.getStart()>sen.getSpanS() && sp.getEnd()<sen.getSpanE()){
						ArrayList<Span> remove= new ArrayList<Span>();
						for(Span spanToRemove : sen.getClues().keySet()){
							if( sp.getStart()==spanToRemove.getStart() && sp.getEnd()==spanToRemove.getEnd() ){
								remove.add(spanToRemove);
							}	
							//if((sp.getStart()>=spanToRemove.getStart() && sp.getStart()<spanToRemove.getEnd()) || (sp.getEnd()>spanToRemove.getStart() && sp.getEnd()<=spanToRemove.getEnd()) || sp.getStart()<spanToRemove.getStart() && sp.getEnd()>spanToRemove.getEnd()){
							//	remove.add(spanToRemove);
							//}
						}	
						for(Span spanToRemove : remove){
							sen.getClues().remove(spanToRemove);
						}
					}
				}
			}
		}		


	}

	public static HashSet<Sentence> getEvaluationSentences(Doc doc, TreeMap<String,ArrayList<Span>> exclude, boolean context){
		
		HashSet<Sentence> output= new HashSet<Sentence>();
		
		String key=doc.getCorrespondingRawFile().getParentFile().getName()+"|"+doc.getCorrespondingRawFile().getName();
		
		if(exclude.containsKey(key)){
			ArrayList<Span> spans = exclude.get(key);

			if(spans.size()>0){
				ArrayList<Sentence> sentences = doc.getSentences();
				for(int i=0;i<sentences.size();i++){					
					for(Span sp : spans){
						if(sp.getStart()>sentences.get(i).getSpanS() && sp.getEnd()<sentences.get(i).getSpanE()){														
							
							boolean found =false;
							for(Span clue : sentences.get(i).getClues().keySet()){
								if(sp.getStart()==clue.getStart() && sp.getEnd()==clue.getEnd()){
									found=true;
								}
							}
							if(found){
								
								output.add(sentences.get(i));							
								if(context){
									if(i>0 ){
										output.add(sentences.get(i-1));
									}
									if(i<sentences.size()-1){
										output.add(sentences.get(i+1));
									}
								}							
								break;
								
							}
						}
					}
				}
			}
		}		
		return output;
	}
	
	

	public static void handleFeatures(Doc doc){
		ArrayList<Sentence> sentences = doc.getSentences();		
		for(Sentence s : sentences){
			storeClueCounts(s);
			s.getAdjective().addAll(getSyntacticInfo(s, "GATE_Token", adjectivePOS));
			s.getAdverb().addAll(getSyntacticInfo(s, "GATE_Token", adverbPOS, adverbNOT));
			s.getCardinal().addAll(getSyntacticInfo(s, "GATE_Token", cardinalPOS));
			s.getModal().addAll(getSyntacticInfo(s, "GATE_Token", modalPOS, modalNOT));
			s.getPronoun().addAll(getSyntacticInfo(s, "GATE_Token", pronounPOS));
		}		
	}


	public static void storeClues(Sentence sen, Doc doc, String annName, String annAtt){
		HashMap<String,ArrayList<Annotation>> autoAnns=sen.getAutoAnns();		

		if(autoAnns.containsKey(annName)){				
			ArrayList<Annotation> annotations=autoAnns.get(annName);				
			for(Iterator<Annotation> itr_j=annotations.iterator();itr_j.hasNext();){
				Annotation ann=itr_j.next();
				HashMap<String,String> attpairs=ann.getAttributes();
				if(attpairs.containsKey(annAtt)){
					if(!discardFeature(doc,ann,sen)){
						if(Config.STRONGSUBJTYPES.contains(attpairs.get(annAtt))){
							sen.addToClues(new Span(ann.getSpanS(),ann.getSpanE()), attpairs.get(annAtt));    				
						}
						else if(Config.WEAKSUBJTYPES.contains(attpairs.get(annAtt))){
							sen.addToEndClues(new Span(ann.getSpanS(),ann.getSpanE()), attpairs.get(annAtt));
						}
					}
				}
			}
		}
	}


	public static void storeClues(Sentence sen, Doc doc, String annName){
		HashMap<String,ArrayList<Annotation>> autoAnns=sen.getAutoAnns();		

		if(autoAnns.containsKey(annName)){				
			ArrayList<Annotation> annotations=autoAnns.get(annName);				
			for(Iterator<Annotation> itr_j=annotations.iterator();itr_j.hasNext();){
				Annotation ann=itr_j.next();				
				if(!discardFeature(doc,ann,sen)){
					sen.addToClues(new Span(ann.getSpanS(),ann.getSpanE()), annName);    				
				}			
			}
		}

	}
	
	public static void storeClueCounts(Sentence sen){
		int weakC=0;
		int strongC=0;
		int subjPC=0;
		int objPC=0;
		TreeMap<Span,LinkedList<String>> clues=sen.getClues();		
		
		for(LinkedList<String> v : clues.values()){
			String type=v.get(0);
			if(Config.STRONGSUBJTYPES.contains(type)){
				strongC++;
			}
			else if(Config.WEAKSUBJTYPES.contains(type)){
				weakC++;
			}
			else if(Config.SUNDANCESUBJTYPES.contains(type)){
				subjPC++;
			}
			else if(Config.SUNDANCEOBJTYPES.contains(type)){
				objPC++;
			}			
		}
		
		sen.setStrongCount(strongC);
		sen.setWeakCount(weakC);
		sen.setSubjPatternCount(subjPC);
		sen.setObjPatternCount(objPC);		
	}

	public static ArrayList<Span> getSyntacticInfo(Sentence sen, String annName, HashSet<String> pos, HashSet<String> not){
		ArrayList<Span> output= new ArrayList<Span>();
		HashMap<String,ArrayList<Annotation>> gatedefaultAnns=sen.getGatedefaultAnns();		
		if(gatedefaultAnns.containsKey(annName)){				
			ArrayList<Annotation> annotations=gatedefaultAnns.get(annName);				
			for(Iterator<Annotation> itr=annotations.iterator();itr.hasNext();){
				Annotation ann=itr.next();				
				if(pos.contains(ann.getAttributes().get("category"))){
					if(!not.contains(ann.getAttributes().get("string").toLowerCase())){
						output.add(new Span(ann.getSpanS(),ann.getSpanE()));
					}				
				}
			}
		}
		return output;
	}
	
	public static ArrayList<Span> getSyntacticInfo(Sentence sen, String annName, HashSet<String> pos){
		ArrayList<Span> output= new ArrayList<Span>();
		HashMap<String,ArrayList<Annotation>> gatedefaultAnns=sen.getGatedefaultAnns();		
		if(gatedefaultAnns.containsKey(annName)){				
			ArrayList<Annotation> annotations=gatedefaultAnns.get(annName);				
			for(Iterator<Annotation> itr=annotations.iterator();itr.hasNext();){
				Annotation ann=itr.next();				
				if(pos.contains(ann.getAttributes().get("category"))){					
						output.add(new Span(ann.getSpanS(),ann.getSpanE()));									
				}
			}
		}
		return output;
	}
	
	
	private static boolean discardFeature(Doc doc, Annotation ann, Sentence sen){		
		//if((attpairs.get("pattern").indexOf("noun")!=-1 && Character.isLowerCase(docContent.getRawSpan(ann.getSpanS(), ann.getSpanE()).charAt(0))) || attpairs.get("pattern").indexOf("noun")==-1){
		boolean output=false;
		String text=doc.getRawSpan(ann.getSpanS(), ann.getSpanE());
		String textSen=doc.getRawSpan(sen.getSpanS(), sen.getSpanE());
		String[] words=textSen.split("\\s+");
		Matcher m=Config.FEATUREERASER.matcher(text.substring(1)); // ilk harfi burada kontrol etmiyoruz
		if(sen.getSpanS()!=0){
			if(m.find()){
				output=true;
			}
			else{
				if(isCharUpperCase(text,0)){	
					if(ann.getSpanS()!= sen.getSpanS() ){  //&& !(sen.getSenID()==1 && ann.getSpanS() == sen.getSpanS()+1 && doc.getRawSpan(sen.getSpanS(),sen.getSpanS()+1).equals("\""))
						if(sen.getSenID()==1){
						for(int i=0;i<words.length;i++){
							if(words[i].length() > 0 && Character.isLowerCase(words[i].charAt(0))){
								output=true;
							}
						}
						}
						else{
							output=true;	
						}
					}		
				}
			}
		}		
		return output;
	}
	
	private static boolean isCharUpperCase(String text,int index){
		return Character.isUpperCase(text.charAt(0));
	}

}
