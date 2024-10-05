import java.util.Arrays;

public class СustomArrays {

    public static class CustomArr<A> implements Interface.metod<A> {

        private Object[] arr;
        private int last;

        public CustomArr(Object[] arr, int last) {
            this.arr = arr;
            this.last = last;
        }
        @Override
        public void add(A el) {
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
        public void get(int index) {
            System.out.println(arr[index]);
        }

        @Override
        public void remove(int index) {
            if (index + 1 != last) {
                for (int i = index; i < last - 1; i++) {
                    arr[i] = arr[i + 1];
                }
            }
            arr = Arrays.copyOf(arr, (--last));
        }

        @Override
        public void print() {
            for (int i = 0; i < last; i++) {
                System.out.print(arr[i] + " ");
            }
            System.out.println();
        }
        @Override
        public void len() {
            System.out.println(last);
        }
    }
}
