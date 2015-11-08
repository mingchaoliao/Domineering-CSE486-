package alphabuster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Random;

public class Generator {

	public static void main(String[] args) throws FileNotFoundException {
		Random rnd = new Random(System.currentTimeMillis());
		PrintWriter pw = new PrintWriter(new File("zobrist.txt"));
		long[] a=new long[128];
		for(int i = 0; i < 128; i++) {
			long b = 0;
			while(true) {
				b = rnd.nextLong();
				if(b > 0) break;
				
			}
			a[i]=b;
		}
		int count=0;
		for(int i = 0; i < 64; i++) {
			pw.print(a[count++]);
			pw.print("\t");
			pw.println(a[count++]);
		}
		
		pw.close();
	}

}
