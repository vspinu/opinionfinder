package logic;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import entity.Sentence;
import entity.Doc;
import config.Config;


public class EvaluationHandler {

	public static double[][] evaluateRuleBased(ArrayList<Doc> docs){		
		double[][] conTable=new double[Config.CONTINGENCYTABLEMAPPING.size()][Config.CONTINGENCYTABLEMAPPING.size()];
		for(Doc doc : docs){
			for(Sentence sen : doc.getSentences()){
				conTable[Config.CONTINGENCYTABLEMAPPING.get(sen.getGoldC()).intValue()][Config.CONTINGENCYTABLEMAPPING.get(sen.getHpC()).intValue()]++;		
			}
		}
		return conTable;
	}
	
	public static HashMap<String,Double> createStatistics(double[][] ctable, boolean normalized){
		double subjSenC=0;
		double objSenC=0;
		double ukSenC=0;
		double pSubjSenC=0;
		double pObjSenC=0;
		double pUkSenC=0;
		double correctP=0;
		double totalSenC=0;
		
		double precSubj=0;
		double precObj=0;
		double precUk=0;
		
		double recSubj=0;
		double recObj=0;
		double recUk=0;
		
		double assignedSenC=0;
		
		double[][] normctable;
		HashMap<String,Double> output=new HashMap<String, Double>();
		for(int i=0;i<ctable.length;i++){
			subjSenC+=ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)][i];
		}
		for(int i=0;i<ctable.length;i++){
			objSenC+=ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)][i];
		}
		for(int i=0;i<ctable.length;i++){
			ukSenC+=ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)][i];
		}
		for(int i=0;i<ctable.length;i++){
			pSubjSenC+=ctable[i][Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)];
		}
		for(int i=0;i<ctable.length;i++){
			pObjSenC+=ctable[i][Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)];
		}
		for(int i=0;i<ctable.length;i++){
			pUkSenC+=ctable[i][Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)];
		}
		for(int i=0;i<ctable.length;i++){
			correctP+=ctable[i][i];
		}
		
		totalSenC=subjSenC+objSenC+ukSenC;
		assignedSenC=pSubjSenC+pObjSenC;
		
		
		
		
		
		
		
		if(normalized){
			
			output.put(Config.PSENTENCENUMSUBJ, 100*new Double(pSubjSenC)/totalSenC);
			output.put(Config.PSENTENCENUMOBJ, 100*new Double(pObjSenC)/totalSenC);
			output.put(Config.PSENTENCENUMUK, 100*new Double(pUkSenC)/totalSenC);
					
			output.put(Config.SENTENCENUMSUBJ, 100*new Double(subjSenC)/totalSenC);
			output.put(Config.SENTENCENUMOBJ, 100*new Double(objSenC)/totalSenC);
			output.put(Config.SENTENCENUMUK, 100*new Double(ukSenC)/totalSenC);
			output.put(Config.SENTENCENUM, 100*new Double(totalSenC)/totalSenC);
			
			normctable=normContingencyTable(ctable,new Double(totalSenC).intValue());
			output.put(Config.CTSUBJSUBJ,new Double(100*normctable[Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)]));
			output.put(Config.CTSUBJOBJ,new Double(100*normctable[Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)]));
			output.put(Config.CTSUBJUK,new Double(100*normctable[Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)]));

			output.put(Config.CTOBJSUBJ,new Double(100*normctable[Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)]));
			output.put(Config.CTOBJOBJ,new Double(100*normctable[Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)]));
			output.put(Config.CTOBJUK,new Double(100*normctable[Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)]));

			output.put(Config.CTUKSUBJ,new Double(100*normctable[Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)][Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)]));
			output.put(Config.CTUKOBJ,new Double(100*normctable[Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)][Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)]));
			output.put(Config.CTUKUK,new Double(100*normctable[Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)][Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)]));
		}
		else{

			output.put(Config.PSENTENCENUMSUBJ, new Double(pSubjSenC));
			output.put(Config.PSENTENCENUMOBJ, new Double(pObjSenC));
			output.put(Config.PSENTENCENUMUK, new Double(pUkSenC));
					
			output.put(Config.SENTENCENUMSUBJ, new Double(subjSenC));
			output.put(Config.SENTENCENUMOBJ, new Double(objSenC));
			output.put(Config.SENTENCENUMUK, new Double(ukSenC));
			output.put(Config.SENTENCENUM, new Double(totalSenC));
			
			output.put(Config.CTSUBJSUBJ,new Double(ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)]));
			output.put(Config.CTSUBJOBJ,new Double(ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)]));
			output.put(Config.CTSUBJUK,new Double(ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)]));

			output.put(Config.CTOBJSUBJ,new Double(ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)]));
			output.put(Config.CTOBJOBJ,new Double(ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)]));
			output.put(Config.CTOBJUK,new Double(ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)]));

			output.put(Config.CTUKSUBJ,new Double(ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)][Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)]));
			output.put(Config.CTUKOBJ,new Double(ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)][Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)]));
			output.put(Config.CTUKUK,new Double(ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)][Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)]));
		}
		
		
		if(subjSenC!=0){
			recSubj= (ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)])/subjSenC;
			precSubj= (ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.SUBJ)])/pSubjSenC;
		}
		
		if(objSenC!=0){
			recObj= (ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)])/objSenC;
			precObj= (ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)][Config.CONTINGENCYTABLEMAPPING.get(Config.OBJ)])/pObjSenC;
		}
		
		if(ukSenC!=0){
			recUk= (ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)][Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)])/ukSenC;
			precUk= (ctable[Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)][Config.CONTINGENCYTABLEMAPPING.get(Config.UNKNOWN)])/pUkSenC;
		}
		
		output.put(Config.RECSUBJ,new Double(recSubj));
		output.put(Config.RECOBJ,new Double(recObj));
		output.put(Config.RECUK,new Double(recUk));
		output.put(Config.PRECSUBJ,new Double(precSubj));
		output.put(Config.PRECOBJ,new Double(precObj));
		output.put(Config.PRECUK,new Double(precUk));
		
		output.put(Config.ACCURACY,new Double(correctP/totalSenC));		
		output.put(Config.BASELINEACCURACY, new Double(subjSenC/totalSenC));
		
		output.put(Config.ASSIGNEDACCURACY,new Double(correctP/assignedSenC));
		output.put(Config.ASSIGNEDPERCENTAGE, new Double(100*assignedSenC/totalSenC));
		
		
		output.put(Config.FMSUBJ,precSubj==0 && recSubj==0 ? new Double(0) : new Double((2*precSubj*recSubj)/(precSubj+recSubj)));
		output.put(Config.FMOBJ,precObj==0 && recObj==0 ? new Double(0) : new Double((2*precObj*recObj)/(precObj+recObj)));
		output.put(Config.FMUK,precUk==0 && recUk==0 ? new Double(0) : new Double((2*precUk*recUk)/(precUk+recUk)));
		
		return output;
	}
	
	public static double[][] normContingencyTable(double[][] ctable,int tcount){
		double[][] output=new double[ctable.length][ctable.length];
		for(int i=0;i<3;i++){
			for(int j=0;j<3;j++){
				output[i][j]=(double)ctable[i][j]/(double)tcount;	
			}
		}			
		return output;
	}
	
	
	
	public static void writeEval(double[][] ctable) {
		
		HashMap<String,Double> eval = createStatistics(ctable,true);
		HashMap<String,Double> evalC = createStatistics(ctable,false);
		
		FileOutputStream rawFile=null;
		OutputStreamWriter sWriter=null;
		BufferedWriter bWriter=null;
		
		
		try {
			rawFile = new FileOutputStream(Config.EVALFILE);		
			sWriter = new OutputStreamWriter(rawFile,"UTF-8");			
			bWriter = new BufferedWriter(sWriter,4096); // default 2048
			bWriter.append("Total number of Test Sentences="+evalC.get(Config.SENTENCENUM));
			bWriter.newLine();
			bWriter.append("Total number of Subjective Test Sentences="+evalC.get(Config.SENTENCENUMSUBJ));
			bWriter.newLine();
			bWriter.append("Total number of Objective Test Sentences="+evalC.get(Config.SENTENCENUMOBJ));
			bWriter.newLine();
			bWriter.append("Total number of Unclassified Test Sentences="+evalC.get(Config.SENTENCENUMUK));
			bWriter.newLine();
			bWriter.append("Baseline Accuracy (Subjective) ="+evalC.get(Config.BASELINEACCURACY));
			bWriter.newLine();
			bWriter.newLine();
			bWriter.append("          Auto_subj | Auto_obj | Auto_u");
			bWriter.newLine();											
			bWriter.append("          -----------------------------");
			bWriter.newLine();
			bWriter.append("Gold_subj|  "+ Config.DFORMATTER.format(eval.get(Config.CTSUBJSUBJ))+"      "+Config.DFORMATTER.format(eval.get(Config.CTSUBJOBJ))+"      "+Config.DFORMATTER.format(eval.get(Config.CTSUBJUK)));
			bWriter.newLine();
			bWriter.append("Gold_obj |  "+ Config.DFORMATTER.format(eval.get(Config.CTOBJSUBJ))+"      "+Config.DFORMATTER.format(eval.get(Config.CTOBJOBJ))+"      "+Config.DFORMATTER.format(eval.get(Config.CTOBJUK)));
			bWriter.newLine();
			bWriter.append("Gold_u   |  "+ Config.DFORMATTER.format(eval.get(Config.CTUKSUBJ))+"      "+Config.DFORMATTER.format(eval.get(Config.CTUKOBJ))+"      "+Config.DFORMATTER.format(eval.get(Config.CTUKUK)));
			bWriter.newLine();
			bWriter.append("          -----------------------------");
			bWriter.newLine();
			bWriter.newLine();
			bWriter.append("           Subj | Obj | Uk");
			bWriter.newLine();
			bWriter.append("          ------------------");
			bWriter.newLine();
			bWriter.append("Precision|"+Config.DFORMATTER.format(eval.get(Config.PRECSUBJ))+" "+Config.DFORMATTER.format(eval.get(Config.PRECOBJ))+" "+Config.DFORMATTER.format(eval.get(Config.PRECUK)));
			bWriter.newLine();
			bWriter.append("Recall   |"+Config.DFORMATTER.format(eval.get(Config.RECSUBJ))+" "+Config.DFORMATTER.format(eval.get(Config.RECOBJ))+" "+Config.DFORMATTER.format(eval.get(Config.RECUK)));
			bWriter.newLine();
			bWriter.append("F-measure|"+Config.DFORMATTER.format(eval.get(Config.FMSUBJ))+" "+Config.DFORMATTER.format(eval.get(Config.FMOBJ))+" "+Config.DFORMATTER.format(eval.get(Config.FMUK)));
			bWriter.newLine();
			bWriter.append("          ------------------");
			bWriter.newLine();
			bWriter.append("Accuracy:"+Config.DFORMATTER.format(eval.get(Config.ACCURACY)));
			bWriter.newLine();
			bWriter.newLine();
			bWriter.newLine();
			bWriter.append(getHtmlPresentation(evalC, eval));
			
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
	
	public static String getHtmlPresentation(HashMap<String,Double> evalC, HashMap<String,Double> evalN){		
		String output = "<tr><td>"+evalC.get(Config.SENTENCENUM).intValue()+"</td><td>"+evalC.get(Config.SENTENCENUMSUBJ).intValue()+"</td><td>"+evalC.get(Config.SENTENCENUMOBJ).intValue()+"</td><td>"+Config.DFORMATTER.format(evalC.get(Config.BASELINEACCURACY))+"</td><td>"+printConfusionMatrix(evalC)+"</td><td>"+printConfusionMatrixPercentage(evalN)+"</td><td>"+Config.DFORMATTERP.format(evalC.get(Config.PRECSUBJ))+"</td><td>"+Config.DFORMATTERP.format(evalC.get(Config.RECSUBJ))+"</td><td>"+Config.DFORMATTERP.format(evalC.get(Config.FMSUBJ))+"</td><td>"+Config.DFORMATTERP.format(evalC.get(Config.PRECOBJ))+"</td><td>"+Config.DFORMATTERP.format(evalC.get(Config.RECOBJ))+"</td><td>"+Config.DFORMATTERP.format(evalC.get(Config.FMOBJ))+"</td><td>"+Config.DFORMATTER.format(evalC.get(Config.ACCURACY))+"</td><td>"+Config.DFORMATTER.format(evalC.get(Config.ASSIGNEDPERCENTAGE))+"%</td><td>"+Config.DFORMATTER.format(evalC.get(Config.ASSIGNEDACCURACY))+"</td></tr>";
		return output;
	}
	
	
	public static String printConfusionMatrix(HashMap<String,Double> eval){					
		String output = "<table border=\"0\" cellspacing=\"3\"><tr><td></td><td>Psubj</td><td>Pobj</td><td>Pun</td><td></td></tr><tr><td>Gsubj</td><td>"+eval.get(Config.CTSUBJSUBJ).intValue()+"</td><td>"+eval.get(Config.CTSUBJOBJ).intValue()+"</td><td>"+eval.get(Config.CTSUBJUK).intValue()+"</td><td>"+eval.get(Config.SENTENCENUMSUBJ).intValue()+"</td></tr><tr><td>Gobj</td><td>"+eval.get(Config.CTOBJSUBJ).intValue()+"</td><td>"+eval.get(Config.CTOBJOBJ).intValue()+"</td><td>"+eval.get(Config.CTOBJUK).intValue()+"</td><td>"+eval.get(Config.SENTENCENUMOBJ).intValue()+"</td></tr><tr><td></td><td>"+eval.get(Config.PSENTENCENUMSUBJ).intValue()+"</td><td>"+eval.get(Config.PSENTENCENUMOBJ).intValue()+"</td><td>"+eval.get(Config.PSENTENCENUMUK).intValue()+"</td><td>"+eval.get(Config.SENTENCENUM).intValue()+"</td></tr></table>";
		return output;
	}

	
	public static String printConfusionMatrixPercentage(HashMap<String,Double> eval){					
		String output = "<table border=\"0\" cellspacing=\"3\"><tr><td></td><td>Psubj</td><td>Pobj</td><td>Pun</td><td></td></tr><tr><td>Gsubj</td><td>"+Config.DFORMATTER.format(eval.get(Config.CTSUBJSUBJ))+"%</td><td>"+Config.DFORMATTER.format(eval.get(Config.CTSUBJOBJ))+"%</td><td>"+Config.DFORMATTER.format(eval.get(Config.CTSUBJUK))+"%</td><td>"+Config.DFORMATTER.format(eval.get(Config.SENTENCENUMSUBJ))+"%</td></tr><tr><td>Gobj</td><td>"+Config.DFORMATTER.format(eval.get(Config.CTOBJSUBJ))+"%</td><td>"+Config.DFORMATTER.format(eval.get(Config.CTOBJOBJ))+"%</td><td>"+Config.DFORMATTER.format(eval.get(Config.CTOBJUK))+"%</td><td>"+Config.DFORMATTER.format(eval.get(Config.SENTENCENUMOBJ))+"%</td></tr><tr><td></td><td>"+Config.DFORMATTER.format(eval.get(Config.PSENTENCENUMSUBJ))+"%</td><td>"+Config.DFORMATTER.format(eval.get(Config.PSENTENCENUMOBJ))+"%</td><td>"+Config.DFORMATTER.format(eval.get(Config.PSENTENCENUMUK))+"%</td><td>"+Config.DFORMATTER.format(eval.get(Config.SENTENCENUM))+"%</td></tr></table>";
		return output;
	}

	public static double[][] evaluateRuleBased( HashSet<Sentence> evalSentences) {
		double[][] conTable=new double[Config.CONTINGENCYTABLEMAPPING.size()][Config.CONTINGENCYTABLEMAPPING.size()];
		for(Sentence sen : evalSentences){
			conTable[Config.CONTINGENCYTABLEMAPPING.get(sen.getGoldC()).intValue()][Config.CONTINGENCYTABLEMAPPING.get(sen.getHpC()).intValue()]++;
		}
		return conTable;
	}

	/*
	public static double[][] evaluateRuleBased(ArrayList<Doc> docs, HashSet<Sentence> modSentences) {
		double[][] conTable=new double[Config.CONTINGENCYTABLEMAPPING.size()][Config.CONTINGENCYTABLEMAPPING.size()];
		for(Doc doc : docs){
			for(Sentence sen : doc.getSentences()){
				if(modSentences.contains(sen)){
					conTable[Config.CONTINGENCYTABLEMAPPING.get(sen.getGoldC()).intValue()][Config.CONTINGENCYTABLEMAPPING.get(sen.getHpC()).intValue()]++;
				}
			}
		}
		return conTable;
		
	}*/
	
	
	public static void printAverageClueNumber(HashSet<Sentence> modSentences){
		System.out.println(modSentences.size());
		double total=0;
		for(Sentence s : modSentences){
			total+=s.getClues().size();			
		}
		System.out.println("Average number of clues in a sentence: "+total/modSentences.size());
	}
	
}
