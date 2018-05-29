
public class Bitmap {
	private int[] mask = new int[32];
	private int[] bitmap = new int[2]; // 2 ints = 64 bits
	
	public Bitmap(){
		this.mask[31] = 1;
		for (int i = 30; i >= 0; i--)
			this.mask[i] = this.mask[i+1] << 1;
	}
	
	public void setZero(int index){
		this.bitmap[index/32] = this.bitmap[index/32] & ~this.mask[index%32];
	}
	
	public void setOne(int index){
		this.bitmap[index/32] = this.bitmap[index/32] | this.mask[index%32];
	}
	
	public int findZero() {
		for (int i = 0; i < 2; i++){
			for (int j = 0; j < 32; j++){
				if ((this.bitmap[i] & this.mask[j]) == 0){
					return (i*32) + j;
				}
			}
		}
		return -1;
	}

	
	public int findOne(){
		for (int i = 0; i < 2; i++){
			for (int j = 0; j < 32; j++){
				if ((this.bitmap[i] & this.mask[j]) == 1){
					return (i*32) + j;
				}
			}
		}
		return -1;
	}
	
	 public void print() {
	        for (int i=0; i < this.bitmap.length; i++) {
	            System.out.println(Integer.toBinaryString(this.bitmap[i]));
	        }
	    }
	public static void main(String args[])
	{
		Bitmap bs = new Bitmap();
		bs.setOne(2);
		bs.setOne(1);
		bs.setOne(0);
		bs.setOne(4);
		bs.print();
		System.out.println(bs.findZero());
	}
}
