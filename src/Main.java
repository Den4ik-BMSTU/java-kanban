import task.Epic;
import task.Subtask;
import task.Task;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();

        //Опишу свою реальную жизнь)
        Epic epic1 = new Epic("Эпик №1", "Ремонт");
        manager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Эпик1 Подзадача1", "Поклеить обои", "DONE", epic1);
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Эпик1 Подзадача2", "Доделать ремонт", "NEW", epic1);
        manager.addSubtask(subtask12);


        Epic epic2 = new Epic("Эпик №2", "Яндекс.Практикум");
        manager.addEpic(epic2);

        Subtask subtask21 = new Subtask("Эпик2 Подзадача1", "Доделать код на первую проверку для " +
                "финального проект 3 спринта", "DONE", epic2);
        manager.addSubtask(subtask21);

        System.out.println("Эпик = " + manager.getEpics());
        System.out.println("подзадача = " + manager.getSubtasks());
    }
}