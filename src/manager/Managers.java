package manager;

import history_manager.*;
import task_manager.*;


public class Managers {

    public static TaskManager getDefault(String url, String key) {
        return new HTTPTaskManager(url, key);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}