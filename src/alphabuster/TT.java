package alphabuster;

import java.util.ArrayList;

public class TT {
	private TTEntry[] tt;
	private int size;
	public TT(int size) {
		this.size = size;
		this.tt = new TTEntry[size];
	}
	
	public void store(TTEntry tte) {
		//System.out.println(tt.size());
		
		int i = (int) (tte.getHashkey()%size);
		if(tt[i]==null || tt[i].getDepth() >= tte.getDepth())
			tt[i] = tte;
		
		
	}
	
	public TTEntry getEntry(long hashkey) {
		int i = (int) (hashkey%size);
		if(tt[i] != null) return tt[i];
		return null;
	}
	
	public void print() {
		int blank = 0;
		for(TTEntry t : tt) {
			if(t==null) blank++;
		}
		System.out.println("Blank: "+blank);
	}
}
