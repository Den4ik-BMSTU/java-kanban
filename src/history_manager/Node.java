package history_manager;

import task.Task;

public class Node {
    public Task task;
    public Node previous;
    public Node next;

    public Node(Task task, Node previous, Node next) {
        this.task = task;
        this.previous = previous;
        this.next = next;
    }
}