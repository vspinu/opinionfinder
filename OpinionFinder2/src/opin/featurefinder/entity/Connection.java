package opin.featurefinder.entity;
public class Connection {

	private Token partner;
	private int partnerIndex;
	private String type;
	private int distance;
	
	public Connection(Token partner, int partnerIndex, String type, int distance) {
		this.partnerIndex=partnerIndex;
		this.partner=partner;
		this.type=type;
		this.distance=distance;
	}

	public Token getPartner() {
		return partner;
	}

	public String getType() {
		return type;
	}

	public int getDistance() {
		return distance;
	}

	public int getPartnerIndex() {
		return partnerIndex;
	}
	
	@Override
	public String toString() {	
		return partnerIndex+"-"+type+"-"+distance+" | "+partner.toString();
	}
	
}
