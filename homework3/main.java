import java.util.Scanner;

public class homeworkThird {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = 15;
        Object[] data = new Object[n];
        for (int i = 0; i < n; i++) {
            data[i] = input.nextInt();
        }
        СustomArrays.CustomArr<Integer> arrey = new СustomArrays.CustomArr<>(data, n);
        arrey.print();
        arrey.len();
        arrey.add(152);
        n++;
        arrey.add(null);
        arrey.get(n / 2);
        arrey.print();
        arrey.len();
        arrey.remove(10);
        arrey.print();
        arrey.len();
    }
}
