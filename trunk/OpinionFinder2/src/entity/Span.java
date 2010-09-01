package entity;


import java.io.Serializable;


public class Span implements Comparable<Span> ,Serializable{

	private int start;
	private int end;
	private String type;
	private String source;
	private String pol;
	
	
	public Span(int start, int end) {
		this.start=start;
		this.end=end;
	}
	
	public Span(int start, int end, String source) {
		this.start=start;
		this.end=end;
		this.source=source;
	}

	
	/*// Overlapping spans are considered to be equal
	@Override 
	public int compareTo(Span o) {
		if((start>=o.start && start<o.end) || (end>o.start && end<=o.end) || start<o.start && end>o.end){
			return 0;
		}
		else if(start < o.start){
			return -1;
		}
		else if(start > o.start){
			return 1;
		}
		else{
			System.out.println("NOT COMPARABLE SPAN CLASS");
			return 0;
		}		
	}
	
	@Override
	public boolean equals(Object obj) {
		Span s =(Span)obj;
		if((start>=s.start && start<s.end) || (end>s.start && end<=s.end) || start<s.start && end>s.end){
			return true;
		}
		return false;
	}
	*/
	
	@Override 
	public int compareTo(Span o) {
		if(start==o.start && end==o.end){
			return 0;
		}
		else if(start < o.start){
			return -1;
		}
		else if(start > o.start){
			return 1;
		}
		else{
			//System.out.println("NOT COMPARABLE SPAN CLASS"+start+"-"+end+":"+o.start+"-"+o.end);
			return (o.end-o.start)-(end-start);
		}		
	}
	
	@Override
	public boolean equals(Object obj) {
		Span s =(Span)obj;
		if(start==s.start && end==s.end){
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		String s=String.valueOf(start)+"-"+String.valueOf(end);
		return s.hashCode();
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return start+"-"+end+":"+source+":"+type;
	}

	public String getPol() {
		return pol;
	}

	public void setPol(String pol) {
		this.pol = pol;
	}
	
}
