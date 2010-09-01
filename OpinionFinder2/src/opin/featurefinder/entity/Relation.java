package opin.featurefinder.entity;
import java.util.HashSet;


public abstract class Relation {
	
	private int from;
	private int to;
	private boolean force;
	
	public Relation(int from, int to) {
		this.from=from;
		this.to=to;
	}
		
	public abstract boolean markHoldingRelationsPositivePositive(HashSet<Token> fromCandidates, HashSet<Token> toCandidates, HashSet<Token> toBeRemovedFrom, HashSet<Token> toBeRemovedTo);

	public abstract boolean markHoldingRelationsPositiveNegative(HashSet<Token> fromCandidates, HashSet<Token> toCandidates, HashSet<Token> toBeRemovedFrom);
	
	public abstract boolean markHoldingRelationsNegativePositive(HashSet<Token> fromCandidates, HashSet<Token> toCandidates, HashSet<Token> toBeRemovedTo);
	
	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	public boolean isForce() {
		return force;
	}

	public void setForce(boolean force) {
		this.force = force;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public void setTo(int to) {
		this.to = to;
	}

}
