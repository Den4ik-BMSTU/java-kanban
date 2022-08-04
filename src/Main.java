import task.*;
import manager.*;

public class Main {
    public static void main(String[] agrs){
        TaskManager inMemoryTaskManager = Managers.getDefault();

        inMemoryTaskManager.createTask(new Task("Таск 1", TaskType.TASK, TaskStatus.NEW, "Задача1"));
        inMemoryTaskManager.createTask(new Task("Таск 2", TaskType.TASK, TaskStatus.NEW, "Задача2"));
        inMemoryTaskManager.createEpic(new Epic("Epic 1", TaskType.EPIC, TaskStatus.NEW, "Эпик 1"));
        inMemoryTaskManager.createEpic(new Epic("Epic 2", TaskType.EPIC, TaskStatus.NEW, "Эпик 2"));
        inMemoryTaskManager.addSubTask(new SubTask("SubTask1", TaskType.SUBTASK, TaskStatus.NEW, "Субтаск 1", 3));
        inMemoryTaskManager.addSubTask(new SubTask("SubTask 2", TaskType.SUBTASK, TaskStatus.NEW, "Субтаск 2", 3));
        inMemoryTaskManager.addSubTask(new SubTask("SubTask 3", TaskType.SUBTASK, TaskStatus.NEW, "Субтаск 3", 3));

        for (int i = 1; i <= 2; i++) {
            inMemoryTaskManager.getTaskById(i);
        }
        for (int i = 3; i <= 4; i++) {
            inMemoryTaskManager.getEpicById(i);
        }

        for (int i = 5; i <= 7; i++) {
            inMemoryTaskManager.getSubtaskById(i);
        }

        for (int i = 1; i <= 2; i++) {
            inMemoryTaskManager.getTaskById(i);
        }

        System.out.println(inMemoryTaskManager.getHistory());



        inMemoryTaskManager.deleteTaskById(2);

        System.out.println(inMemoryTaskManager.getHistory());

        inMemoryTaskManager.deleteEpicById(3);

        System.out.println(inMemoryTaskManager.getHistory());
    }
}