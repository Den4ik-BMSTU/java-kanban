package manager;

import task.Task;
import task.SubTask;
import task.Epic;
import task.TaskStatus;

import java.util.*;

public interface TaskManager {



    // Task
    void createTask(Task task); // создание таска


    Collection<Task> getTasks(); //получение списка тасков

    Task getTaskById(int taskId); //получение таска по ИД

    void deleteAllTasks(); // удалить все таски

    void updateTask(Task task); //обновить таск

    void deleteTaskById(int taskId); //удалить таск по ИД

    //Epic
    void createEpic(Epic epic); //создание эпика

    void updateEpic(Epic epic); //обновление эпика

    Collection<Epic> getEpics(); //получение списка эпика

    Epic getEpicById(int epicId); // получение эпика по ИД

    void deleteAllEpics(); //удалить все эпики

    void deleteEpicById(int epicId); //удалить эпик по ИД

    //SubTasks
    void addSubTask(SubTask subTask); //добавление подзадачи

    Collection<SubTask> getSubTask(int epicId); //получение списка подзадач по эпику

    Collection<SubTask> getAllSubTaks(); //получение всех подзадач

    SubTask getSubtaskById(int subTaskId); //получение подзадачи по ИД

    void deleteSubTaskById(int subTaskId); //удаление подзадачи по ИД

    void deleteAllSubTasks(); //удалить все подзадачи

    void updateSubtask(SubTask subTask); //обновить подзадачу

    List<Task>getHistory();

    //вроде ничего не забыл
}
