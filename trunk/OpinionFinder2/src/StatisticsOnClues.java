import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;

import entity.Annotation;
import entity.Doc;
import entity.Sentence;
import entity.Span;


public class StatisticsOnClues {

	/**
	 * @param args
	 */
	
	
	public static double expressionCount = 0;
	public static double clueCount = 0;
	
	public static double insideExpressionCount = 0;
	public static double outsideExpressionCount = 0;
	
	public static double weakinsideExpressionCount = 0;
	public static double weakoutsideExpressionCount = 0;

	public static double stronginsideExpressionCount = 0;
	public static double strongoutsideExpressionCount = 0;


	public static double expressionWithClueCount = 0;
	public static double expressionWithOutClueCount = 0;
	
	public static double senCount=0;
	
	
	public static void main(String[] args) {
		ArrayList<Doc> docs=null;
		
		try{
			docs = readObject();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		
		for(Doc doc : docs){
			senCount+=doc.getSentences().size();
			for(Sentence sen : doc.getSentences()){
				
				
				
				ArrayList<Span> manAnnSpans = getRequiredManAnnSpans(sen);
				ArrayList<Span> autoAnnSpans = getRequiredAutoAnnSpans(sen);
				expressionCount+=manAnnSpans.size();
				clueCount+=autoAnnSpans.size();
				
				for(Span as : autoAnnSpans){
					boolean inside = InSubjectiveExpressionSpan(as, manAnnSpans);
					if(inside){
						insideExpressionCount++;
						
						if(as.getSource()!=null && as.getSource().equals("weaksubj")){
							weakinsideExpressionCount++;
						}
						else if(as.getSource()!=null && as.getSource().equals("strongsubj")){
							stronginsideExpressionCount++;
						}
						else{
							System.out.println("BU NE YA!");
						}
						
					}
					else{
						outsideExpressionCount++;
						
						if(as.getSource()!=null && as.getSource().equals("weaksubj")){
							weakoutsideExpressionCount++;
						}
						else if(as.getSource()!=null && as.getSource().equals("strongsubj")){
							strongoutsideExpressionCount++;
						}
						else{
							System.out.println("BU NE YA!");
						}
					}
				}
				
				for(Span ms : manAnnSpans){
					boolean consists = consistsClueSpan(ms, autoAnnSpans);
					if(consists){
						expressionWithClueCount++;
					}
					else{
						expressionWithOutClueCount++;
					}
				}
			}
		}
		
		
		System.out.println("Expression Count: "+expressionCount+" | With Clue: "+expressionWithClueCount+" | Without Clue : "+expressionWithOutClueCount);
		System.out.println("Clue Count: "+clueCount+" | in Expression: "+insideExpressionCount+" | not in Expression : "+outsideExpressionCount);
		
		System.out.println("Percentage of Clue Instances Inside Subjectivity Expression: "+insideExpressionCount/clueCount);
		System.out.println("Percentage of Clue Instances Outside Subjectivity Expression: "+outsideExpressionCount/clueCount);
		System.out.println("Percentage of Subjective Expressions With Clues: "+expressionWithClueCount/expressionCount);
		
		System.out.println();
		System.out.println();
		
		System.out.println(weakinsideExpressionCount+"|"+weakoutsideExpressionCount);
		System.out.println(stronginsideExpressionCount+"|"+strongoutsideExpressionCount);
		
		System.out.println("Percentage of Weaksubj Clue Instances Inside Subjectivity Expression: "+(weakinsideExpressionCount/(weakinsideExpressionCount+weakoutsideExpressionCount)));
		System.out.println("Percentage of Strongsubj Clue Instances Inside Subjectivity Expression: "+(stronginsideExpressionCount/(stronginsideExpressionCount+strongoutsideExpressionCount)));
		
		
		System.out.println(senCount);
	}

	
	// assumes manSpans are sorted
	public static boolean InSubjectiveExpressionSpan(Span span, ArrayList<Span> manSpans){
		boolean output=false;
		for(Span s: manSpans){
			if(span.getStart()>=s.getStart() && span.getEnd()<=s.getEnd()){
				output=true;
				break;
			}
		}
		return output;
	}
	
	// assumes autoSpans are sorted
	public static boolean consistsClueSpan(Span span, ArrayList<Span> autoSpans){
		boolean output=false;
		for(Span s: autoSpans){
			if(s.getStart()>=span.getStart() && s.getEnd()<=span.getEnd()){
				output=true;
				break;
			}
		}
		return output;
	}
	
	
	public static boolean InSpan(Span s1, Span s2){
		boolean output=false;		
		if(s1.getStart()>=s2.getStart() && s1.getEnd()<=s2.getEnd()){
				output=true;			
		}		
		return output;
	}
	

	public static void compareAutoAndManAnnsForSubjectivity(ArrayList<Span> autoAnns, ArrayList<Span> manAnns){		
		for(Span sa : autoAnns){
			boolean matchedBefore=false;			
			for(Span sm : manAnns){
				
				//if((sa.getStart()>=sm.getStart() && sa.getStart()<sm.getEnd()) || (sa.getStart()>sm.getStart() && sa.getEnd()<=sm.getEnd()) || sa.getStart()<sm.getStart() && sa.getEnd()>sm.getEnd()){
				if((sa.getStart()>=sm.getStart() && sa.getEnd()<=sm.getEnd())){
				    sa.setType("subj");
					if(matchedBefore){
						System.out.println("Matched more than 1 manual expression!!");
					}
					matchedBefore=true;
				}			
			}
			if(sa.getType()==null){
				sa.setType("obj");
			}
		}
	}
	

	
	// can only be used on gateman.mpqa.lre.1.2
	public static ArrayList<Span> getRequiredManAnnSpans(Sentence sen){
		ArrayList<Span> output = new ArrayList<Span>();
		HashMap<String,ArrayList<Annotation>> manAnns=sen.getManAnns();
		ArrayList<Annotation> annotations;
		HashMap<String,String> attpairs;
		
		if(manAnns.containsKey("GATE_expressive-subjectivity")){				
			annotations=manAnns.get("GATE_expressive-subjectivity");				
			for(Annotation ann : annotations){
				if(ann.getSingleAttributes("polarity")!=null){
					Span s = new Span(ann.getSpanS(),ann.getSpanE());
					s.setPol(ann.getSingleAttributes("polarity").replace("uncertain-", ""));
					output.add(s);
				}
			}
		}
		if(manAnns.containsKey("GATE_direct-subjective")){				
			annotations=manAnns.get("GATE_direct-subjective");				
			for(Annotation ann : annotations){
				attpairs=ann.getAttributes(); //!attpairs.containsKey("expression-intensity") 
				if(attpairs.containsKey("expression-intensity") && !attpairs.get("expression-intensity").equals("neutral") ){ //&& attpairs.get("expression-intensity").equals("neutral")  										
					if(!attpairs.containsKey("implicit") || !attpairs.get("implicit").equals("true")){
						Span s = new Span(ann.getSpanS(),ann.getSpanE());
						s.setPol(ann.getSingleAttributes("polarity").replace("uncertain-", ""));
						output.add(s);												
					}
				}							
			}
		}		
		Collections.sort(output);
		return output;
	}
	
	
	
	public static ArrayList<Span> getRequiredAutoAnnSpans(Sentence sen){
		ArrayList<Span> output = new ArrayList<Span>();
		HashMap<String,ArrayList<Annotation>> autoAnns=sen.getAutoAnns();
		ArrayList<Annotation> annotations;
		if(autoAnns.containsKey("matchfeat")){				
			annotations=autoAnns.get("matchfeat");				
			for(Annotation ann : annotations){
				output.add(new Span(ann.getSpanS(),ann.getSpanE(),ann.getAttributes().get("type")));
			}
		}	
		return output;
	}
	
	
	
	public static ArrayList<Doc> readObject() throws Exception{
		
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("/home/cem/workspace/DATA/corpus/docsCompleteEMNLP05NewTerminology.dat"));
		ArrayList<Doc> docs= (ArrayList<Doc>)ois.readObject();		
		ois.close();
		return docs;
		
	}
	
	
}
