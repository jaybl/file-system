import java.util.Arrays;


public class OFT {
	private Bitmap bm = new Bitmap();
	private IOSystem io = new IOSystem();
	private OFTEntry[] table = new OFTEntry[4];
	private int pos;
	private PackableMemory pm = new PackableMemory(64);
	
	public OFT(IOSystem io, Bitmap bm){
		this.io = io;
		this.bm = bm;
		for (int i = 0; i < 4; i++){
			table[i] = new OFTEntry(-1, -1, null);
		
		}
	}
	
	public int open(String file_name) throws Exception{
		//find file desc index
		byte[] dir = this.io.read_block(1);
		
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
		
		
		
		//find empty oft entry
		int oft = -1;
		for (int i = 1; i < 4; i++){
			if (this.table[i].getIndex() == -1 && this.table[i].getPos() == -1){
				
				oft = i;
				break;
			}
		}
		if (oft == -1)
			throw new Exception();
		//System.out.println(oft);
		
		
		
		
		byte[] fileDesc = this.io.read_block(1);
		byte[] descLoc = Arrays.copyOfRange(fileDesc, pos+4, pos+8);
		
		this.pm.mem = fileDesc;
		int fileIndex = this.pm.unpack(pos+4);
		
		//double check it's not already open
		for (int i = 1; i < 4; i++){
			if (this.table[i].getIndex() == fileIndex){
				throw new Exception();
			}
		}
		
		this.table[oft].setPos(0);
		this.table[oft].setIndex(fileIndex);
		
		int blockNum = fileIndex/4 + 4;
		int slot = fileIndex%4*16;
		descLoc = this.io.read_block(blockNum);
		this.pm.mem = descLoc;
		int len = this.pm.unpack(slot);
		this.table[oft].setLen(len);
		//this.io.printDisk();
		if (len == 0)
		{
			//System.out.println(oft);
			return oft;
		}
		else{
			byte[] block = this.io.read_block(blockNum);
			this.table[oft].setBuffer(block);
		}
		
		return oft;
	}
	
	public int close(int index) throws Exception{
		OFTEntry entry = this.table[index];
		if (entry.getIndex() == -1 || entry.getPos() == -1)
			throw new Exception();
	
		int blockNum = entry.getIndex()/4 + 4;
		int slot = entry.getIndex()%4*16;
		
		byte[] block = entry.getBuffer();
		byte[] fileDesc = this.io.read_block(blockNum);
		
		
		//write buffer to disk with updated length in desc
		if (block != null){
			this.pm.mem = fileDesc;
			this.pm.pack(entry.getLen(), slot);
			this.io.write_block(blockNum, block);
		}
		//free oft entry
		entry.setBuffer(null);
		entry.setPos(-1);
		entry.setIndex(-1);
		
		//System.out.println(blockNum);
		return 0;
	}
	
	public int seek(int index, int pos) throws Exception{
		if (this.table[index].getIndex() == -1 || this.table[index].getPos() == -1){
			throw new Exception();
		}
//		int curPos = this.table[index].getPos();
//		int curBlock = curPos/64;
//		int blockStart = curBlock * 64;
//		int blockEnd = (curBlock + 1)*64;
//		//System.out.println(blockStart + " ** " + blockEnd + " ** " + curBlock);
//		
//		byte[] fileDesc = this.io.read_block(this.table[index].getIndex()/4 + 4);
//		if (pos > blockEnd){
//			this.pm.mem = fileDesc;
//			int slot = this.table[index].getIndex()%4*16;
//			//this means the current block is the 1st, and cursor is moving past
//			if (blockEnd == 64){
//				
//				int i = this.pm.unpack(slot+4);
//				if (i == -1)
//					throw new Exception();
//				byte[] dataBlock = this.io.read_block(i);
//				this.io.write_block(i, this.table[index].getBuffer());
//				
//				int nextBlock = pos/64;
//				//block goes past max
//				if (nextBlock > 2){
//					throw new Exception();
//				}
//				//next block is 2nd
//				else if (nextBlock == 1)
//				{
//					int next = this.pm.unpack(slot+8);
//					
//					//check next block exists
//					if (next == -1){
//						throw new Exception();
//					}
//					this.table[index].setBuffer(this.io.read_block(next));
//				}
//				//next block is 3rd
//				else if (nextBlock == 2){
//					int next = this.pm.unpack(slot+12);
//					
//					//check next block exists
//					if (next == -1){
//						throw new Exception();
//					}
//					this.table[index].setBuffer(this.io.read_block(next));
//				}
//				
//			}
//			//current block is 2nd, cursor going past
//			else if (blockEnd == 128){
//
//				int i = this.pm.unpack(slot+8);
//				if (i == -1)
//					throw new Exception();
//				byte[] dataBlock = this.io.read_block(i);
//				this.io.write_block(i, this.table[index].getBuffer());
//				
//				int nextBlock = pos/64;
//				//block goes past max
//				if (nextBlock > 2){
//					throw new Exception();
//				}
//				
//				//next block is 3rd
//				else if (nextBlock == 2){
//					int next = this.pm.unpack(slot+12);
//					
//					//check next block exists
//					if (next == -1){
//						throw new Exception();
//					}
//					this.table[index].setBuffer(this.io.read_block(next));
//				}
//			}
//		}
//		else if (pos < blockStart){
//			this.pm.mem = fileDesc;
//			int slot = this.table[index].getIndex()%4*16;
//			//current block is 2nd, cursor going to 1st
//			if (blockEnd == 128){
//				int i = this.pm.unpack(slot+8);
//				if (i == -1)
//					throw new Exception();
//				byte[] dataBlock = this.io.read_block(i);
//				this.io.write_block(i, this.table[index].getBuffer());
//				
//				int nextBlock = pos/64;
//				//block goes past max
//				if (nextBlock < 0){
//					throw new Exception();
//				}
//				
//				//next block is 1st
//				else if (nextBlock == 0){
//					int next = this.pm.unpack(slot+4);
//					
//					//check next block exists
//					if (next == -1){
//						throw new Exception();
//					}
//					this.table[index].setBuffer(this.io.read_block(next));
//				}
//			}
//			//current block is 3rd, cursor going before
//			else if (blockEnd == 192){
//				int i = this.pm.unpack(slot+12);
//				if (i == -1)
//					throw new Exception();
//				byte[] dataBlock = this.io.read_block(i);
//				this.io.write_block(i, this.table[index].getBuffer());
//				
//				int nextBlock = pos/64;
//				//block goes past max
//				if (nextBlock < 0){
//					throw new Exception();
//				}
//				
//				//next block is 1st
//				else if (nextBlock == 0){
//					int next = this.pm.unpack(slot+4);
//					
//					//check next block exists
//					if (next == -1){
//						throw new Exception();
//					}
//					this.table[index].setBuffer(this.io.read_block(next));
//				}
//				//next block is 2nd
//				else if (nextBlock == 1){
//					int next = this.pm.unpack(slot+8);
//					
//					//check next block exists
//					if (next == -1){
//						throw new Exception();
//					}
//					this.table[index].setBuffer(this.io.read_block(next));
//				}
//			}
//			
//		}
		this.table[index].setPos(pos);
		System.out.println("position is " + this.table[index].getPos());
		return 0;
	}
	
	public int write(int index, char c, int count) throws Exception{
		if (this.table[index].getIndex() == -1 || this.table[index].getPos() == -1){
			throw new Exception();
		}
		byte[] buffer = this.table[index].getBuffer();
		
		for (int i = 0; i < count; i++){
			//cursor reaches end of file
			if (this.table[index].getPos() >= 192){
				System.out.println("eof");
				return -1;
			}
			int end = (this.table[index].getPos()/64*64 + 64);
			int dataBlock = (this.table[index].getPos()/64);
			//check block exists yet
			
			byte[] fileDesc = this.io.read_block(this.table[index].getIndex()/4 + 4);
			
			
			this.pm.mem = fileDesc;
			int dataIndex = this.pm.unpack(this.table[index].getIndex()*16 + 4*(dataBlock+1));
			
			if (dataIndex == -1){
				dataIndex = this.bm.findZero();
				
				this.pm.pack(dataIndex, this.table[index].getIndex() *16+ 4*(dataBlock+1));
				this.io.write_block(this.table[index].getIndex()/4 + 4, fileDesc);
				this.bm.setOne(dataIndex);
				//this.io.printDisk();
				//this.bm.print();
			}
			//System.out.println(dataIndex);
			
			byte[] block = this.io.read_block(dataIndex);
			
			block[this.table[index].getPos()%64] = (byte)c;
				
			this.io.write_block(dataIndex, block);
			this.table[index].setPos(this.table[index].getPos() + 1);	//increment pos
			this.table[index].incLen();
			this.pm.pack(this.table[index].getLen(), this.table[index].getIndex() *16);
			
			this.io.write_block(this.table[index].getIndex()/4 + 4, fileDesc);
		}
		//this.io.printDisk();
		return 0;
	}
	
	public String read(int index, int count) throws Exception{
		//my implementation already saves in write. so this will just print.
		if (this.table[index].getIndex() == -1 || this.table[index].getPos() == -1){
			throw new Exception();
		}
		String ret = "";
		for (int i = 0; i < count; i++){
			//cursor reaches end of file
			if (this.table[index].getPos() >= 192 || this.table[index].getPos() > this.table[index].getLen()){
				System.out.println("eof");
				return ret;
			}
			int dataBlock = (this.table[index].getPos()/64);
			byte[] fileDesc = this.io.read_block(this.table[index].getIndex()/4 + 4);
			
			
			this.pm.mem = fileDesc;
			int dataIndex = this.pm.unpack(this.table[index].getIndex()*16 + 4*(dataBlock+1));
			byte[] block = this.io.read_block(dataIndex);
			
			System.out.print((char)(block[this.table[index].getPos()%64]));
			ret += (char)(block[this.table[index].getPos()%64]);
			this.table[index].setPos(this.table[index].getPos() + 1);	//increment pos
		}
		System.out.println();
		return ret;
	}
	
	public void empty(){
		for (int i = 0; i < 4; i++){
			table[i] = new OFTEntry(-1, -1, null);
		
		}
	}
}
