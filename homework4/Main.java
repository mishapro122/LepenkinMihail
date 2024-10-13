import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Введите длинну массива: ");
        int n = input.nextInt();
        System.out.println("Введите элементы массива, после них введите нужный тип сортировки: пузырек или вставки, сделайте все это через пробел");
        List<Integer> arr = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            arr.add(input.nextInt());
        }
        String type = input.nextLine();
        List<Integer> otvet = Sort.choosingSort(arr, type);
        for (int i = 0; i < n; i++) {
            System.out.print(otvet.get(i) + " ");
        }
    }
}
