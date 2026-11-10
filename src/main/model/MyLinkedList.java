package main.model;

public class MyLinkedList<T> {

    private static class Node<T> {
        T item;
        Node<T> prev;
        Node<T> next;

        Node(Node<T> prev, T item, Node<T> next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    /**
     * Размер списка. Сложность O(1), так как размер обновляется при каждом удалении или добавлении элемента
     * и его не нужно заново высчитывать или проходиться по коллекции.
     */
    public int size() {
        return size;
    }

    /**
     * Добавление элемента в конец списка.
     * O(1), так как мы просто "пакуем" элемент коллекции в ноду и присваиваем необходимые ссылки.
     */
    public void add(T element) {
        Node<T> newNode = new Node<>(tail, element, null);
        if (tail == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    /**
     * Получение элемента по индексу.
     * Сложность O(n), так как у нас нет прямого доступа по индексу и необходимо дойти до нужного элемента.
     */
    public T get(int index) {
        checkIndex(index);
        return node(index).item;
    }

    /**
     * Удаление элемента по индексу.
     * Сложность O(n), так как сначала ищем узел проходом по списку, после - меняем ссылки соседних элементов.
     */
    public T remove(int index) {
        checkIndex(index);
        Node<T> node = node(index);

        T item = node.item;
        Node<T> prev = node.prev;
        Node<T> next = node.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
        }

        // "нулим" ссылки удаляемого узла, чтобы GC удалил объект
        node.prev = null;
        node.next = null;
        node.item = null;

        size--;
        return item;
    }

    /**
     * Поиск узла по индексу, внутренний метод.
     * Определяем в какой половине списка лежит искомый элемент и ищем с хвоста, либо с головы.
     * По сути - это n/2, но всё равно O(n).
     */
    private Node<T> node(int index) {
        Node<T> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    /**
     * Проверка индекса, внутренний метод.
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс не может быть отрицательным числом или " +
                    "больше/равным размеру списка");
        }
    }
}