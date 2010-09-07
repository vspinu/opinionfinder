package opin.preprocessor.entity;

import java.util.TreeMap;


public class GateDefaultLine {

	public final static String STRINGATTRIBUTE="string";
	public final static String GATETOKEN="GATE_TOKEN";
	public final static String GATESENTENCE="GATE_SENTENCE";
	public final static String CATEGORYATTRIBUTE="category";
	public final static String LEMMAATTRIBUTE="lemma";
	public final static String LENGTHATTRIBUTE="length";
	public final static String KINDATTRIBUTE="kind";
	public final static String ORTHATTRIBUTE="orth";

	String string;
	String type;
	int spanS;
	int spanE;
	TreeMap<String,String> attributes = new TreeMap<String,String>();
	
	public String getString() {
		return string;
	}

	public String getType() {
		return type;
	}

	public TreeMap<String, String> getAttributes() {
		return attributes;
	}
	
	public GateDefaultLine( String string, String lemma, String category, int spanS, int spanE) {
		this.string=string;
		this.spanS=spanS;
		this.spanE=spanE;
		this.type=GATETOKEN;
		
		attributes.put(CATEGORYATTRIBUTE, category);
		attributes.put(LEMMAATTRIBUTE, lemma);
		attributes.put(LENGTHATTRIBUTE, String.valueOf(string.length()));	
		attributes.put(STRINGATTRIBUTE, string);
		if(string.matches("\\W+")){
			attributes.put(KINDATTRIBUTE,"punctuation");
		}
		else{
			attributes.put(KINDATTRIBUTE,"word");
			boolean allUpper=true;
			int i=0;
			while(allUpper && i<string.length()){
				allUpper=allUpper & Character.isUpperCase(string.charAt(i));
				i++;
			}
			if(allUpper){
				attributes.put(ORTHATTRIBUTE, "allCaps");
			}
			else{
				if(Character.isUpperCase(string.charAt(0))){
					attributes.put(ORTHATTRIBUTE, "upperInitial");
				}
				else{
					attributes.put(ORTHATTRIBUTE, "lowercase");
				}			
			}
		}
		
	}
	
	public GateDefaultLine(int spanS, int spanE) {
		this.spanS=spanS;
		this.spanE=spanE;
		this.type=GATESENTENCE;
	}
	
	@Override
	public String toString() {
		String line=null;
		if(type.equals(GATESENTENCE)){
			line="\t"+spanS+","+spanE+"\t"+STRINGATTRIBUTE+"\t"+GATESENTENCE;
		}
		else{
			line="\t"+spanS+","+spanE+"\t"+STRINGATTRIBUTE+"\t"+GATETOKEN;
			for(String key : attributes.keySet()){
				line=line+"\t"+key+"=\""+attributes.get(key)+"\"";
			}
		}
		return line;
	}

}