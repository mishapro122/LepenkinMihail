import java.util.Arrays;

public static class CustomArrays<A> implements Interface.metod<A> {

    private Object[] arr;
    private int last;
    /**
    * функция присоваиваний входных значений 
    */
    public CustomArrays(Object[] arr, int last) {
        this.arr = arr;
        this.last = last;
    }
    /**
    * функция добавления элемента
    * сначала идет проверка на то, является ли элемент нулевым или нет, если нулевой, то @RuntimeException
    * создается массив arr, который является копией массива arr, но на одну еденицу длиннее, как раз в этой еденице будет новый элемент
    */
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
    /**
    * функция получения элемента по индексу
    */
    @Override
    public <A> get(int index) {
        return arr[index];
    }
    /**
    * функция удаления элемента
    * сначала идет проверка на то, является ли индекс элемента, который мы хотим удалить последним
    * если является, то мы просто создаем массив arr, который является копией массива arr, но на 1 еденицу короче
    * если нет, то мы просто сдвигаем элементы начиная с с index + 1 на один шаг назад, потом мы просто создаем массив arr, который является копией массива arr, но на 1 еденицу короче
    */
    @Override
    public void remove(int index) {
        if (index + 1 != last) {
            for (int i = index; i < last - 1; i++) {
                arr[i] = arr[i + 1];
            }
        }
        arr = Arrays.copyOf(arr, (--last));
    }
}
