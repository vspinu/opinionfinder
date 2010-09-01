package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Config {
   
	public final static String IDTOKEN="ID";
	public final static File CONFIGFILE = new File("/home/cem/workspace/SentenceClassifier/Config.txt");
		
	public final static String[] STRENGTHFORSUBJARR = {"extreme","high","medium"};
	public final static Pattern LINETOKENIZER= Pattern.compile("\\s+(?=([^\"]*?(=\"|$)))");
	public final static Pattern ATTRIBUTETOKENIZER=Pattern.compile("(\\S+)=\"(.*)\"");
	public final static Pattern FEATUREERASER=Pattern.compile("[^\\-\\sa-z]");
	
	public final static String MANANNDIRECTORY="man_anns";
	public final static String AUTOANNDIRECTORY="auto_anns";
	public final static String DOCDIRECTORY="docs";
	public final static String DOCLISTDIRECTORY="doc_lists";
	
	public static HashSet<String> GOLDSTRENGTHFORSUBJ;
	
	public static HashSet<String> SUNDANCESUBJTYPES;
	public static HashSet<String> SUNDANCEOBJTYPES;
	public static HashSet<String> STRONGSUBJTYPES;
	public static HashSet<String> WEAKSUBJTYPES;
	public static HashSet<String> PATTERNSUBJTYPES;
	public static HashSet<String> PATTERNOBJTYPES;
	
	public final static String NAME="name";
	public final static String SPANS="spanS";
	public final static String SPANE="spanE";
	public final static String SUBJ="subj";
	public final static String OBJ="obj";
	public final static String UNKNOWN="unknown";
	public static String MANANNFILE="";
	public static String[] AUTOANNFILES;
	public static String AUTOCLASSFILE="";
	public static String GOLDCLASSFILE="";
	public static String SENTENCESPLITFILE="";
	public static String GATEDEFAULT="";
	
	public static File DATABASE;
	public final static File EVALFILE=new File("rb_evalNew.txt");
	public final static File LOGFILE=new File("log.dat");	
	public static File DOCLISTFILE;
	
	public static boolean EVALUATE;
	public static boolean LOG;
	public static boolean WRITEGOLD;
	public static boolean WRITEHP;
	public static boolean WRITESPLIT;
	public static String OP;
	public static String CLASSIFIER;
	public static File EXCLUDESPANFILE;

		
	public final static String ANNOTATIONNAME="matchfeat";
	public final static String ANNOTATIONATT="type";
	public final static String ANNOTATIONNAMEFORSUBJPATTERN="SundancePattern:total:5:prob:0.95";
	public final static String ANNOTATIONNAMEFOROBJPATTERN="SundancePattern:total:5:prob:lte0.15";
	
	
	
	public final static String GOLDCLASSATT="goldclass";
	public final static String AUTOCLASSATT="autoclass";
	
	public final static String ARFFHEADERNOMINAL="@RELATION subjsc\n\n@ATTRIBUTE instance string\n@ATTRIBUTE strong {=0,=1,>=2}\n@ATTRIBUTE weak {=0,=1,>=2}\n@ATTRIBUTE patternsubj {=0,=1,>=2}\n@ATTRIBUTE patternobj {=0,=1,>=2}\n@ATTRIBUTE modal {=0,=1,>=2}\n@ATTRIBUTE cardinal {=0,=1,>=2}\n@ATTRIBUTE pronoun {=0,=1,>=2}\n@ATTRIBUTE adj {=0,=1,>=2}\n@ATTRIBUTE adverb {=0,=1,>=2}\n@ATTRIBUTE strongA {=0,=1,>=2}\n@ATTRIBUTE weakA {=0,=1,>=2}\n@ATTRIBUTE patternsubjA {=0,=1,>=2}\n@ATTRIBUTE patternobjA {=0,=1,>=2}\n@ATTRIBUTE modalA {=0,=1,>=2}\n@ATTRIBUTE cardinalA {=0,=1,>=2}\n@ATTRIBUTE pronounA {=0,=1,>=2}\n@ATTRIBUTE adjA {=0,=1,>=2}\n@ATTRIBUTE adverbA {=0,=1,>=2}\n@ATTRIBUTE class {subj,obj}\n\n@DATA";
	public final static String ARFFHEADERNUMERIC="@RELATION subjsc\n\n@ATTRIBUTE instance string\n@ATTRIBUTE strong NUMERIC\n@ATTRIBUTE weak NUMERIC\n@ATTRIBUTE patternsubj NUMERIC\n@ATTRIBUTE patternobj NUMERIC\n@ATTRIBUTE modal NUMERIC\n@ATTRIBUTE cardinal NUMERIC\n@ATTRIBUTE pronoun NUMERIC\n@ATTRIBUTE adj NUMERIC\n@ATTRIBUTE adverb NUMERIC\n@ATTRIBUTE strongA NUMERIC\n@ATTRIBUTE weakA NUMERIC\n@ATTRIBUTE patternsubjA NUMERIC\n@ATTRIBUTE patternobjA NUMERIC\n@ATTRIBUTE modalA NUMERIC\n@ATTRIBUTE cardinalA NUMERIC\n@ATTRIBUTE pronounA NUMERIC\n@ATTRIBUTE adjA NUMERIC\n@ATTRIBUTE adverbA NUMERIC\n@ATTRIBUTE class {subj,obj}\n\n@DATA";
	
	public final static DecimalFormat DFORMATTERP = new DecimalFormat("0.000");
	public final static DecimalFormat DFORMATTER = new DecimalFormat("0.00");
	
	public static int SIMPLEOBJTESTSTRONG;
	public static int SIMPLEOBJTESTWEAK;
	
	public static HashMap<String,Integer> CONTINGENCYTABLEMAPPING;
	public final static String[] CONTINGENCYTABLEMAPPINGARR = {"subj","obj","unknown"};
	
	// Constants for Statistics
	public final static String ACCURACY="acc";
	public final static String BASELINEACCURACY="baseacc";
	public final static String SENTENCENUM="sennum";
	public final static String SENTENCENUMOBJ="objsennum";
	public final static String SENTENCENUMSUBJ="subjsennum";
	public final static String SENTENCENUMUK="uksennum";
	public final static String CTSUBJOBJ="subjobj";
	public final static String CTSUBJSUBJ="subjsubj";
	public final static String CTSUBJUK="subjuk";
	public final static String CTOBJSUBJ="objsubj";
	public final static String CTOBJOBJ="objobj";
	public final static String CTOBJUK="objuk";
	public final static String CTUKSUBJ="uksubj";
	public final static String CTUKOBJ="ukobj";
	public final static String CTUKUK="ukuk";
	public final static String PRECSUBJ="precsubj";
	public final static String PRECOBJ="precobj";
	public final static String PRECUK="precuk";
	public final static String RECSUBJ="recsubj";
	public final static String RECOBJ="recobj";
	public final static String RECUK="recuk";
	public final static String FMSUBJ="fmsubj";
	public final static String FMOBJ="fmobj";
	public final static String FMUK="fmuk";
	public static final String PSENTENCENUMSUBJ = "psubjsennum";
	public static final String PSENTENCENUMOBJ = "pobjsennum";
	public static final String PSENTENCENUMUK = "puksennum";
	public static final String ASSIGNEDACCURACY = "assignedaccuracy";
	public static final String ASSIGNEDPERCENTAGE = "assignedpercentage";
	
	public static boolean READGATEDEFAULT;
	public static boolean READAUTOANN;
	public static boolean READMANANN;
	
	public static void initialize(){
		System.out.println("Reading Config!");
		String[] tokens;
		HashMap<String,String> pairs=readConfig(CONFIGFILE);	
	
						
			DATABASE=new File(pairs.get("database"));
			if(!DATABASE.exists()){
				System.err.println("database directory does not exist!");
				System.exit(-1);
			}
			DOCLISTFILE=new File(DATABASE.getAbsolutePath()+File.separator+DOCLISTDIRECTORY+File.separator+pairs.get("doclistfile"));
			if(!DOCLISTFILE.exists()){
				System.err.println("doclist file does not exist!");
				System.exit(-1);
			}

			STRONGSUBJTYPES=new HashSet<String>();
			if(pairs.containsKey("strongsubj")){
				tokens=pairs.get("strongsubj").split(",");				
				for(int i=0;i<tokens.length;i++){
					STRONGSUBJTYPES.add(tokens[i].trim());
				} 
			}

			WEAKSUBJTYPES=new HashSet<String>();
			if(pairs.containsKey("weaksubj")){
				tokens=pairs.get("weaksubj").split(",");				
				for(int i=0;i<tokens.length;i++){
					WEAKSUBJTYPES.add(tokens[i].trim());
				}
			}

			SUNDANCESUBJTYPES=new HashSet<String>();
			if(pairs.containsKey("sundancesubj")){
				tokens=pairs.get("sundancesubj").split(",");				
				for(int i=0;i<tokens.length;i++){
					SUNDANCESUBJTYPES.add(tokens[i].trim());
				} 
			}
			
			SUNDANCEOBJTYPES=new HashSet<String>();
			if(pairs.containsKey("sundanceobj")){
				tokens=pairs.get("sundanceobj").split(",");				
				for(int i=0;i<tokens.length;i++){
					SUNDANCEOBJTYPES.add(tokens[i].trim());
				}
			}
			
			GOLDSTRENGTHFORSUBJ=new HashSet<String>();
			GOLDSTRENGTHFORSUBJ.addAll(Arrays.asList(STRENGTHFORSUBJARR));
			
			
		
			EVALUATE=pairs.get("evaluate").equals("yes");
			LOG=pairs.get("log").equals("yes");
			WRITEHP=pairs.get("log").equals("yes");
			if(WRITEHP){
				if(!(pairs.containsKey("autoclassfile") || pairs.get("autoclassfile").trim().equals(""))){
					System.err.println("Config File is incomplete: autoclassfile parameter is not defined!");
				    System.exit(-1);
				}
				AUTOCLASSFILE=pairs.get("autoclassfile");
			}
			WRITEGOLD=pairs.get("gold").equals("yes");
			if(WRITEGOLD){
				if(!(pairs.containsKey("goldclassfile") || pairs.get("goldclassfile").trim().equals(""))){
					System.err.println("Config File is incomplete: goldclassfile parameter is not defined!");
				    System.exit(-1);
				}
				GOLDCLASSFILE=pairs.get("goldclassfile");
			}
			WRITESPLIT=pairs.get("split").equals("yes");
			if(WRITESPLIT){
				if(!(pairs.containsKey("sentencesplitfile") || pairs.get("sentencesplitfile").trim().equals(""))){
					System.err.println("Config File is incomplete: sentencesplitfile parameter is not defined!");
				    System.exit(-1);
				}
				SENTENCESPLITFILE=pairs.get("sentencesplitfile");
			}
						
			READMANANN=Boolean.parseBoolean(pairs.get("readManAnn"));
			READAUTOANN=Boolean.parseBoolean(pairs.get("readAutoAnn"));
			READGATEDEFAULT=Boolean.parseBoolean(pairs.get("readGatedefault"));
			
			if(READAUTOANN){
				AUTOANNFILES=pairs.get("autoannfiles").split(",");
				for(int i=0;i<AUTOANNFILES.length;i++){
					AUTOANNFILES[i]=AUTOANNFILES[i].trim();		    	
				}
			}
		    
			if(READGATEDEFAULT){
		    	GATEDEFAULT=pairs.get("gatedefaultfile").trim();		    
			}
			
			if(READMANANN){
				MANANNFILE=pairs.get("manannfile");
			}
			
		    SIMPLEOBJTESTSTRONG=Integer.parseInt(pairs.get("simpleObjTestStrong"));
			SIMPLEOBJTESTWEAK=Integer.parseInt(pairs.get("simpleObjTestWeak"));
			
			if(pairs.containsKey("excludeSpanFile") && pairs.get("excludeSpanFile").trim().length()>0){
				EXCLUDESPANFILE=new File(pairs.get("excludeSpanFile").trim());
				if(!EXCLUDESPANFILE.exists()){
					System.err.println("Exclude Span File does not exist!");
				    System.exit(-1);
				}
			}
			
			CONTINGENCYTABLEMAPPING=new HashMap<String,Integer>();
			for(int i=0;i<CONTINGENCYTABLEMAPPINGARR.length;i++){
				CONTINGENCYTABLEMAPPING.put(CONTINGENCYTABLEMAPPINGARR[i],new Integer(i));
			}
	}
	
	
	public static HashMap<String,String> readConfig(File configFile){
		
		FileInputStream rawFile=null;
		InputStreamReader sReader=null;
		BufferedReader bReader=null;
		String line;
		String[] tokens;
		HashMap<String,String> output=new HashMap<String, String>();
		
		if(!configFile.exists()){
			System.err.println("Config File does not exist!");
			System.exit(-1);
		}
		
		try {
			rawFile = new FileInputStream(configFile);		
			sReader = new InputStreamReader(rawFile,"UTF-8");			
			bReader = new BufferedReader(sReader); // default 2048
			
			while((line=bReader.readLine())!=null){
				line=line.trim();
				if(!line.equals("") && !line.startsWith("#")){
					tokens=line.split("=");
					output.put(tokens[0],tokens[1]);				
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
