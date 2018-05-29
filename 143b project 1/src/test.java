import java.util.Scanner;


public class test {
	public static void main(String[] args){
		Scanner sc = new Scanner(System.in);
		FileSystem fs = new FileSystem();
		
		boolean init = false;
		while (true){
			try{
				System.out.print("% ");
				String input = sc.nextLine();
				String[] parts = input.split(" ");
				switch(parts[0]){
					case "in":
						
						init = true;
						try{
							String initName = parts[1];
							fs.restoreDisk(initName);
							System.out.println("disk restored");
						}catch(Exception e){
							System.out.println("disk initialized");
						}
						
						break;
					case "ex":
						System.out.println("Exiting shell");
						return;
					case "cr":
						if (!init){
							System.out.println("disk not initialized");
							break;
						}
						String name = parts[1];
						if (name.length() > 4){
							name = name.substring(0, 4);
						}
						fs.create(name);
						System.out.println(name + " created");
						break;
					case "de":
						if (!init){
							System.out.println("disk not initialized");
							break;
						}
						String name1 = parts[1];
						if (name1.length() > 4){
							name1 = name1.substring(0, 4);
						}
						fs.destroy(name1);
						System.out.println(name1 + " destroyed");
						break;
					case "sk":
						if (!init){
							System.out.println("disk not initialized");
							break;
						}
						String index = parts[1];
						String pos = parts[2];
						fs.seek(Integer.parseInt(index), Integer.parseInt(pos));
						//System.out.println("position is " + pos);
						break;
					case "dr":
						if (!init){
							System.out.println("disk not initialized");
							break;
						}
						fs.list();
						break;
					case "sv":
						String fileName = parts[1];
						fs.saveDisk(fileName);
						System.out.println("disk saved");
						break;
					case "op":
						String openFile = parts[1];
						int oftIndex = fs.open(openFile);
						System.out.println(openFile + " opened " + oftIndex);
						break;
					case "cl":
						int oftIndex1 = Integer.parseInt(parts[1]);
						fs.close(oftIndex1);
						System.out.println(oftIndex1 + " closed");
						break;
					case "wr":
						int oftIndex2 = Integer.parseInt(parts[1]);
						char c = parts[2].charAt(0);
						int count = Integer.parseInt(parts[3]);
						fs.write(oftIndex2, c, count);
						System.out.println(count + " bytes written");
						break;
					case "rd":
						int oftIndex3 = Integer.parseInt(parts[1]);
						int count1 = Integer.parseInt(parts[2]);
						fs.read(oftIndex3, count1);
						break;
					default:
						System.out.println("");
						break;
				}
			} catch(Exception e){
				//e.printStackTrace();
				System.out.println("error");
			}
		}
	}
}
