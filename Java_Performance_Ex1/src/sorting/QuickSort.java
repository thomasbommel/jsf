package sorting;

import java.util.Arrays;

/**
 * This code was taken from:<br>
 * https://javabeginners.de/Algorithmen/Sortieralgorithmen/Quicksort.php
 */
public class QuickSort implements Sorter {

	public int[] intArr = {3,1,2};
	
	@Override
	public void sort(int[] input) {
		this.intArr = input;
		this.quickSort(0, intArr.length - 1);
	}

	private int[] quickSort(int l, int r) {
		int q;
		if (l < r) {
			q = partition(l, r);
			quickSort(l, q);
			quickSort(q + 1, r);
		}
		return intArr;
	}

	private int partition(int l, int r) {
		int i, j, x = intArr[(l + r) / 2];
		i = l - 1;
		j = r + 1;
		while (true) {
			do {
				i++;
			} while (intArr[i] < x);
			
			do {
				j--;
			} while (intArr[j] > x);
			
			if (i < j) {
				int k = intArr[i];
				intArr[i] = intArr[j];
				intArr[j] = k;
			} else {
				return j;
			}
		}
	}

	public static void main(String[] args) {
		int[] arr = {9,5,3,5,1,2,20};
		new QuickSort().sort(arr);
		System.out.println(Arrays.toString(arr));
	}
}
