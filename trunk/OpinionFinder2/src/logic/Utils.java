package logic;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

import entity.Span;

public class Utils {
	
	public static TreeMap<String,ArrayList<Span>> takeRandomSubset(TreeMap<String,ArrayList<Span>> spans){
		
		TreeMap<String,ArrayList<Span>> randomSubSet = new TreeMap<String,ArrayList<Span>>(); 
		Random r = new Random();
		
		for(String key : spans.keySet()){
			if(r.nextInt(2)==1){
				randomSubSet.put(key, spans.get(key));
			}
		} 
		
		return randomSubSet;
	}

}
