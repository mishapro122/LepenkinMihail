import java.util.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MergeSortTest {

    public void testSortWithNormalList()  {
        List<Integer> arr = Arrays.asList(5, 3, 8, 1, 2, 7, 4, 6);
        List<Integer> sortArr = MergeSort.sort(arr);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), sortArr);
    }

    public void testSortWithEmptyList() {
        List<Integer> arr = List.of();
        List<Integer> sortArr = MergeSort.sort(arr);
        assertEquals(List.of(), sortArr);
    }

    public void testSortWithSingleElementList() {
        List<Integer> arr = List.of(52);
        List<Integer> sortArr = MergeSort.sort(arr);
        assertEquals(List.of(52), sortArr);
    }

    public void testSortWithAlreadySortedList() {
        List<Integer> arr = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> sortArr = MergeSort.sort(arr);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), sortArr);
    }

    public void testSortWithReverseSortedList() {
        List<Integer> arr = Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1);
        List<Integer> sortArr = MergeSort.sort(arr);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), sortArr);
    }

    public void testSortWithMaxElements() {
        List<Integer> arr = new ArrayList<>();
        for (int i = 0; i < 4999; i++) {
            arr.add(i);
        }
        assertDoesNotThrow(() -> MergeSort.sort(arr));
    }
}
