package sorting;

import java.util.Arrays;
import java.util.Random;

public class Benchmark {
	
	/**
	 * Defines the Load (aka array size) of a benchmark.
	 */
	public enum Load {
		SMALL  	(100),		//1-hundred
		MEDIUM 	(50000),	//50-thousand
		LARGE  	(1000000);	//1-million
		
		public final int load;
		
		private Load(int load) {
			this.load = load;
		}
	}
	
	/**
	 * Args:<br>
	 * [0] = Load (name of enum value)					<br>
	 * [1] = Number of arrays to generate & benchmark	<br>
	 * [2] = Benchmarking repetitions of each array		<br>
	 */
	public static void main(String[] args) {
		if (args.length != 3) return;
		Benchmark bm = new Benchmark();
		
		int arrayLoad = Load.valueOf(args[0]).load;
		int nrArrays = Integer.parseInt(args[1]);
		int repetitions = Integer.parseInt(args[2]);
		
		for (int curArray = 0; curArray < nrArrays; curArray++) {
			int[] arr = new int[arrayLoad];
			Random r = new Random(curArray * 123);	//rng seed
			for (int i = 0; i < arrayLoad; i++) {
				arr[i] = r.nextInt();
			}
			bm.bechmarkSorters(arr, repetitions);
		}
	}
	
	public void bechmarkSorters(int[] array, int repetitions) {
		QuickSort quickSorter = new QuickSort();
		BottomUpMergeSort mergeSorter = new BottomUpMergeSort();
		CubeSort cubeSorter = new CubeSort();
		
		int[] sortedArr = array.clone();
		Arrays.sort(sortedArr);
		
		for (int i = 0; i < repetitions; i++) {			
			benchmarkQuickSort(quickSorter, array.clone(), sortedArr);
			benchmarkMergeSort(mergeSorter, array.clone(), sortedArr);
			benchmarkCubeSort(cubeSorter, array.clone(), sortedArr);
		}
	}
	
	private void benchmarkQuickSort(QuickSort sorter, int[] array, int[] sortedArr) {
		sorter.sort(array);
		assert Arrays.equals(array, sortedArr);
	}
	
	private void benchmarkMergeSort(BottomUpMergeSort sorter, int[] array, int[] sortedArr) {
		sorter.sort(array);
		assert Arrays.equals(array, sortedArr);
	}
	
	private void benchmarkCubeSort(CubeSort sorter, int[] array, int[] sortedArr) {
		sorter.sort(array);
		assert Arrays.equals(array, sortedArr);
	}
	
}
