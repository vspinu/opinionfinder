package opin.featurefinder.entity;
import java.util.HashSet;
import java.util.Iterator;


public class DependencyRelation extends Relation{

	private String type;
	
	public DependencyRelation(int dependent, int head, String type, boolean force) {
		super(dependent,head);
		this.type=type;
		super.setForce(force);
	}
			
	
	@Override
	public boolean markHoldingRelationsPositivePositive(HashSet<Token> depCandidates, HashSet<Token> headCandidates, HashSet<Token> toBeRemovedFrom, HashSet<Token> toBeRemovedTo){
		toBeRemovedTo.addAll(headCandidates);
		HashSet<Token> heads= new HashSet<Token>();
		
		for (Iterator<Token> iterator = depCandidates.iterator(); iterator.hasNext();) {
			Token curDep = iterator.next();
			if(type==null || (curDep.getRelTypeToHead().equals(type) ^ !super.isForce())){
				if(headCandidates.contains(curDep.getHead())){
					Token head=curDep.getHead();
					curDep.addOutgoingConCandidates(new Connection(head,super.getTo(),curDep.getRelTypeToHead(),-1));
					heads.add(head);
					head.addIncomingConCandidates(new Connection(curDep,super.getFrom(),curDep.getRelTypeToHead(),-1));					
				}	
				else{
					toBeRemovedFrom.add(curDep);
				}
			}
			else{
				toBeRemovedFrom.add(curDep);
			}
		}
		
		// QUITTING EARLIER 
		if(toBeRemovedFrom.size()==depCandidates.size()){
			return false;
		}
		
		toBeRemovedTo.removeAll(heads);
		
		return true;
	}
	
	@Override
	public boolean markHoldingRelationsPositiveNegative(HashSet<Token> depCandidates, HashSet<Token> headCandidates, HashSet<Token> toBeRemovedFrom){
		
		
		for (Iterator<Token> iterator = depCandidates.iterator(); iterator.hasNext();) {
			Token curDep = iterator.next();
			if(type==null || (curDep.getRelTypeToHead().equals(type) ^ !super.isForce())){
				if(headCandidates.contains(curDep.getHead())){
					toBeRemovedFrom.add(curDep);				
				}	
			}
		}
		
		// QUITTING EARLIER 
		if(toBeRemovedFrom.size()==depCandidates.size()){
			return false;
		}
		
		return true;
	}
	
	
	@Override
	public boolean markHoldingRelationsNegativePositive(HashSet<Token> depCandidates, HashSet<Token> headCandidates, HashSet<Token> toBeRemovedTo){
		
		for (Iterator<Token> iterator = depCandidates.iterator(); iterator.hasNext();) {
			Token curDep = iterator.next();
			if(type==null || (curDep.getRelTypeToHead().equals(type) ^ !super.isForce())){
				if(headCandidates.contains(curDep.getHead())){		
					toBeRemovedTo.add(curDep.getHead());
				}	
			}
		}
		
		// QUITTING EARLIER 
		if(toBeRemovedTo.size()==headCandidates.size()){
			return false;
		}
		
		return true;
	}


	public String getType() {
		return type;
	}
	
	@Override
	public String toString() {
		return super.getFrom()+" "+super.getTo()+" "+type;
	}

}
