package sorting;

import java.util.Arrays;

/**
 * This code was taken from:<br>
 * http://cubesort.blogspot.com/
 */
public class CubeSort implements Sorter {
	
	public int movement1, movement2, cube;

	public void sort(int[] arr) {
		movement1 = 1;
		movement2 = arr.length;
		cube = arr.length - 1;

		do {
			for (int i = arr.length - 1; i > 0; i--) {
				if (arr[i - 1] > arr[i]) {
					int aux = arr[i];
					arr[i] = arr[i - 1];
					arr[i - 1] = aux;
					cube = i;
				}
			}
			movement1 = cube + 1;

			for (int j = 1; j < arr.length; j++) {
				if (arr[j - 1] > arr[j]) {
					int aux = arr[j];
					arr[j] = arr[j - 1];
					arr[j - 1] = aux;
					cube = j;
				}
			}
			movement2 = cube - 1;
		} while (movement2 >= movement1);
	}
	
	public static void main(String[] args) {
		int arr[] = {9,5,3,5,1,2,20};
		CubeSort sorter = new CubeSort();
		sorter.sort(arr);
		System.out.println(Arrays.toString(arr));
	}
}
