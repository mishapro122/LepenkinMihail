package org.lesson04;

import java.util.*;

public class Sort {
    /**
     * choosingSort - функция выбора нужной сортировки, на вход принимает массив, который нужно отсортировать, и тип необходимой сортировки
     * пока на выбор дается только два вида сортировок только пузырьком и вставками
     * если пользователь хочет использовать другую сортировку, то @throw IllegalStateException
     */
    public static List<Integer> choosingSort(List<Integer> arr,  SortTypes type) {
        return switch (type) {
            case BUBBLE -> BubbleSort.sort(arr);
            case MERGE -> MergeSort.sort(arr);
        };
    }
}
