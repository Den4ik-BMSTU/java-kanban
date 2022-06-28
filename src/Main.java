import task.Task;
import task.TaskStatus;
import task.SubTask;
import task.Epic;
import manager.*;

public class Main {
    public static void main(String[] agrs){
        TaskManager inMemoryTaskManager = Managers.getDefault();

        inMemoryTaskManager.createTask(new Task("Таск 1", TaskStatus.NEW, "Доделать финальный проект"));
        inMemoryTaskManager.createTask(new Task("Таск 2", TaskStatus.NEW, "Купить еды"));
        inMemoryTaskManager.createTask(new Task("Таск 3", TaskStatus.NEW, "Купить воды"));
        inMemoryTaskManager.createTask(new Task("Таск 4", TaskStatus.NEW, "Купить пиццу"));
        inMemoryTaskManager.createTask(new Task("Таск 5", TaskStatus.NEW, "Купить мороженое"));
        inMemoryTaskManager.createTask(new Task("Таск 6", TaskStatus.NEW, "Купить билеты"));
        inMemoryTaskManager.createTask(new Task("Таск 7", TaskStatus.NEW, "Купить путёвку"));
        inMemoryTaskManager.createTask(new Task("Таск 8", TaskStatus.NEW, "Купить что-то"));
        inMemoryTaskManager.createTask(new Task("Таск 9", TaskStatus.NEW, "Купить велосипед"));
        inMemoryTaskManager.createTask(new Task("Таск 10", TaskStatus.NEW, "Отдохнуть"));
        inMemoryTaskManager.createTask(new Task("Таск 11", TaskStatus.NEW, "Помыть посуду"));
        inMemoryTaskManager.createTask(new Task("Таск 12", TaskStatus.NEW, "Сделать уборку"));
        inMemoryTaskManager.createTask(new Task("Таск 13", TaskStatus.NEW, "Подготовить одежду"));
        inMemoryTaskManager.createEpic(new Epic("Эпик 1", TaskStatus.NEW, "Покупка квартиры"));
        inMemoryTaskManager.createEpic(new Epic("Эпик 2", TaskStatus.NEW, "Продажа дачи"));
        inMemoryTaskManager.createEpic(new Epic("Эпик 3", TaskStatus.NEW, "Покупка машины"));
        inMemoryTaskManager.createEpic(new Epic("Эпик 4", TaskStatus.NEW, "Ремонт квартиры"));
        inMemoryTaskManager.createEpic(new Epic("Эпик 5", TaskStatus.NEW, "Переезд"));
        inMemoryTaskManager.createEpic(new Epic("Эпик 6", TaskStatus.NEW, "Полёт на курорт"));
        inMemoryTaskManager.addSubTask(new SubTask("Сабтаск 1", TaskStatus.NEW, "---", 15));
        inMemoryTaskManager.addSubTask(new SubTask("Сабтаск 2", TaskStatus.NEW, "---", 15));
        inMemoryTaskManager.addSubTask(new SubTask("Сабтаск 3", TaskStatus.NEW, "---", 16));

        for (int i = 1; i <= 13; i++) {
            inMemoryTaskManager.getTaskById(i);
        }
        for (int i = 14; i <= 18; i++) {
            inMemoryTaskManager.getEpicById(i);
        }
        inMemoryTaskManager.getTaskById(4);
        inMemoryTaskManager.getSubtaskById(20);
        inMemoryTaskManager.getSubtaskById(21);
        inMemoryTaskManager.getTaskById(9);
        inMemoryTaskManager.getSubtaskById(22);

        System.out.println(inMemoryTaskManager.getHistory());
    }
}