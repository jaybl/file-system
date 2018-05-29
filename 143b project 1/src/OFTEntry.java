
public class OFTEntry {
	private int pos;
	private int index;
	private byte[] buffer = new byte[64];
	private int length = 0;
	public OFTEntry(int p, int i, byte[] b){
		this.pos = p;
		this.index = i;
		this.buffer = b;
	}
	
	public void setPos(int p){
		this.pos = p;
	}
	
	public void setIndex(int i){
		this.index = i;
	}
	
	public void setBuffer(byte[]b){
		this.buffer = b;
	}
	
	public int getPos(){
		return this.pos;
	}
	
	public int getIndex(){
		return this.index;
	}
	
	public byte[] getBuffer(){
		return this.buffer;
	}
	
	public void incLen(){
		this.length++;
	}
	
	public void setLen(int len){
		this.length = len;
	}
	
	public int getLen(){
		return this.length;
	}
}
