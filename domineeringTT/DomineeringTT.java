package domineering;

import java.util.ArrayList;

public class DomineeringTT {
	private ArrayList<DomineeringTTEntry> tt;
	
	public DomineeringTT() {
		this.tt = new ArrayList<DomineeringTTEntry>();
	}
	
	public void store(DomineeringTTEntry tte) {
		//System.out.println(tt.size());
		if(tt.size() > 1024) return;
		tt.add(tte);
	}
	
	public DomineeringTTEntry getEntry(int hashkey) {
		for(int i = 0; i < tt.size(); i++) {
			if(tt.get(i).getHashkey() == hashkey) return tt.get(i);
		}
		return null;
	}
}
