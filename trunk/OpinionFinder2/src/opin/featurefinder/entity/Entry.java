package opin.featurefinder.entity;
import java.util.ArrayList;
import java.util.HashMap;



public class Entry {

	private ArrayList<Element> elements;
	private ArrayList<Relation> relations;
	private HashMap<String,String> attributes;
	private boolean linear;
	private boolean single;
	int id;
	 
	
	//private boolean[][] relationMatrix;
	//private int[] mostConstrainedIteration;
	
	public Entry(int id, boolean single) {
		this.id=id;		
		if(!single){
			elements=new ArrayList<Element>();
			relations=new ArrayList<Relation>();
		}
		else{
			elements=new ArrayList<Element>(1);
		}
		this.linear=true;
		this.single=single;
		attributes=new HashMap<String, String>();
	}
	
	// does not set linear and isInRelation fields
	public Entry(int id, ArrayList<Element> elements, ArrayList<Relation> relations) {
		this.id=id;	
		this.elements=elements;
		this.relations=relations;
		if(elements.size()==1){
			this.single=true;
		}
		else{
			this.single=false;
		}
		
	}
	
	public void addElement(Element e) {
		elements.add(e);
	}
	
	public void addRelation(Relation r) {
		relations.add(r);
		int from = r.getFrom();
		int to = r.getTo();
		if(from > to){
			linear = false;
		}
		elements.get(from).setInRelation(true);
		elements.get(to).setInRelation(true);		
		//relationMatrix[r.getFrom()][r.getTo()]=true;
	}
	
	public void addPlainRelation(Relation r) {
		relations.add(r);
		int from = r.getFrom();
		int to = r.getTo();
		if(from > to){
			linear = false;
		}
				
		//relationMatrix[r.getFrom()][r.getTo()]=true;
	}
	
	public ArrayList<Element> getElements() {
		return elements;
	}
	
	public ArrayList<Relation> getRelations() {
		return relations;
	}
	
	public int length(){
		return elements.size();
	}

	public boolean isLinear() {
		return linear;
	}

	public void setLinear(boolean linear) {
		this.linear = linear;
	}

	public boolean isSingle() {
		return single;
	}

	public void setSingle(boolean single) {
		this.single = single;
	}

	public int getId() {
		return id;
	}

	public HashMap<String,String> getAttributes() {
		return attributes;
	}

	public void addAttributes(String k, String v) {
		this.attributes.put(k, v);
	}
	
	
	/*
	public void createRelationMatrix(){
		relationMatrix=new boolean[elements.size()][elements.size()];
	}
	
	
	
	public boolean isAnElementInRelation(int index){
		for(int i=0;i<relationMatrix.length;i++){
			if(relationMatrix[i][index] || relationMatrix[index][i]){
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Integer> giveElementsInRelation(int index){
		ArrayList<Integer> output=new ArrayList<Integer>();
		for(int i=0;i<relationMatrix.length;i++){
			if(relationMatrix[i][index] || relationMatrix[index][i]){
				output.add(i);
			}
		}
		return output;
	}
	
	
	public ArrayList<Integer> incomingElements(int index){
		ArrayList<Integer> output=new ArrayList<Integer>();
		for(int i=0;i<relationMatrix.length;i++){
			if(relationMatrix[i][index]){
				output.add(i);
			}
		}
		return output;
	}
	
	public ArrayList<Integer> outgoingElements(int index){
		ArrayList<Integer> output=new ArrayList<Integer>();
		for(int i=0;i<relationMatrix.length;i++){
			if(relationMatrix[index][i]){
				output.add(i);
			}
		}
		return output;
	}
	
	public void computeMostConstrainedIndex(){
		int[] counts=new int[relationMatrix.length];
		for(int i=0;i<relationMatrix.length;i++){
			for(int j=0;j<relationMatrix.length;j++){
				if(relationMatrix[i][j]){
					counts[i]++;
					counts[j]++;
				}
			}
		}
		
		
	}*/
		
}
