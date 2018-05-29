
public class Descriptor {
	private int len;
	private int block1, block2, block3;
	public Descriptor(){
		
	}
	public void setLen(int len){
		this.len = len;
	}
	public void setBlock(int block, int val) throws Exception{
		switch(block){
			case 1:
				this.block1 = val;
			case 2:
				this.block2 = val;
			case 3:
				this.block3 = val;
			default:
				throw new Exception("invalid block number " + block);
		}
	}
	
	public int getLen(){
		return this.len;
	}
	
	public int getBlock(int block) throws Exception{
		switch(block){
			case 1:
				return this.block1;
			case 2:
				return this.block2;
			case 3:
				return this.block3;
			default:
				throw new Exception("invalid block number " + block);
		}
	}
}
