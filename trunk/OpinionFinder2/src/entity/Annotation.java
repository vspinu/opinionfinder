package entity;

import java.io.Serializable;
import java.util.HashMap;

public class Annotation implements Serializable{
	
	private HashMap<String,String> attributes;
	private String name;
	private String type;
	private int id;
	private int spanS;
	private int spanE;

	
	public Annotation(String name, int id, int start, int end) {
		attributes=new HashMap<String, String>(6);
		this.id=id;
		this.name=name;
		this.spanS=start;
		this.spanE=end;
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}

	public String getSingleAttributes(String key) {
		return attributes.get(key);
	}
	
	public boolean hasAttribute(String key) {
		return attributes.containsKey(key);
	}
	
	public void addToAttributes(String key,String value) {
		attributes.put(key,value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSpanS() {
		return spanS;
	}

	public void setSpanS(int start) {
		this.spanS = start;
	}

	public int getSpanE() {
		return spanE;
	}

	public void setSpanE(int end) {
		this.spanE = end;
	}
	
	public String toString(){
		return "id:"+String.valueOf(id)+" spanS:"+spanS+" name:"+name;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this.spanS==((Annotation)obj).getSpanS() && this.spanE==((Annotation)obj).getSpanE()){
			return true;
		}
		else{
			return false;
		}
	}

}
