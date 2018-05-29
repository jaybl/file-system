import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;


public class FileSystem {
	private IOSystem ldisk = new IOSystem();
	private Bitmap bitmap = new Bitmap();
	private PackableMemory pm = new PackableMemory(64);
	OFT oft = new OFT(this.ldisk, this.bitmap);
	public FileSystem(){
		for (int i = 0; i < 10; i++){
			this.bitmap.setOne(i);	//reserve bitmap, directory, and file descriptors
		}
	}
	
	
	
	public void create(String file_name) throws Exception {
		
		
		
		byte[] dir = this.ldisk.read_block(1);
		
		for (int i = 0; i < 64; i += 8){
			int stop = 4;
			for (int j = i; j < i+4; j++){
				if (dir[j] == -1){
					stop = j-i;
					break;
				}
			}
			byte[] name = Arrays.copyOfRange(dir, i, i + stop);
			String strName = new String(name);
			
			if (strName.equals(file_name)){
				throw new Exception();	
			}
		}
		
		int freeDesc = 0;
		int freeFileBlock = -1;
		boolean brk = false;
		for (int i = 4; i < 10; i++){
			byte[] desc = this.ldisk.read_block(i);
			for (int j = 0; j < 64; j+=16){
				
				if (desc[j] == -1){
					
					freeFileBlock = i;
					brk = true;
					break;
				}
				freeDesc++;
			}
			if (brk)
				break;
		}
	
		this.bitmap.setOne(freeFileBlock);
		
		//dir entry
		for (int i = 0; i < 64; i+=8){
			if (dir[i] == -1){
				for (int j = 0; j < file_name.length() && j < 4; j++){
					dir[i+j] = (byte) file_name.charAt(j);
					
				}
				if (file_name.length() < 4){
					for (int k = file_name.length() + 1; k < 5; k++){
						dir[i+k] = 0;
					}
				}
				this.pm.mem = dir;
				byte[] x = this.ldisk.read_block(freeFileBlock);
				
				this.pm.pack(freeDesc, i+4);
				break;
			}
		}
		
		byte[] fileDescriptor = this.ldisk.read_block(freeFileBlock);
		int pos = (freeDesc%4)*16;
		
		
		this.ldisk.write_block(1, dir);
		
		this.pm.mem = fileDescriptor;
		this.pm.pack(0, pos);
		
		
		this.ldisk.write_block(freeFileBlock, fileDescriptor);
		
		
	}
	
	public int destroy(String file_name) throws Exception{
		byte[] dir = this.ldisk.read_block(1);
		
		int pos = -1;
		
		for (int i = 0; i < 64; i += 8){
			int stop = 4;
			for (int j = i; j < i+4; j++){
				if (dir[j] == -1){
					stop = j-i;
					break;
				}
			}
			byte[] name = Arrays.copyOfRange(dir, i, i + stop);
			String strName = new String(name);
			
			if (strName.equals(file_name)){
				pos = i;
				
				
				break;
			}
		}
		
		if (pos == -1){
			//System.out.println("Error: File name not found.");
			throw new Exception();
		}
		
		this.pm.mem = dir;
		int index = this.pm.unpack(pos+4);
		//remove dir entry
		for (int i = pos; i < pos+8; i++){
			dir[i] = -1;
		}
		
		this.ldisk.write_block(1, dir);
		
		//remove file desc
		int blockNum = index/4 + 4;
		int slot = index%4;
		
		byte[] fileDesc = this.ldisk.read_block(blockNum);
		
		//mark datablocks as empty
		for (int i = slot*16; i < slot*16 + 16; i+= 4){
			this.pm.mem = fileDesc;
			int dataBlock = pm.unpack(i);
			System.out.println(dataBlock);
			this.bitmap.setZero(dataBlock);
		}
		
		for (int i = slot*16; i < slot*16 + 16; i++){ 
			fileDesc[i] = -1;
		}
		this.ldisk.write_block(blockNum, fileDesc);
		
	
		
		return 0;
	}
	
	public String list(){
		
		byte[] dir = this.ldisk.read_block(1);
		String ret = "";
		for (int i = 0; i < 64; i += 8){
			int stop = 4;
			if (dir[i] != -1){
				for (int j = i; j < i+4; j++){
					if (dir[j] == -1){
						stop = j-i;
						break;
					}
				}
				byte[] name = Arrays.copyOfRange(dir, i, i + stop);
				String strName = new String(name);
				System.out.print(strName + " ");
				ret += strName;
				ret += " ";
			}
		}
		System.out.println("");
		return ret;
		//this.ldisk.printDisk();
	}
	
	
	public int saveDisk(String name) throws FileNotFoundException, UnsupportedEncodingException{
		this.ldisk.saveDisk(name);
		return 0;
	}
	
	public void restoreDisk(String name) throws FileNotFoundException, IOException{
		this.ldisk.restoreDisk(name);
		this.oft.empty();
	}
	
	public Bitmap getBitmap(){
		return this.bitmap;
	}
	public int open(String name) throws Exception{
		
		return this.oft.open(name);
	}
	
	public int close(int index) throws Exception{
		return this.oft.close(index);
	}
	
	public int seek(int index, int pos) throws Exception{
		return this.oft.seek(index, pos);
	}
	
	public int write(int index, char c, int count) throws Exception{
		return this.oft.write(index, c, count);
	}
	
	public String read(int index, int count) throws Exception{
		return this.oft.read(index, count);
	}
	
	public IOSystem getDisk(){
		return this.ldisk;
	}
	public static void main (String args[]) {
		FileSystem fs = new FileSystem();
	
//		fs.create("nuts");
//		fs.create("die");
//		fs.create("no");
//		fs.create("test");
//		fs.create("rex");
//		fs.ldisk.printDisk();
//		fs.bitmap.print();
//		try {
//			fs.destroy("die");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		fs.ldisk.printDisk();
//		fs.create("hi");
//		fs.ldisk.printDisk();
//		fs.list();
	}
}
