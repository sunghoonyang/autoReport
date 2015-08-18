package AutoReport;

import java.util.*;

/**
 * data structure for the data
 * @author Abraxas
 *
 */
public class reportGist {
	String name;
	Double[] score;
	static String[] strengths = {"Entrepreneur", "Competitor", "Learner", "Intellectual", 
									"Socializer", "Friend", "Perfectionist", "Influencer"}; 
	TreeMap<Double, String[]> map;
	String[] val;

	public reportGist(String name, Double[] raw) {
		this.score = raw;
		map = new TreeMap<Double, String[]>();
		
		for (int i=0;i<8;i++) {
			//repeating key case
			while (map.containsKey(raw[i])) {
				raw[i] = raw[i] + 0.01;
			}
			val = new String[2]; val[0] = strengths[i]; val[1] = strengths[i]+".png";
			map.put(raw[i], val);
		}	
	}
	
	public Object[] topFour() {
		Object[] rv = new Object[4];
		Double d = map.lastKey();
		Object first = map.get(d);
		rv[0] = first;
		for (int i=1;i<4;i++) {
			d = map.lowerKey(d);
			rv[i] = map.get(d);
		}
		return rv;
	}
	
	public String[] topFourImages() {
		int i = 0;
		String[] rv = new String[4];
		Object[] vals = this.topFour();
		while(i < 4) {
			String[] value = (String[]) vals[i];
			rv[i] = value[1];
			i++;
		}
		return rv;
	}
	
	public String[] topFourNames() {
		int i = 0;
		String[] rv = new String[4];
		Object[] vals = this.topFour();
		while(i < 4) {
			String[] value = (String[]) vals[i];
			rv[i] = value[0];
			i++;
		}
		return rv;
	}
}
