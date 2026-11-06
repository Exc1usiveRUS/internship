package model;

import main.model.MyLinkedList;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyLinkedListTest {

    // Пустой список

    @Test
    void emptyListHasSizeZero() {
        MyLinkedList<String> list = new MyLinkedList<>();
        assertEquals(0, list.size());
    }

    @Test
    void emptyListGetAndRemoveException() {
        MyLinkedList<String> list = new MyLinkedList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    }

    // Один элемент

    @Test
    void singleElementAddAndGet() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("a");
        assertEquals(1, list.size());
        assertEquals("a", list.get(0));
    }

    @Test
    void singleElementRemoveLeavesEmptyAndListIsReusable() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("a");
        assertEquals("a", list.remove(0));
        assertEquals(0, list.size());

        // head и tail должны очиститься, список снова работает
        list.add("b");
        assertEquals(1, list.size());
        assertEquals("b", list.get(0));
    }

    // Удаление элементов

    @Test
    void remove_head_middle_tail() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        for (int i = 1; i <= 5; i++) list.add(i);   // [1, 2, 3, 4, 5]

        assertEquals(1, list.remove(0));
        assertEquals(5, list.remove(3));
        assertEquals(3, list.remove(1));

        assertEquals(2, list.size());
        assertEquals(2, list.get(0));
        assertEquals(4, list.get(1));
    }

    @Test
    void removeKeepsBackwardLinksConsistent() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        for (int i = 1; i <= 6; i++) list.add(i);
        list.remove(2);

        assertEquals(6, list.get(4));
        assertEquals(4, list.get(2));
    }

    // Проверка исключений при запросе по индексу

    @Test
    void outOfBoundsThrows() {
        MyLinkedList<String> list = new MyLinkedList<>();
        list.add("a");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    void getWorksFromBothEnds() {
        MyLinkedList<Integer> list = new MyLinkedList<>();
        for (int i = 0; i < 10; i++) list.add(i);
        assertEquals(1, list.get(1));
        assertEquals(8, list.get(8));
    }
}
