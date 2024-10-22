package org.lesson04;

import java.util.*;

public class Sort {
    /**
     * choosingSort - функция выбора нужной сортировки, на вход принимает массив, который нужно отсортировать, и тип необходимой сортировки
     * пока на выбор дается только два вида сортировок только пузырьком и вставками
     * если пользователь хочет использовать другую сортировку, то @throw IllegalStateException
     */
    public static List<Integer> choosingSort(List<Integer> arr, String type) {
        if (type.equals(" пузырек")) {
            return BubbleSort.sort(arr);
        }
        else if (type.equals(" вставки")) {
            return MergeSort.sort(arr);
        }
        else {
            throw new IllegalStateException("выбранного типа не существует");
        }
    }
}
