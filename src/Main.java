import task.Epic;
import task.Subtask;
import task.Task;

public class Main {
    public static void main(String[] args) {
        System.out.println("Поехали!");
        Manager manager = new Manager();

        Epic epic1 = new Epic("Эпик №1", "Жизнь");
        manager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Эпик1 Подзадача1", "Рождение", "DONE", epic1);
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Эпик1 Подзадача2", "Взросление", "IN_PROGRESS", epic1);
        manager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Эпик1 Подзадача3", "Размножение", "NEW", epic1);
        manager.addSubtask(subtask13);
        Subtask subtask12New = new Subtask("Эпик1 Подзадача2 изменена", "Старение", "NEW", epic1);
        subtask12New.setId(12);
        manager.updateSubtask(subtask12New);

        Epic epic2 = new Epic("Эпик №2", "ЯндексПрактикум");
        manager.addEpic(epic2);

        Subtask subtask21 = new Subtask("Эпик2 Подзадача1", "Спринт № 1, 2, 3", "DONE", epic2);
        manager.addSubtask(subtask21);



        System.out.println("Эпик = " + manager.getEpics());
        System.out.println("подзадача = " + manager.getSubtasks());


    }
}