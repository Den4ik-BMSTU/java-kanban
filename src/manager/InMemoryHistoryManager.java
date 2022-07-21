package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private  final Map<Integer, Node> customeLinkedList = new HashMap<>();
    private final List<Task> historyList;
    private Node first;
    private Node last;

    private static class Node {
        private Node previous;
        private Task value;
        private Node next;

        public Node(Node previous, Task value, Node next) {
            this.previous = previous;
            this.value = value;
            this.next = next;
        }

    }

    public InMemoryHistoryManager() {
        this.historyList = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (customeLinkedList.containsKey(task.getId())) {
            removeNode(customeLinkedList.get(task.getId()));
            customeLinkedList.remove(task.getId());
        }
        Node nodeAdded = linkLast(task); //После добавления задачи не забудьте обновить значение узла в HashMap.
        customeLinkedList.put(task.getId(), nodeAdded);
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }

    @Override
    public void remove(int id) {
        if (customeLinkedList.containsKey(id)) {
            removeNode(customeLinkedList.get(id));
            customeLinkedList.remove(id);
        }
    }

    public Node linkLast(Task task) { // добавлять задачу в конец списка
        final Node secondLast = last;
        final Node newNode = new Node(secondLast, task, null);
        last = newNode;
        if (secondLast != null) {
            secondLast.next = newNode;
        } else {
            first = newNode;
        }
        return newNode;
    }

    public List<Task> getTasks() { // собирать все задачи из списка в обычный ArrayList
        List<Task> allListHistory = new ArrayList<>();
        Node node = first;
        while (node != null) {
            allListHistory.add(node.value);
            node = node.next;
        }
        return allListHistory;
    }

    public void removeNode(Node value) {

        if (value == first) {
            if (first.next != null)
                first = first.next;
            else
                first = null;
        } else if (value == last) {
            if (last.previous != null)
                last = last.previous;
            else
                last = null;
        } else {
            if (value.previous != null) {
                value.previous.next = value.next;
                if (value.next != null) {
                    value.next.previous = value.previous;
                }
            }
        }
    }
}