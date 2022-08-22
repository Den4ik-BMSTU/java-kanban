package manager;

import history_manager.HistoryManager;
import history_manager.InMemoryHistoryManager;
import task_manager.FileBackedTaskManager;
import task_manager.TaskManager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault(File file) {
        return new FileBackedTaskManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}