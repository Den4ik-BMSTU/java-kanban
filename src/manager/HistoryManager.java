package manager;

import task.Task;

import java.util.List;

public interface HistoryManager { //история просмотров

    void add(Task task); //добавление в список просмотра

    List<Task>getHistory(); //получение списка просмотра

    void remove(int id); //удаление задач из просмотора

}