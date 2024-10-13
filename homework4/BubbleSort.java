import java.util.ArrayList;
import java.util.List;

public class BubbleSort {
    private static final int SIZE = 5000;

    public static List<Integer> sort(List<Integer> arr) {
        if (arr == null) {
            throw new NullPointerException("нельзя отсортировать пустой массив");
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
