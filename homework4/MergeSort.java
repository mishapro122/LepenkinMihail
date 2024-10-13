import java.util.*;

import static java.util.List.*;

public class MergeSort  {
    private static final int SIZE = 5000;

    public static List<Integer> sort(List<Integer> arr) {
        if (arr == null) {
            throw new NullPointerException("нельзя отсортировать пустой массив");
        }
        if (arr.size() > SIZE) {
            throw new IllegalStateException("размер массива слишком большой");
        }
        List<Integer> sortArr = new ArrayList<>(copyOf(arr));
        Collections.sort(sortArr);
        return sortArr;
    }
}
