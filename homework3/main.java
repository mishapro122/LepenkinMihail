import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int n = 15;
        Object[] data = new Object[n];
        for (int i = 0; i < n; i++) {
            data[i] = input.nextInt();
        }
        CustomArrays<Integer> arrey = new CustomArrays<>(data, n);
        arrey.add(152);
        n++;
        System.out.println(arrey.get(n / 2));
        arrey.remove(10);
    }
}
