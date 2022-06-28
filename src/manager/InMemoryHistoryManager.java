package manager;

import task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final static int LIST_SIZE = 10;
    private final List<Task> historyList = new ArrayList<>();

    @Override
    public void add(Task task) {

        if (historyList.size() == LIST_SIZE) {
            historyList.remove(0);
        }
        if (task != null) {
            historyList.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyList;
    }
}