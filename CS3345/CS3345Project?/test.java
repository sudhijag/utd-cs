import java.io.*;

public class test{
	public static void main(String args[]) {
		LazyBinarySearchTree BST= new LazyBinarySearchTree();
		
		String inputFileName= args[0];
		String outputFileName= args[1];

		String output="";//im using this method cause the usual line by line output didnt work
		
		try {
			FileInputStream is = new FileInputStream(inputFileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String temp;

			//This part opens the file
			try {
				File outputFile = new File(outputFileName);
				if (outputFile.createNewFile()) {
					System.out.println("We made the file");
				} 
				else {
					System.out.println("File already exists.");
				}
			} 
			catch (IOException e) {
				System.out.println("Failed to create file");
				System.out.println(e);
			}


			
			try {
				FileWriter fw = new FileWriter(outputFileName);
				System.out.println("Done writing.");
			}catch(IOException e) {

			}

			System.out.println("Failed to write to file");
			//System.out.println(e);


			//This part reads from the file
			try {
				while ((temp = br.readLine()) != null)   {
					System.out.println (temp);

					if(temp.contains("Insert:")) {
						System.out.println(">> Inserting");
						//get the number after the insert
						int numbertoinsert=Integer.parseInt(temp.substring(7, temp.length()));
						System.out.println(">> numbertoinsert: "+ numbertoinsert);
						System.out.println();

						if(numbertoinsert < 1 || numbertoinsert > 99) {
							output= (output+ "Error raised in insert: IllegalArgumentException raised" + '\n');
						}
						else {
							boolean isSuccessful= false;
							isSuccessful= BST.insert(numbertoinsert);

							output = (output + isSuccessful + "\n");
							//write(Boolean.toString(isSuccessful), outputFileName);
							System.out.println("Wrote this to file: "+ isSuccessful);
						}
					}
					else if(temp.contains("Delete:")) {
						System.out.println(">> Deleting");
						int numbertodelete=Integer.parseInt(temp.substring(7, temp.length()));
						System.out.println(">> numbertodelete: "+ numbertodelete);
						System.out.println();

						if(numbertodelete < 1 || numbertodelete > 99) {
							output= (output+ "Error raised in delete: IllegalArgumentException raised" + '\n');
						}
						else {
							boolean isSuccessful= false;
							isSuccessful= BST.delete(numbertodelete);

							output = (output + isSuccessful + "\n");
							//write(Boolean.toString(isSuccessful), outputFileName);
							System.out.println("Wrote this to file: "+ isSuccessful);
						}
					}
					else if(temp.contains("Contains:")) {
						System.out.println(">> Finding contain");
						int numbertocontain=Integer.parseInt(temp.substring(9, temp.length()));
						System.out.println(">> numbertocontain: "+ numbertocontain);
						System.out.println();
						
						if(numbertocontain < 1 || numbertocontain > 99) {
							output= (output+ "Error raised in delete: IllegalArgumentException raised" + '\n');
						}
						else {
							boolean isContained=BST.contains(numbertocontain);
	
							
							
							output = (output + isContained + "\n");
							//write(Boolean.toString(isContained), outputFileName);
							System.out.println("Wrote this to file: "+ isContained);
						}
					}
					else if(temp.contains("Size")) {
						System.out.println(">> Finding Size");
						System.out.println();

						int size= BST.size();
						output= (output + size + "\n");
						//write(Integer.toString(size), outputFileName);
						System.out.println("Wrote this to file: "+ size);
					}
					else if(temp.contains("Height")) {
						System.out.println(">> Finding Height");
						System.out.println();

						int height= BST.height();
						output = (output + height + "\n");
						//write(Integer.toString(height), outputFileName);
						System.out.println("Wrote this to file: "+ height);

					}
					else if(temp.contains("FindMin")) {
						System.out.println(">> Finding Min");
						System.out.println();

						int minimum= BST.findMin();
						output = (output + minimum + "\n");
						//write(Integer.toString(minimum), outputFileName);
						System.out.println("Wrote this to file: "+ minimum);

					}
					else if(temp.contains("FindMax")) {
						System.out.println(">> Finding Max");
						System.out.println();


						int maximum= BST.findMax();
						output = (output + maximum + "\n");
						//write(Integer.toString(maximum), outputFileName);
						System.out.println("Wrote this to file: "+ maximum);
					}
					else if(temp.contains("PrintTree")) {
						System.out.println(">> Printing");
						System.out.println();

						String s= BST.traverse2(BST.getRoot());
						output= (output + s+ '\n');
						
						System.out.println(output);
					}
					else {
						output= (output + "Error in line: " + temp + "\n");
						System.out.println();
					}
				}

				is.close();
			}
			catch(Exception e){
				System.out.println(e);
			}
		}
		catch(FileNotFoundException e) {
			System.out.print("We didn't find that file, are you sure you typed it in right?");
		}

		write(output, outputFileName);
	}

	public static void write(String s, String outputFileName) {
		try {
			FileWriter fw = new FileWriter(outputFileName);
			System.out.println("Done writing.");
			fw.write(s);
			fw.close();
		}catch(IOException e) {
			System.out.println(e);
		}
	}
}

