import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;


public class IOSystem {
	public static int L = 64;
	public static int B = 64;
	
	private byte ldisk[][];
	public IOSystem(){
		this.ldisk = new byte[L][B];
		for (int i = 0; i < L; i++){
			this.ldisk[i] = new byte[B];
			for (int j = 0; j < B; j++){
				this.ldisk[i][j] = -1;
			}
		}
		
	}
	
	public byte[] read_block(int i){
		return ldisk[i];
	}
	
	public void write_block(int i, byte[] p){
		if (p.length == B)
			this.ldisk[i] = p;
	}
	
	public void write_block(char i, byte[] p){
		if (p.length == B)
			this.ldisk[i] = p;
	}
	
	public void printDisk() {
        for (int i=0; i<this.ldisk.length; i++) {
            System.out.print("|ROW: " + i + "|");
            for (int j=0; j<this.ldisk[i].length; j++) {
                System.out.print(" " + this.ldisk[i][j]);
            }
            System.out.print("\r\n");
        }
    }
	
	public void saveDisk(String name) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(name, "UTF-8");
		for (int i=0; i<this.ldisk.length; i++) {
          
            for (int j=0; j<this.ldisk[i].length; j++) {
                writer.print(this.ldisk[i][j]+ " " );
            }
            writer.print("\r\n");
        }
		writer.close();
	}
	
	public void restoreDisk(String name) throws IOException, FileNotFoundException{
		byte[][] ldisk = new byte[64][64];
		BufferedReader reader = new BufferedReader(new FileReader(name));
		String line;
		int i = 0;
		while ((line = reader.readLine()) != null){
			String [] numbers = line.split(" ");
			byte[] block = new byte[64];
			for (int j = 0; j < 64; j++){
				block[j] = ((byte) Integer.parseInt(numbers[j]));
			}
			ldisk[i] = block;
			i++;
		}
		this.ldisk = ldisk;
		reader.close();
	}
	
	public static void main (String args[]) throws FileNotFoundException, IOException{
		IOSystem io = new IOSystem();
		io.restoreDisk("stop.txt");
		io.printDisk();
	}
}
