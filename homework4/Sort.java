import java.util.*;

public class Sort {
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
