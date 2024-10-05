import java.util.Arrays;

public class СustomArrays {

    public static class CustomArr<A> implements Interface.metod<A> {

        private Object[] arr; //массив
        private int last; //последний индекс массива

        public CustomArr(Object[] arr, int last) { #функция создания массива
            this.arr = arr;
            this.last = last;
        }
        @Override
        public void add(A el) { //функция добавления элемента в массив с проверкой на null 
            try {
                if (el == null) {
                    throw new RuntimeException("Аргумент не должен быть null");
                }
                if (arr.length == last) {
                    arr = Arrays.copyOf(arr, (last + 1));
                    arr[last] = el;
                    ++last;

                } else {
                    arr[last] = el;
                    ++last;
                }
            } catch (RuntimeException e) {
                System.out.println("Аргумент не должен быть null");
            }
        }

        @Override
        public void get(int index) { //функция вывода элемента по индексу
            System.out.println(arr[index]);
        }

        @Override
        public void remove(int index) { //функция удаления элемента по индексу
            if (index + 1 != last) {
                for (int i = index; i < last - 1; i++) {
                    arr[i] = arr[i + 1];
                }
            }
            arr = Arrays.copyOf(arr, (--last));
        }

        @Override
        public void print() { //функция вывода всего массива
            for (int i = 0; i < last; i++) {
                System.out.print(arr[i] + " ");
            }
            System.out.println();
        }
        @Override
        public void len() { //функция вывода длинны всего массива
            System.out.println(last);
        }
    }
}
