package opin.featurefinder.entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;




public class Matcher {

	
	private Sentence sentence;
	private Entry pattern;
	
	@SuppressWarnings("unchecked")
	private HashSet[] patternCandidates;
	private HashMap<String, HashSet<String>> expansions;

	@SuppressWarnings("unchecked")
	public ArrayList<Match> match(ArrayList<Match> matches){
		boolean consistent=true;
		
		consistent=applyMorphoSyntaxConstraints();
		if(!consistent){
			return null;
		}
		
		
		if(!pattern.isSingle()){
			consistent=applyRelationConstraints();
			if(!consistent){
				return null;
			}	
			decodeMatches(0,new Token[pattern.length()],matches);
		}
		else{		
			
			// For single words there is no reason for decoding and application of syntactic constraints 
			for (Iterator iterator = patternCandidates[0].iterator(); iterator.hasNext();) {
				matches.add(new Match(matches.size(),(Token)iterator.next(), pattern, sentence));
			}
		}
		
	    return matches;
	}
	
	


	private boolean applyMorphoSyntaxConstraints() {
		ArrayList<Element> elements=pattern.getElements();
		Element current=null;
		List<MorphoSyntax> currentMorpho=null;
		HashSet<Token> candidates=null;
		HashSet<Token> im;
		boolean consistent=true;
		
		for(int i=0;i<elements.size();i++){
			current=elements.get(i);
			currentMorpho=current.getMorpho();
			
			if(currentMorpho.size()==1){
				candidates=getCandidatesFromMorphoSyntax(currentMorpho.get(0), current.isForce());
			}
			else{
				candidates=new HashSet<Token>();
				for (Iterator<MorphoSyntax> itrM = currentMorpho.iterator(); itrM.hasNext();) {
					MorphoSyntax m = itrM.next();
					im=getCandidatesFromMorphoSyntax(m, current.isForce());
					if(im!=null){
						candidates.addAll(im);
					}			
				}
			}

			if((candidates==null || candidates.size()==0) && current.isForce()){
				consistent=false;
				break;
			}
			
			if(!current.isForce() && !current.isInRelation() && (candidates==null || candidates.size()==0)){
				consistent=false;
				break;				
			}
		
			patternCandidates[i]=candidates;
		}
		return consistent;
		
	}
	
	private HashSet<Token> getCandidatesFromMorphoSyntax(MorphoSyntax m, boolean force){
		HashSet<Token> candidates=null;
		String word;
		String lemma;
		
		if(m.getWord()!=null){
			word=m.getWord();
			if(expansions.containsKey(word)){
				candidates=getCandidates(m.isWordForce(), expansions.get(word), sentence.getWordMap(), candidates);
			}
			else{
				candidates=getCandidates(m.isWordForce(), word, sentence.getWordMap(), candidates);
			}
			
			if(candidates==null && force){
				return null;
			}
		}
		if(m.getLemma()!=null){
			lemma=m.getLemma();
			if(expansions.containsKey(lemma)){
				candidates=getCandidates(m.isLemmaForce(), expansions.get(lemma), sentence.getLemmaMap(), candidates);
			}
			else{
				candidates=getCandidates(m.isLemmaForce(), lemma, sentence.getLemmaMap(), candidates);
			}
			
			if(candidates==null && force){
				return null;
			}
		}
		if(m.getPos()!=null){
			candidates=getCandidates(m.isPosForce(), m.getPos(), sentence.getPosMap(), candidates);
			if(candidates==null && force){
				return null;
			}
		}
		if(m.getMC()!=null){
			candidates=getCandidates(m.isMCForce(), m.getMC(), sentence.getMCMap(), candidates);
			if(candidates==null && force){
				return null;
			}
		}
		
		return candidates;
		
	}
	
	@SuppressWarnings("unchecked")
	private boolean applyRelationConstraints() {
		boolean consistent=true;
		ArrayList<Element> elements=pattern.getElements();
		ArrayList<Relation> relations=pattern.getRelations();
		Relation curRel;
		
		HashSet<Token> toBeRemovedFrom;
		HashSet<Token> toBeRemovedTo;
		HashSet<Token> fromTokenSet;
		HashSet<Token> toTokenSet;
		
		for(int i=0;i<relations.size();i++){
			curRel=relations.get(i);
			fromTokenSet=patternCandidates[curRel.getFrom()];
			toTokenSet=patternCandidates[curRel.getTo()];
			toBeRemovedFrom=new HashSet<Token>();
			toBeRemovedTo=new HashSet<Token>();
			
			boolean fromForce=elements.get(curRel.getFrom()).isForce();
			boolean toForce=elements.get(curRel.getTo()).isForce();
			
			
			if(fromForce && toForce){
				consistent=curRel.markHoldingRelationsPositivePositive(fromTokenSet, toTokenSet, toBeRemovedFrom, toBeRemovedTo);				
			}
			else if(fromForce && !toForce){
				consistent=curRel.markHoldingRelationsPositiveNegative(fromTokenSet, toTokenSet, toBeRemovedFrom);
			}
			else if(!fromForce && toForce){
				consistent=curRel.markHoldingRelationsNegativePositive(fromTokenSet, toTokenSet, toBeRemovedTo);
			}

			
			if(!consistent){
				break;
			}
			else{
				for (Iterator<Token> iterator = toBeRemovedFrom.iterator(); iterator.hasNext();) {
					Token token = iterator.next();
					consistent=removeCandidate(curRel.getFrom(),token);
					if(!consistent){
						break;
					}
				}
				for (Iterator<Token> iterator = toBeRemovedTo.iterator(); iterator.hasNext();) {
					Token token = iterator.next();
					consistent=removeCandidate(curRel.getTo(),token);
					if(!consistent){
						break;
					}
				}
			}
		}
				
		return consistent;
	}

	private boolean removeCandidate(int index, Token token){
		boolean output;
		Element fromElement=pattern.getElements().get(index);
		Connection c;

		patternCandidates[index].remove(token);
		if(patternCandidates[index].size()==0){
			return false;
		}

		if(fromElement.isForce()){

			if(token.getIncomingConCandidates()!=null){
				for(Iterator<Connection> itr=token.getIncomingConCandidates().iterator();itr.hasNext();){
					c=itr.next();					
					if(patternCandidates[c.getPartnerIndex()].contains(c.getPartner())){
						output=removeCandidate(c.getPartnerIndex(), c.getPartner());
						if(!output){
							return false;
						}
					}
				}				
			}	


			if(token.getOutgoingConCandidates()!=null){
				for(Iterator<Connection> itr=token.getOutgoingConCandidates().iterator();itr.hasNext();){
					c=itr.next();					
					if(patternCandidates[c.getPartnerIndex()].contains(c.getPartner())){
						output=removeCandidate(c.getPartnerIndex(), c.getPartner());
						if(!output){
							return false;
						}
					}
				}				
			}

		}
		
		return true;
	}
	
	
	// Expansions are handled here
	private HashSet<Token> getCandidates(boolean force, String constraint, HashMap<String,ArrayList<Token>> tokenMap, HashSet<Token> candidates){
		if(candidates==null){
			candidates=new HashSet<Token>();
			
			if(force){

					if(tokenMap.containsKey(constraint)){
						
						candidates.addAll(tokenMap.get(constraint));
					}

						
			}
			else{
				
				candidates.addAll(sentence.getTokenList());
				
					if(tokenMap.containsKey(constraint)){						
						candidates.removeAll(tokenMap.get(constraint));
					}
				
				if(candidates.size()==0){
					candidates=null;
				}
			}
		}
		else{
			HashSet<Token> help= new HashSet<Token>();

				if(tokenMap.containsKey(constraint)){
					help.addAll(tokenMap.get(constraint));
				}
			
			if(force){
				if(help.size()==0){
					candidates=null;
				}
				else{
					candidates.retainAll(help);
				}
			}
			else{
				candidates.removeAll(help);
			}
		}
		
		return candidates;
	}
	

	private HashSet<Token> getCandidates(boolean force, HashSet<String> constraint, HashMap<String,ArrayList<Token>> tokenMap, HashSet<Token> candidates){
		String c;
		if(candidates==null){
			candidates=new HashSet<Token>();
			
			if(force){
				for (Iterator<String> itr = constraint.iterator(); itr.hasNext();) {
					c = itr.next();
					if(tokenMap.containsKey(c)){						
						candidates.addAll(tokenMap.get(c));
					}
				}	
			}
			else{				
				candidates.addAll(sentence.getTokenList());
				for (Iterator<String> itr = constraint.iterator(); itr.hasNext();) {
					c = itr.next();
					if(tokenMap.containsKey(c)){						
						candidates.removeAll(tokenMap.get(c));
					}
				}
				if(candidates.size()==0){
					candidates=null;
				}
			}
		}
		else{
			HashSet<Token> help= new HashSet<Token>();
			for (Iterator<String> itr = constraint.iterator(); itr.hasNext();) {
				c = itr.next();
				if(tokenMap.containsKey(c)){
					help.addAll(tokenMap.get(c));
				}
			}
			if(force){
				if(help.size()==0){
					candidates=null;
				}
				else{
					candidates.retainAll(help);
				}
			}
			else{
				candidates.removeAll(help);
			}
		}
		
		return candidates;
	}
	
	
	@SuppressWarnings("unchecked")
	private void decodeMatches(int position, Token[] currentMatch, ArrayList<Match> matches) {

		if(position==currentMatch.length){
			matches.add(new Match(matches.size(), currentMatch, pattern, sentence));
			return;
		}
		if(!pattern.getElements().get(position).isForce()){
			currentMatch[position]=null;
			decodeMatches(position+1, currentMatch, matches);
		}
		else{
		    boolean cont;
		    Token t;
		    
		    if(position != 0){
		    	for (Iterator iterator = patternCandidates[position].iterator(); iterator.hasNext();) {
		    		t = (Token) iterator.next();
		    		cont=true;

		    		if(pattern.isLinear()){	
		    			if(t.getIncomingConCandidates()!=null){
		    				for (Iterator<Connection> iteratorI = t.getIncomingConCandidates().iterator(); iteratorI.hasNext();) {
		    					Connection c = iteratorI.next();
		    					if(currentMatch[c.getPartnerIndex()]!=c.getPartner()){
		    						cont=false;
		    						break;
		    					}
		    				}
		    			}
		    		}
		    		else{
		    			if(t.getIncomingConCandidates()!=null){
		    				for (Iterator<Connection> iteratorI = t.getIncomingConCandidates().iterator(); iteratorI.hasNext();) {
		    					Connection c = iteratorI.next();
		    					if(c.getPartnerIndex()<position){
		    						if(currentMatch[c.getPartnerIndex()]!=c.getPartner()){
		    							cont=false;
		    							break;
		    						}
		    					}
		    				}
		    			}
		    			if(t.getOutgoingConCandidates()!=null && cont){
		    				for (Iterator<Connection> iteratorI = t.getOutgoingConCandidates().iterator(); iteratorI.hasNext();) {
		    					Connection c = iteratorI.next();
		    					if(c.getPartnerIndex()<position){
		    						if(currentMatch[c.getPartnerIndex()]!=c.getPartner()){
		    							cont=false;
		    							break;
		    						}
		    					}
		    				}
		    			}
		    		}
		    		if(cont){
		    			currentMatch[position]=t;
		    			decodeMatches(position+1, currentMatch, matches);
		    		}
		    	}				
			}
		    else{
		    	for (Iterator iterator = patternCandidates[position].iterator(); iterator.hasNext();) {
		    		t = (Token) iterator.next();
		    		currentMatch[position]=t;
	    			decodeMatches(position+1, currentMatch, matches);
		    	}
		    }
		}
	}


	public Sentence getSentence() {
		return sentence;
	}
	public void setSentence(Sentence sentence) {
		this.sentence = sentence;
	}
	public Entry getPattern() {
		return pattern;
	}
	public void setPattern(Entry pattern) {
		this.pattern = pattern;
		patternCandidates=new HashSet[pattern.length()];		
	}




	public void setExpansions(HashMap<String, HashSet<String>> expansions) {
		this.expansions = expansions;
	}
	
	
	
	



}
