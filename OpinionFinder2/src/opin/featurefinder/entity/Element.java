package opin.featurefinder.entity;
import java.util.ArrayList;
import java.util.Iterator;

public class Element {
	private ArrayList<MorphoSyntax> morpho ;
	private boolean force;
	private boolean isInRelation;
	private int id;
	
	//private List<Integer> incomingConnections;
	//private List<Integer> outgoingConnections;
	
	
	public Element(int size, int id) {
		this.morpho=new ArrayList<MorphoSyntax>(size);
		this.force=true;
		this.isInRelation=false;
		this.id=id;
	}
	
	public Element(int size) {
		this.morpho=new ArrayList<MorphoSyntax>(size);
		this.force=true;
		this.isInRelation=false;
	}
	
	public Element(ArrayList<MorphoSyntax> morpho) {
		this.morpho=morpho;
		this.force=true;
		this.isInRelation=false;
	}
	
	public Element(ArrayList<MorphoSyntax> morpho, int id) {
		this.morpho=morpho;
		this.id=id;
		this.force=true;
		this.isInRelation=false;
	}
	
	public void add(MorphoSyntax morphosyntax) {
		morpho.add(morphosyntax);
	}
	

	public void setForce(boolean force) {
		this.force = force;
	}

	public boolean isForce() {
		return force;
	}

	public boolean isInRelation() {
		return isInRelation;
	}


	public void setInRelation(boolean isInRelation) {
		this.isInRelation = isInRelation;
	}

	public ArrayList<MorphoSyntax> getMorpho() {
		return morpho;
	}
	
	@Override
	public String toString() {
		String output="";
		for (Iterator<MorphoSyntax> itr = morpho.iterator(); itr.hasNext();) {
			MorphoSyntax m = (MorphoSyntax) itr.next();			
			output+=m.toString();
			output+=" | ";
		}
		return output;
	}

	public int getId() {
		return id;
	}
	
}
