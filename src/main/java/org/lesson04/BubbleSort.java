package org.lesson04;

import java.util.ArrayList;
import java.util.List;

public class BubbleSort {
    /**
     * SIZE - максимально возможный размер массива
     */
    private static final int SIZE = 5000;

    /**
     * sort - функция сортировки массива, которая выводит копию отсортированного заданного массива
     * сначала идет проверка на то, является ли массив пустым, если является, то выводит @throw IllegalArgumentException
     * потом идет проверка на размер, если размер массива больше чем максимальный, то выводит @throw IllegalStateException
     * далее создается sortArr - копия изначального массива, которую мы будем сортировать
     * в конце реализована сортировка пузырьком и вывод отсортировонного массива
     */

    public static List<Integer> sort(List<Integer> arr) {
        if (arr == null) {
            throw new IllegalArgumentException("нельзя отсортировать пустой массив");
        }
        if (arr.size() > SIZE) {
            throw new IllegalStateException("размер массива слишком большой");
        }
        List<Integer> sortArr = new ArrayList<>(List.copyOf(arr));
        for (int i = 0; i < sortArr.size(); i++) {
            for (int j = i + 1; j < sortArr.size(); j++) {
                if (sortArr.get(i).compareTo(sortArr.get(j)) > 0) {
                    Integer temp = sortArr.get(i);
                    sortArr.set(i, sortArr.get(j));
                    sortArr.set(j, temp);
                }
            }
        }
        return sortArr;
    }
}
