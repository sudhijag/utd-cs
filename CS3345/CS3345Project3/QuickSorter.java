import java.util.*;
import java.time.*;

public final class QuickSorter{
	
	/*
	 * Function that generates a random arrayList 
	 * @param	size: the size of the random array to be generated
	 * @return	ourList: the generated list
	 * */
	static ArrayList<Integer> GenerateRandomList(int size){
		ArrayList<Integer> ourList =new ArrayList<Integer>(size);
		
		if(size < 0) {
			System.out.print("Given size should be non-negative");
			throw new IllegalArgumentException();
		}
		//You should use java.util.Random to generate integral values uniformly across the entire range of the int data type.
		else {
			Random generator= new Random();
			
			for(int i=0; i< size; i++) {
				ourList.add(generator.nextInt());
			}
		}
		
		//for(int i=0; i< size; i++) {
			//System.out.print(ourList.get(i));
		//}
		
		return ourList;
	}
	
	/*
	 * Function that swaps two random elements 10%-of-the-array-size times
	 * @param	array: the arrayList to be shuffled
	 * */
	public static <E extends Comparable<E>> void tenpercentshuffle(ArrayList<E> array) {
		int numberOfShuffles=array.size()/10;
		Random generator= new Random();
		
		for(int i=0; i < numberOfShuffles; i++) {
			int indexToSwap1= generator.nextInt(array.size()-1); 
			int indexToSwap2= generator.nextInt(array.size()-1); 
			
			E temp= array.get(indexToSwap1);
			array.set(indexToSwap1, array.get(indexToSwap2));
			array.set(indexToSwap2, temp);
		}
	}
	
	/*
	 * Function that performs insertion sort if the array is small
	 * @param	array: the arrayList to be insertion sorted
	 * @return	duration: the time taken to sort the array
	 * */
	public static <E extends Comparable<E>> Duration InsertionSort(ArrayList<E> array) {
		long startingtime = 0;
		long finishingtime = 0;

		startingtime = System.nanoTime();
		
		for(int i=1; i< array.size(); i++) {
			E saved_value= array.get(i);
			int j= i-1;
			
			
			while( (j>=0) && (array.get(j).compareTo(saved_value) > 0) ){
				//shift right
				array.set(j+1, array.get(j));
				j--;
			}
			array.set(j+1, saved_value);
		}
		
		finishingtime = System.nanoTime(); 

		Duration duration = Duration.ofNanos(finishingtime - startingtime);
		return duration;
	}
	
	/*
	 * Function that calls the various quick-sort functions, times execution
	 * @param	array: the arrayList to be quick-sorted
	 * @param	ps: the pivot strategy to be used like RANDOM_ELEMENT
	 * @return	duration: the time taken to sort the array
	 * */
	public static <E extends Comparable<E>> Duration timedQuickSort(ArrayList<E> array, PivotStrategy ps) {
		if(array == null || ps == null) {  
			System.out.println("At least one of your arguments were null");
			throw new NullPointerException();
		}
		long startingtime = 0;
		long finishingtime = 0;

		startingtime = System.nanoTime();
		
		int left= 0;
		int right= array.size()-1;

		if(ps == PivotStrategy.FIRST_ELEMENT) {
			E temp1= array.get(left);
			array.set(left, array.get(right));
			array.set(right, temp1);

			first_element_quicksort(array, 0, array.size()-1);
		}
		else if(ps == PivotStrategy.RANDOM_ELEMENT) {
			random_element_quicksort(array, 0, array.size()-1);
		}
		else if(ps == PivotStrategy.MEDIAN_OF_THREE_RANDOM_ELEMENTS) {
			three_random_quicksort(array, 0, array.size()-1);
			
		}
		else if(ps == PivotStrategy.MEDIAN_OF_THREE_ELEMENTS) {
			three_quicksort(array, 0, array.size()-1);
		}

		finishingtime = System.nanoTime(); 

		Duration duration = Duration.ofNanos(finishingtime - startingtime);
		return duration;
	}
	
	/*
	 * Recursive function that chooses the pivot to be the first element
	 * @param	array: the arrayList to be insertion sorted
	 * @param	left: the left boundary index of the subarray
	 * @param	right: the right boundary index of the subarray
	 * @return	duration: the time taken to sort the array
	 * */
	 private static <E extends Comparable<E>> ArrayList<E> first_element_quicksort(ArrayList<E> array, int left, int right){
		    int saved_left= left;
		  	int saved_right=right;
		  
		  	int pivot= right;
			
			for(int i= left; i< right; i++) {
				if(array.get(i) .compareTo(array.get(pivot)) < 0) {
					//swap left and i
					E temp= array.get(left);
					array.set(left, array.get(i));
					array.set(i, temp);
					
					left++;
				}
			}
			//After we are done looping, swap left and pivot
			E temp= array.get(left);
			array.set(left, array.get(pivot));
			array.set(pivot, temp);
			
			//return the pivot index
			pivot=left;
		  
			left= saved_left;
		  	right=saved_right;
			
	        //if there are at least two unsorted elements between left and pivot
	        if( (left == pivot) || (left+1 == pivot) ) {
	           
	        }
	        else {
	        	first_element_quicksort(array, left, pivot-1);
	        }
	        //if there are at least two unsorted elements between pivot and right
	        if( (pivot== right) || (pivot+1 == right)) {
	            
	        }
	        else {
	        	first_element_quicksort(array, pivot + 1, right);
	        }
	        
	        return array;
	    }
	 	
	 	/*
		 * Recursive function that chooses the pivot to be a random element
		 * @param	array: the arrayList to be insertion sorted
		 * @param	left: the left boundary index of the subarray
		 * @param	right: the right boundary index of the subarray
		 * @return	duration: the time taken to sort the array
		 * */
	 private static <E extends Comparable<E>> ArrayList<E> random_element_quicksort(ArrayList<E> array, int left, int right){
		 	Random generator= new Random();
		 	int random_pivot= generator.nextInt(array.size()-1);
			
			//swap random_pivot and right
			E temp1= array.get(random_pivot);
			array.set(random_pivot, array.get(right));
			array.set(right, temp1);
		 
		 	int saved_left= left;
		  	int saved_right=right;
		  
		  	int pivot= right;
			
			for(int i= left; i< right; i++) {
				if(array.get(i) .compareTo(array.get(pivot)) < 0) {
					//swap left and i
					E temp= array.get(left);
					array.set(left, array.get(i));
					array.set(i, temp);
					
					left++;
				}
			}
			//After we are done looping, swap left and pivot
			E temp= array.get(left);
			array.set(left, array.get(pivot));
			array.set(pivot, temp);
			
			//return the pivot index
			pivot=left;
		  
			left= saved_left;
		  	right=saved_right;
			
	        //if there are at least two unsorted elements between left and pivot
	        if( (left == pivot) || (left+1 == pivot) ) {
	           
	        }
	        else {
	        	first_element_quicksort(array, left, pivot-1);
	        }
	        //if there are at least two unsorted elements between pivot and right
	        if( (pivot== right) || (pivot+1 == right)) {
	            
	        }
	        else {
	        	first_element_quicksort(array, pivot + 1, right);
	        }
	        
	        return array;
	    }
	 
	 /*
		 * Recursive function that chooses the pivot to be a the median of three random elements
		 * @param	array: the arrayList to be insertion sorted
		 * @param	left: the left boundary index of the subarray
		 * @param	right: the right boundary index of the subarray
		 * @return	duration: the time taken to sort the array
		 * */
	 private static <E extends Comparable<E>> ArrayList<E> three_random_quicksort(ArrayList<E> array, int left, int right){
		 	Random generator= new Random();
		 
		 	int one= generator.nextInt(array.size()-1);
			int two= generator.nextInt(array.size()-1);
			int three= generator.nextInt(array.size()-1);
			
			int middle= 0;
			System.out.println("one: " + one + "two: "+ two + "three: "+ three);
			
			if (array.get(one).compareTo(array.get(two)) > 0) {
				//now try to place c
				if(array.get(three).compareTo(array.get(two)) < 0) {
					//System.out.println(two +"is the middle element");
					middle=two;
				}
				else if(array.get(three).compareTo(array.get(one)) < 0) {
					//System.out.println(three + "is the middle element");
					middle=three;
				}
				else {
					//System.out.println(one + "is the middle element");
					middle=one;
				}
			} 
			else {
				  if (array.get(three).compareTo(array.get(one)) < 0) {
					  //System.out.println(one + "is the middle element");
					  middle=one;
				  } else if (array.get(three).compareTo(array.get(two)) < 0) {
				    //System.out.println(three +"is the middle element");
					  middle=three;
				  } else {
					  //System.out.println(two +"is the middle element");
					  middle=two;
				  }
				}
			
			//swap middle and right
			E temp1= array.get(middle);
			array.set(middle, array.get(right));
			array.set(right, temp1);
		 
		 	int saved_left= left;
		  	int saved_right=right;
		  
		  	int pivot= right;
			
			for(int i= left; i< right; i++) {
				if(array.get(i) .compareTo(array.get(pivot)) < 0) {
					//swap left and i
					E temp= array.get(left);
					array.set(left, array.get(i));
					array.set(i, temp);
					
					left++;
				}
			}
			//After we are done looping, swap left and pivot
			E temp= array.get(left);
			array.set(left, array.get(pivot));
			array.set(pivot, temp);
			
			//return the pivot index
			pivot=left;
		  
			left= saved_left;
		  	right=saved_right;
			
	        //if there are at least two unsorted elements between left and pivot
	        if( (left == pivot) || (left+1 == pivot) ) {
	           
	        }
	        else {
	        	first_element_quicksort(array, left, pivot-1);
	        }
	        //if there are at least two unsorted elements between pivot and right
	        if( (pivot== right) || (pivot+1 == right)) {
	            
	        }
	        else {
	        	first_element_quicksort(array, pivot + 1, right);
	        }
	        
	        return array;
	    }
	
	 /*
		 * Recursive function that chooses the pivot to be a the median of three elements, the first, last, and middle
		 * @param	array: the arrayList to be insertion sorted
		 * @param	left: the left boundary index of the subarray
		 * @param	right: the right boundary index of the subarray
		 * @return	duration: the time taken to sort the array
		 * */
	 private static <E extends Comparable<E>> ArrayList<E> three_quicksort(ArrayList<E> array, int left, int right){
		 	Random generator= new Random();
		 
		 	int one= 0;
			int two= array.size()/2;
			int three= array.size()-1;
			
			int middle= 0;
			System.out.println("one: " + one + "two: "+ two + "three: "+ three);
			
			if (array.get(one).compareTo(array.get(two)) > 0) {
				//now try to place c
				if(array.get(three).compareTo(array.get(two)) < 0) {
					//System.out.println(two +"is the middle element");
					middle=two;
				}
				else if(array.get(three).compareTo(array.get(one)) < 0) {
					//System.out.println(three + "is the middle element");
					middle=three;
				}
				else {
					//System.out.println(one + "is the middle element");
					middle=one;
				}
			} 
			else {
				  if (array.get(three).compareTo(array.get(one)) < 0) {
					  //System.out.println(one + "is the middle element");
					  middle=one;
				  } else if (array.get(three).compareTo(array.get(two)) < 0) {
				    //System.out.println(three +"is the middle element");
					  middle=three;
				  } else {
					  //System.out.println(two +"is the middle element");
					  middle=two;
				  }
				}
			
			//swap middle and right
			E temp1= array.get(middle);
			array.set(middle, array.get(right));
			array.set(right, temp1);
		 
		 	int saved_left= left;
		  	int saved_right=right;
		  
		  	int pivot= right;
			
			for(int i= left; i< right; i++) {
				if(array.get(i) .compareTo(array.get(pivot)) < 0) {
					//swap left and i
					E temp= array.get(left);
					array.set(left, array.get(i));
					array.set(i, temp);
					
					left++;
				}
			}
			//After we are done looping, swap left and pivot
			E temp= array.get(left);
			array.set(left, array.get(pivot));
			array.set(pivot, temp);
			
			//return the pivot index
			pivot=left;
		  
			left= saved_left;
		  	right=saved_right;
			
	        //if there are at least two unsorted elements between left and pivot
	        if( (left == pivot) || (left+1 == pivot) ) {
	           
	        }
	        else {
	        	first_element_quicksort(array, left, pivot-1);
	        }
	        //if there are at least two unsorted elements between pivot and right
	        if( (pivot== right) || (pivot+1 == right)) {
	            
	        }
	        else {
	        	first_element_quicksort(array, pivot + 1, right);
	        }
	        
	        return array;
	    }
	
	public static enum PivotStrategy{
		FIRST_ELEMENT,
		RANDOM_ELEMENT,
		MEDIAN_OF_THREE_RANDOM_ELEMENTS,
		MEDIAN_OF_THREE_ELEMENTS;
	}
}