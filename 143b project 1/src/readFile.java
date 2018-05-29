import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class readFile {
	public static void main(String[] args) throws IOException{
		Scanner sc = new Scanner(System.in);
		FileSystem fs = new FileSystem();
		System.getProperty("line.separator");
		boolean init = false;
		File file = new File("E:\\proj1\\win\\input01.txt");
		FileReader fileReader = null;
		FileWriter writer = new FileWriter("E:\\95405112.txt");
		BufferedWriter fw = new BufferedWriter(writer);
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		String line;
		
		while ((line = bufferedReader.readLine()) != null){
			try{
				
				
				//System.out.print("% ");
				String input = line;
				System.out.println(input);
				String[] parts = input.split(" ");
				switch(parts[0]){
					case "in":
						
						init = true;
						try{
							String initName = parts[1];
							fs.restoreDisk(initName);
							System.out.println("disk restored");
							fw.write("disk restored\n");
							fw.newLine();
						}catch(Exception e){
							System.out.println("disk initialized");
							fw.write("disk initialized\n");
							fw.newLine();
						}
						
						break;
					case "ex":
						System.out.println("Exiting shell");
						fileReader.close();
						return;
					case "cr":
						if (!init){
							System.out.println("disk not initialized");
							throw new Exception();
							
						}
						String name = parts[1];
						if (name.length() > 4){
							name = name.substring(0, 4);
						}
						fs.create(name);
						System.out.println(name + " created");
						fw.write(name + " created");
						fw.newLine();
						break;
					case "de":
						if (!init){
							System.out.println("disk not initialized");
							throw new Exception();
							
						}
						String name1 = parts[1];
						if (name1.length() > 4){
							name1 = name1.substring(0, 4);
						}
						fs.destroy(name1);
						System.out.println(name1 + " destroyed");
						fw.write(name1 + " destroyed\n");
						fw.newLine();
						break;
					case "sk":
						if (!init){
							System.out.println("disk not initialized");
							throw new Exception();
						}
						String index = parts[1];
						String pos = parts[2];
						fs.seek(Integer.parseInt(index), Integer.parseInt(pos));
						fw.write("position is " + pos + "\n");
						fw.newLine();
						break;
					case "dr":
						if (!init){
							System.out.println("disk not initialized");
							throw new Exception();
						}
						String dr = fs.list();
						fw.write(dr);
						fw.write("\n");
						fw.newLine();
						break;
					case "sv":
						if (!init){
							System.out.println("disk not initialized");
							throw new Exception();
							
						}
						String fileName = parts[1];
						fs.saveDisk(fileName);
						System.out.println("disk saved");
						fw.write("disk saved\n");
						fw.newLine();
						break;
					case "op":
						if (!init){
							System.out.println("disk not initialized");
							throw new Exception();
							
						}
						String openFile = parts[1];
						int oftIndex = fs.open(openFile);
						System.out.println(openFile + " opened " + oftIndex);
						fw.write(openFile + " opened " + oftIndex + "\n");
						fw.newLine();
						break;
					case "cl":
						if (!init){
							System.out.println("disk not initialized");
							throw new Exception();
							
						}
						int oftIndex1 = Integer.parseInt(parts[1]);
						fs.close(oftIndex1);
						System.out.println(oftIndex1 + " closed");
						fw.write(oftIndex1 + " closed\n");
						fw.newLine();
						break;
					case "wr":
						if (!init){
							System.out.println("disk not initialized");
							throw new Exception();
							
						}
						int oftIndex2 = Integer.parseInt(parts[1]);
						char c = parts[2].charAt(0);
						int count = Integer.parseInt(parts[3]);
						fs.write(oftIndex2, c, count);
						System.out.println(count + " bytes written");
						fw.write(count + " bytes written\n");
						fw.newLine();
						break;
					case "rd":
						if (!init){
							System.out.println("disk not initialized");
							throw new Exception();
							
						}
						int oftIndex3 = Integer.parseInt(parts[1]);
						int count1 = Integer.parseInt(parts[2]);
						String rd = fs.read(oftIndex3, count1);
						fw.write(rd);
						fw.write("\n");
						fw.newLine();
						break;
					default:
						fw.write("\n");
						System.out.println("");
						fw.newLine();
						break;
				}
			} catch(Exception e){
				//e.printStackTrace();
				System.out.println("error");
				fw.write("error\n");
				fw.newLine();
			}
		}
		fw.close();
	}
	
}
