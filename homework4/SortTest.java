import java.util.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SortTest {
   /**
    * тест, который проверяет самый обычный случай
    * тип сортировки - пузырек
    */
    public void testSortWithNormalListPuzur()  {
        List<Integer> arr = Arrays.asList(5, 3, 8, 1, 2, 7, 4, 6);
        List<Integer> sortArr = Sort.choosingSort(arr, "пузырек");
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), sortArr);
    }
    /**
    * тест, который проверяет случай, когда заданный массив - пустой
    * тип сортировки - пузырек
    */
    public void testSortWithEmptyListPuzur() {
        List<Integer> arr = List.of();
        List<Integer> sortArr = Sort.choosingSort(arr, "пузырек");
        assertEquals(List.of(), sortArr);
    }
    /**
    * тест, который проверяет случай, когда заданный массив - из одного элемента
    * тип сортировки - пузырек
    */
    public void testSortWithSingleElementListPuzur() {
        List<Integer> arr = List.of(52);
        List<Integer> sortArr = Sort.choosingSort(arr, "пузырек");
        assertEquals(List.of(52), sortArr);
    }
    /**
    * тест, который проверяет случай, когда заданный массив - уже отсортирован
    * тип сортировки - пузырек
    */
    public void testSortWithAlreadySortedListPuzur() {
        List<Integer> arr = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> sortArr = Sort.choosingSort(arr, "пузырек");
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), sortArr);
    }
    /**
    * тест, который проверяет случай, когда заданный массив - отсортирован наоборот
    * тип сортировки - пузырек
    */
    public void testSortWithReverseSortedListPuzur() {
        List<Integer> arr = Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1);
        List<Integer> sortArr = Sort.choosingSort(arr, "пузырек");
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), sortArr);
    }
    /**
    * тест, который проверяет случай, когда заданный массив - имеет максимально возможную длинну
    * тип сортировки - пузырек
    */
    public void testSortWithMaxElementsPuzur() {
        List<Integer> arr = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            arr.add(i);
        }
        assertDoesNotThrow(() -> Sort.choosingSort(arr, "пузырек"));
    }
    /**
    * тест, который проверяет самый обычный случай
    * тип сортировки - вставки
    */
    public void testSortWithNormalListMerger()  {
        List<Integer> arr = Arrays.asList(5, 3, 8, 1, 2, 7, 4, 6);
        List<Integer> sortArr = Sort.choosingSort(arr, "встваки");
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), sortArr);
    }
    /**
    * тест, который проверяет случай, когда заданный массив - пустой
    * тип сортировки - вставки
    */
    public void testSortWithEmptyListMerge() {
        List<Integer> arr = List.of();
        List<Integer> sortArr = Sort.choosingSort(arr, "встваки");
        assertEquals(List.of(), sortArr);
    }
    /**
    * тест, который проверяет случай, когда заданный массив - из одного элемента
    * тип сортировки - вставки
    */
    public void testSortWithSingleElementListMerge() {
        List<Integer> arr = List.of(52);
        List<Integer> sortArr = Sort.choosingSort(arr, "встваки");
        assertEquals(List.of(52), sortArr);
    }
    /**
    * тест, который проверяет случай, когда заданный массив - уже отсортирован
    * тип сортировки - вставки
    */
    public void testSortWithAlreadySortedListMerge() {
        List<Integer> arr = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
        List<Integer> sortArr = Sort.choosingSort(arr, "встваки");
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), sortArr);
    }
    /**
    * тест, который проверяет случай, когда заданный массив - отсортирован наоборот
    * тип сортировки - вставки
    */
    public void testSortWithReverseSortedListMerge() {
        List<Integer> arr = Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1);
        List<Integer> sortArr = Sort.choosingSort(arr, "встваки");
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), sortArr);
    }
    /**
    * тест, который проверяет случай, когда заданный массив - имеет максимально возможную длинну
    * тип сортировки - вставки
    */
    public void testSortWithMaxElementsMerge() {
        List<Integer> arr = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            arr.add(i);
        }
        assertDoesNotThrow(() -> Sort.choosingSort(arr, "встваки"));
    }
    /**
    * тест, который проверяет случай, когда заданный тип сортировки - не существует
    */
    public void testSortWithUnexistableType() {
        List<Integer> arr = new ArrayList<>();
        for (int i = 0; i < 4999; i++) {
            arr.add(i);
        }
        assertDoesNotThrow(() -> Sort.choosingSort(arr, "бугага"));
    }
}
