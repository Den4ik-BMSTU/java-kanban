package tests;

import task_manager.InMemoryTaskManager;

public class InMemoryTaskManagerTest extends tests.TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createManager() {
        return new InMemoryTaskManager();
    }
}