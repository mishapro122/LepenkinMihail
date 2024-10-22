package org.lesson04;

import java.util.*;

public class Main {
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    System.out.print("Введите длинну массива: ");
    int n = input.nextInt();
    System.out.println("Введите элементы массива");
    List<Integer> arr = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      arr.add(input.nextInt());
    }
    Scanner input1 = new Scanner(System.in);
    System.out.println("Введите тип сортировки BUBBLE или MERGE");
    String type = input1.nextLine();
      SortTypes tipe = SortTypes.valueOf((type));
      List<Integer> otvet = Sort.choosingSort(arr, tipe);
    for (int i = 0; i < n; i++) {
      System.out.print(otvet.get(i) + " ");
    }
  }
}
