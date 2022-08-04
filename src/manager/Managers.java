package manager;

import java.io.File;

public final class Managers {

    private static TaskManager taskManager;
    private static HistoryManager historyManager;

    static {
        historyManager = new InMemoryHistoryManager();
        taskManager = new FileBackedTaskManager(new File("src/manager/saveReports.csv"));
    }
        public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}