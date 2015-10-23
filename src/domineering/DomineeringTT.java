package domineering;

import java.util.ArrayList;

public class DomineeringTT {
	private DomineeringTTEntry[] tt;
	private int size;
	public DomineeringTT(int size) {
		this.size = size;
		this.tt = new DomineeringTTEntry[size];
	}
	
	public void store(DomineeringTTEntry tte) {
		//System.out.println(tt.size());
		
		int i = tte.getHashkey()%size;
		if(tt[i]==null || tt[i].getDepth() >= tte.getDepth())
			tt[i] = tte;
		
		
	}
	
	public DomineeringTTEntry getEntry(int hashkey) {
		int i = hashkey%size;
		if(tt[i] != null) return tt[i];
		return null;
	}
}
