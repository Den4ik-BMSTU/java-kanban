package manager;

import task.Task;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private  final Map<Integer, Node<Task>> customeLinkedList = new LinkedHashMap<>();//так можно? или класс лучше создать?

    private Node<Task> first;
    private Node<Task> last;

    private static class Node<Task> {
        private Node<Task> previous;
        private Task value;
        private Node<Task> next;

        public Node(Node<Task> previous, Task value, Node<Task> next) {
            this.previous = previous;
            this.value = value;
            this.next = next;
        }

    }

    @Override
    public void add(Task task) { // добавление задачи в связный список
        if (task == null){
            return;
        }
        final int id = task.getId();
        remove(id);
        linkLast(task);
        customeLinkedList.put(id,getLast());
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) { //удаление по ид
        Node<Task> task = customeLinkedList.get(id);
        Node<Task>  current = first;
        while(current != null && current != task) {
            current = current.next;
        }
        customeLinkedList.remove(id);
        removeNode(task);
    }

    private void linkLast(Task task) { // добавлять задачу в конец списка
        final Node<Task> secondLast = last;
        final Node<Task> newNode = new Node(secondLast, task, null);
        last = newNode;
        if (secondLast != null) {
            secondLast.next = newNode;
        } else {
            first = newNode;
        }
    }

    private  Node<Task> getLast(){ //получение последнего
        final Node<Task> tail = last;
        return tail;
    }

    private List<Task> getTasks() { // собирать все задачи из списка в обычный ArrayList
        List<Task> allListHistory = new ArrayList<>();
        for (Node<Task> node : customeLinkedList.values()){
            allListHistory.add(node.value);
        }
        return allListHistory;
    }

    private void removeNode(Node node) {
        if (node != null){
            if (node.previous != null){
                node.previous.next = node.next;
            }
            else {
                first = node.next;}

            if (node.next != null){
                node.next.previous = node.previous;
            } else {
                last = node.previous;
            }
        }
    }
}