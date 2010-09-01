package opin.featurefinder.entity;
import java.util.HashSet;
import java.util.Iterator;


public class OrderRelation extends Relation{
	private int max;
	private int min;
	private int equal;
	
	public OrderRelation(int pre, int fol, boolean force) {
		super(pre,fol);
		this.max=-1;
		this.min=-1;
		this.equal=-1;
		super.setForce(force);
	}
		
	public OrderRelation(int pre, int fol, boolean force, int max, int min, int equal) {
		super(pre,fol);
		this.max=max;
		this.min=min;
		this.equal=equal;
		super.setForce(force);
	}
	
	@Override
	public boolean markHoldingRelationsPositivePositive(HashSet<Token> preCandidates, HashSet<Token> folCandidates, HashSet<Token> toBeRemovedFrom, HashSet<Token> toBeRemovedTo) {		
		boolean found=false;
		toBeRemovedTo.addAll(folCandidates);
		HashSet<Token> fols= new HashSet<Token>();
		Token pre;

		if(equal!=-1){
			for (Iterator<Token> iteratorP = preCandidates.iterator(); iteratorP.hasNext();) {
				pre=iteratorP.next();
				found=false;

				for (Iterator<Token> iteratorF = folCandidates.iterator(); iteratorF.hasNext();) {
					Token token = iteratorF.next();
					int distance = token.getPosition()-pre.getPosition();
					if(distance==equal ^ !super.isForce()){
						pre.addOutgoingConCandidates(new Connection(token,super.getTo(),null,distance));
						fols.add(token);
						token.addIncomingConCandidates(new Connection(pre,super.getFrom(),null,distance));						
						found=true;
						break;
					}
				}
				if(!found){
					toBeRemovedFrom.add(pre);
				}
			}
		}
		else{
			if(min!=-1 && max!=-1){
				for (Iterator<Token> iteratorP = preCandidates.iterator(); iteratorP.hasNext();) {
					pre=iteratorP.next();
					found=false;
					for (Iterator<Token> iterator = folCandidates.iterator(); iterator.hasNext();) {
						Token token = iterator.next();
						int distance=token.getPosition()-pre.getPosition();
						if((distance>=min && distance<=max) ^ !super.isForce()){
							pre.addOutgoingConCandidates(new Connection(token,super.getTo(),null,distance));
							fols.add(token);
							token.addIncomingConCandidates(new Connection(pre,super.getFrom(),null,distance));
							found=true;
						}						
					}
					if(!found){
						toBeRemovedFrom.add(pre);
					}
				}
			}
			else if(min!=-1){
				for (Iterator<Token> iteratorP = preCandidates.iterator(); iteratorP.hasNext();) {
					pre=iteratorP.next();
					found=false;
					for (Iterator<Token> iterator = folCandidates.iterator(); iterator.hasNext();) {
						Token token = iterator.next();
						int distance=token.getPosition()-pre.getPosition();
						if(distance>=min ^ !super.isForce()){
							pre.addOutgoingConCandidates(new Connection(token,super.getTo(),null,distance));
							fols.add(token);
							token.addIncomingConCandidates(new Connection(pre,super.getFrom(),null,distance));
							found=true;
						}						
					}
					if(!found){
						toBeRemovedFrom.add(pre);
					}
				}
			}
			else if(max!=-1){

				for (Iterator<Token> iteratorP = preCandidates.iterator(); iteratorP.hasNext();) {
					pre=iteratorP.next();
					found=false;
					for (Iterator<Token> iterator = folCandidates.iterator(); iterator.hasNext();) {
						Token token = iterator.next();
						int distance=token.getPosition()-pre.getPosition();
						if((distance>0 && distance<=max) ^ !super.isForce()){
							pre.addOutgoingConCandidates(new Connection(token,super.getTo(),null,distance));
							fols.add(token);
							token.addIncomingConCandidates(new Connection(pre,super.getFrom(),null,distance));
							found=true;
						}
					}
					if(!found){
						toBeRemovedFrom.add(pre);
					}
				}
			}
			else{
				for (Iterator<Token> iteratorP = preCandidates.iterator(); iteratorP.hasNext();) {
					pre=iteratorP.next();
					found=false;

					for (Iterator<Token> iterator = folCandidates.iterator(); iterator.hasNext();) {
						Token token = iterator.next();
						int distance=token.getPosition()-pre.getPosition();
						if(pre.getPosition()<token.getPosition() ^ !super.isForce()){
							pre.addOutgoingConCandidates(new Connection(token,super.getTo(),null,distance));
							fols.add(token);
							token.addIncomingConCandidates(new Connection(pre,super.getFrom(),null,distance));
							found=true;
						}
					}
					if(!found){
						toBeRemovedFrom.add(pre);
					}
				}
			}

		}


		if(toBeRemovedFrom.size()==preCandidates.size()){
			return false;
		}

		toBeRemovedTo.removeAll(fols);		
		return true;	

	}
	
	
	
	@Override
	public boolean markHoldingRelationsPositiveNegative(HashSet<Token> preCandidates, HashSet<Token> folCandidates, HashSet<Token> toBeRemovedFrom) {
		Token pre;
		Token token;
		
		if(equal!=-1){
			for (Iterator<Token> iteratorP = preCandidates.iterator(); iteratorP.hasNext();) {
				pre=iteratorP.next();
				for (Iterator<Token> iteratorF = folCandidates.iterator(); iteratorF.hasNext();) {
					token = iteratorF.next();
					int distance = token.getPosition()-pre.getPosition();
					if(distance==equal ^ !super.isForce()){
						toBeRemovedFrom.add(pre);
						break;
					}

				}
			}
		}
		else{
			if(min!=-1 && max!=-1){
				for (Iterator<Token> iteratorP = preCandidates.iterator(); iteratorP.hasNext();) {
					pre=iteratorP.next();
					for (Iterator<Token> iterator = folCandidates.iterator(); iterator.hasNext();) {
						token = iterator.next();
						int distance=token.getPosition()-pre.getPosition();
						if((distance>=min && distance<=max) ^ !super.isForce()){
							toBeRemovedFrom.add(pre);
						}
					}
				}
			}
			else if(min!=-1){
				for (Iterator<Token> iteratorP = preCandidates.iterator(); iteratorP.hasNext();) {
					pre=iteratorP.next();

					for (Iterator<Token> iterator = folCandidates.iterator(); iterator.hasNext();) {
						token = iterator.next();
						int distance=token.getPosition()-pre.getPosition();
						if(distance>=min ^ !super.isForce()){
							toBeRemovedFrom.add(pre);
						}
					}
				}
			}
			else if(max!=-1){
				for (Iterator<Token> iteratorP = preCandidates.iterator(); iteratorP.hasNext();) {
					pre=iteratorP.next();
					for (Iterator<Token> iterator = folCandidates.iterator(); iterator.hasNext();) {
						token = iterator.next();
						int distance=token.getPosition()-pre.getPosition();
						if((distance>0 && distance<=max) ^ !super.isForce()){
							toBeRemovedFrom.add(pre);
						}
					}
				}
			}
			else{
				for (Iterator<Token> iteratorP = preCandidates.iterator(); iteratorP.hasNext();) {
					pre=iteratorP.next();
					for (Iterator<Token> iterator = folCandidates.iterator(); iterator.hasNext();) {
						token = iterator.next();
						if(pre.getPosition()<token.getPosition() ^ !super.isForce()){
							toBeRemovedFrom.add(pre);
						}
					}
				}
			}

		}


		if(toBeRemovedFrom.size()==preCandidates.size()){
			return false;
		}
	
		return true;	
	
	}
	
	
	@Override
	public boolean markHoldingRelationsNegativePositive(HashSet<Token> preCandidates, HashSet<Token> folCandidates, HashSet<Token> toBeRemovedTo) {
		Token pre;
		Token token;
		
		if(equal!=-1){
			for (Iterator<Token> iteratorP = preCandidates.iterator(); iteratorP.hasNext();) {
				pre=iteratorP.next();
				for (Iterator<Token> iteratorF = folCandidates.iterator(); iteratorF.hasNext();) {
					token = iteratorF.next();
					int distance = token.getPosition()-pre.getPosition();
					if(distance==equal ^ !super.isForce()){
						toBeRemovedTo.add(token);
						break;
					}

				}
			}
		}
		else{
			if(min!=-1 && max!=-1){
				for (Iterator<Token> iteratorP = preCandidates.iterator(); iteratorP.hasNext();) {
					pre=iteratorP.next();
					for (Iterator<Token> iterator = folCandidates.iterator(); iterator.hasNext();) {
						token = iterator.next();
						int distance=token.getPosition()-pre.getPosition();
						if((distance>=min && distance<=max) ^ !super.isForce()){
							toBeRemovedTo.add(token);
						}
					}
				}
			}
			else if(min!=-1){
				for (Iterator<Token> iteratorP = preCandidates.iterator(); iteratorP.hasNext();) {
					pre=iteratorP.next();

					for (Iterator<Token> iterator = folCandidates.iterator(); iterator.hasNext();) {
						token = iterator.next();
						int distance=token.getPosition()-pre.getPosition();
						if(distance>=min ^ !super.isForce()){
							toBeRemovedTo.add(token);
						}
					}
				}
			}
			else if(max!=-1){
				for (Iterator<Token> iteratorP = preCandidates.iterator(); iteratorP.hasNext();) {
					pre=iteratorP.next();
					for (Iterator<Token> iterator = folCandidates.iterator(); iterator.hasNext();) {
						token = iterator.next();
						int distance=token.getPosition()-pre.getPosition();
						if((distance>0 && distance<=max) ^ !super.isForce()){
							toBeRemovedTo.add(token);
						}
					}
				}
			}
			else{
				for (Iterator<Token> iteratorP = preCandidates.iterator(); iteratorP.hasNext();) {
					pre=iteratorP.next();
					for (Iterator<Token> iterator = folCandidates.iterator(); iterator.hasNext();) {
						token = iterator.next();
						if(pre.getPosition()<token.getPosition() ^ !super.isForce()){
							toBeRemovedTo.add(token);
						}
					}
				}
			}

		}


		if(toBeRemovedTo.size()==folCandidates.size()){
			return false;
		}
	
		return true;	
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	public int getEqual() {
		return equal;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public void setEqual(int equal) {
		this.equal = equal;
	}
	
	@Override
	public String toString() {
		return super.getFrom()+" "+super.getTo()+" "+equal+" "+max+" "+min;
	}
}
