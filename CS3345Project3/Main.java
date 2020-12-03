import java.util.ArrayList;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.time.*;

//import QuickSorter.PivotStrategy;

import java.util.*;


public class Main{
	public static void main(String args[]) {
		int SIZE= Integer.parseInt(args[0]);
		String reportsFilename= args[1];
		String unsortedFilename= args[2];
		String sortedFilename= args[3];
		
		String reportsString= "";
		String unsortedString= "";
		String sortedString= "";
		
		reportsString = reportsString + "Array Size= " + SIZE + "\n";
		
		ArrayList<Integer> array= QuickSorter.GenerateRandomList(SIZE);
		QuickSorter.timedQuickSort(array, QuickSorter.PivotStrategy.FIRST_ELEMENT);
		//QuickSorter.tenpercentshuffle(array);

		ArrayList<Integer> randomarray=new ArrayList<Integer>();
		randomarray.addAll(array); //if we don't do this the array will already be sorted
		ArrayList<Integer> medianofthreearray=new ArrayList<Integer>();
		medianofthreearray.addAll(array); //if we don't do this the array will already be sorted
		ArrayList<Integer> medianofthreerandomarray=new ArrayList<Integer>();
		medianofthreerandomarray.addAll(array); //if we don't do this the array will already be sorted
		
		for(int i=0; i< SIZE; i++) {
			unsortedString = unsortedString + array.get(i)+"\n";
		}
		
		Duration d=null;
		
		if(SIZE < 20) {
			d=QuickSorter.InsertionSort(array);
			reportsString = reportsString + "FIRST_ELEMENT: " + d+ "\n";
			
			d=QuickSorter.InsertionSort(randomarray);
			reportsString = reportsString + "RANDOM_ELEMENT: " + d+ "\n";
			
			d=QuickSorter.InsertionSort(medianofthreearray);
			reportsString = reportsString + "MEDIAN_OF_THREE_ELEMENTS: " + d+ "\n";
			
			d=QuickSorter.InsertionSort(array);
			reportsString = reportsString + "MEDIAN_OF_THREE_RANDOM_ELEMENTS: " + d+ "\n";
		}
		
		d= QuickSorter.timedQuickSort(array, QuickSorter.PivotStrategy.FIRST_ELEMENT);
		reportsString = reportsString + "FIRST_ELEMENT: " + d + "\n";
		
		for(int i=0; i< SIZE; i++) {
			System.out.print(array.get(i)+"\n");
		}
		
		System.out.print("II");
		
		
		d=QuickSorter.timedQuickSort(randomarray, QuickSorter.PivotStrategy.RANDOM_ELEMENT);
		reportsString = reportsString + "RANDOM_ELEMENT: " + d+ "\n";
		
		d=QuickSorter.timedQuickSort(medianofthreearray, QuickSorter.PivotStrategy.MEDIAN_OF_THREE_ELEMENTS);
		reportsString = reportsString + "MEDIAN_OF_THREE_ELEMENTS: " + d+ "\n";
		
		d=QuickSorter.timedQuickSort(medianofthreerandomarray, QuickSorter.PivotStrategy.MEDIAN_OF_THREE_RANDOM_ELEMENTS);
		reportsString = reportsString + "MEDIAN_OF_THREE_RANDOM_ELEMENTS: " + d+ "\n";
		
		for(int i=0; i< SIZE; i++) {
			sortedString = sortedString + array.get(i) +"\n";
		}
		//System.out.println("Duration: " + d);
		
		
		System.out.println("Wrote this to file: ");
		System.out.println(reportsString);
		
			try {
				FileWriter fw = new FileWriter(reportsFilename);
				System.out.println("Done writing.");
				fw.write(reportsString);
				fw.close();
			}catch(IOException e) {
				System.out.println(e);
			}
			
			try {
				FileWriter fw2 = new FileWriter(sortedFilename);
				System.out.println("Done writing.");
				fw2.write(sortedString);
				fw2.close();
			}catch(IOException e) {
				System.out.println(e);
			}
			
			try {
				FileWriter fw3 = new FileWriter(unsortedFilename);
				System.out.println("Done writing.");
				fw3.write(unsortedString);
				fw3.close();
			}catch(IOException e) {
				System.out.println(e);
			}
	}
}