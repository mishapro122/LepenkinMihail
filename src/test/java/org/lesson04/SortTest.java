package org.lesson04;

import java.util.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SortTest {
  @Test
  public void testSortWithNormalListPuzur() {
    List<Integer> arr = Arrays.asList(5, 3, 8, 1, 2, 7, 4, 6);
    List<Integer> sortArr = Sort.choosingSort(arr, "пузырек");
    assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), sortArr);
  }

  @Test
  public void testSortWithEmptyListPuzur() {
    List<Integer> arr = List.of();
    List<Integer> sortArr = Sort.choosingSort(arr, "пузырек");
    assertEquals(List.of(), sortArr);
  }

  @Test
  public void testSortWithSingleElementListPuzur() {
    List<Integer> arr = List.of(52);
    List<Integer> sortArr = Sort.choosingSort(arr, "пузырек");
    assertEquals(List.of(52), sortArr);
  }

  @Test
  public void testSortWithAlreadySortedListPuzur() {
    List<Integer> arr = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
    List<Integer> sortArr = Sort.choosingSort(arr, "пузырек");
    assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), sortArr);
  }

  @Test
  public void testSortWithReverseSortedListPuzur() {
    List<Integer> arr = Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1);
    List<Integer> sortArr = Sort.choosingSort(arr, "пузырек");
    assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), sortArr);
  }

  @Test
  public void testSortWithNormalListMerger() {
    List<Integer> arr = Arrays.asList(5, 3, 8, 1, 2, 7, 4, 6);
    List<Integer> sortArr = Sort.choosingSort(arr, "вставки");
    assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), sortArr);
  }

  @Test
  public void testSortWithEmptyListMerge() {
    List<Integer> arr = List.of();
    List<Integer> sortArr = Sort.choosingSort(arr, "вставки");
    assertEquals(List.of(), sortArr);
  }

  @Test
  public void testSortWithSingleElementListMerge() {
    List<Integer> arr = List.of(52);
    List<Integer> sortArr = Sort.choosingSort(arr, "вставки");
    assertEquals(List.of(52), sortArr);
  }

  @Test
  public void testSortWithAlreadySortedListMerge() {
    List<Integer> arr = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
    List<Integer> sortArr = Sort.choosingSort(arr, "вставки");
    assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), sortArr);
  }

  @Test
  public void testSortWithReverseSortedListMerge() {
    List<Integer> arr = Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1);
    List<Integer> sortArr = Sort.choosingSort(arr, "вставки");
    assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), sortArr);
  }
}
  
