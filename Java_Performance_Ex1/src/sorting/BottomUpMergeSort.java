package sorting;

import java.util.Arrays;

/**
 * This code was taken from:<br>
 * http://www.codebytes.in/2014/10/bottom-up-merge-sort-java-implementation.html
 */
public class BottomUpMergeSort implements Sorter {
	
	@Override
	public void sort(int[] input) {
        int aux[] = new int[input.length];
        mergeSort(input, aux, 0, input.length - 1);
	}

    private void merge(int[] orig, int[] aux, int start, int mid, int end) {
        int i, j, z = start; 
        
        if(orig[mid] <= orig[mid+1])return; 
        
        for(i=start, j = mid+1; i!=mid+1 || j!=end+1;){
            if(i==mid+1)               while(j!=end+1){ aux[z++] = orig[j++]; }
            else if(j==end+1)          while(i!=mid+1){ aux[z++] = orig[i++]; }
            else if(orig[i]<=orig[j])  aux[z++] = orig[i++];
            else                       aux[z++] = orig[j++];
        }
        System.arraycopy(aux, start, orig, start, end-start+1);
    }

    private void mergeSort(int[] orig, int[] aux, int start, int end) {
        int N = orig.length;
        for (int sz = 1; sz < N; sz *= 2) {
            for (int lo = 0; lo < N - sz; lo += sz + sz) {
                merge(orig, aux, lo, lo + sz - 1, Math.min(lo + sz + sz - 1, N-1));
            }
        }
    }

    public static void main(String[] args) {
		int arr[] = {9,5,3,5,1,2,20};
		BottomUpMergeSort sorter = new BottomUpMergeSort();
		sorter.sort(arr);
		System.out.println(Arrays.toString(arr));
    }

}
