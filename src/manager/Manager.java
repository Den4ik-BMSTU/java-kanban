package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager { // класс для объекта менеджер
    private int id; //хранение задач для Задач, Подзадач и Эпиков:
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epics;

    public Manager() {
        id = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    //Задачи: добавляем task
    public void addTask(Task task) {
        task.setId(++id);
        tasks.put(id, task);
    }

    // храним task
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    // извлекаем task
    public Task getTask(int id) {
        return tasks.getOrDefault(id, null);
    }

    public List<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    // метод для удаления
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    //Аналогично для Эпиков
    public void addEpic(Epic epic) {
        epic.setId(++id);
        epic.setStatus("NEW");
        epics.put(id, epic);
    }

    public void updateEpic(Epic epic) {
        epic.setIdSubTasks(epics.get(epic.getId()).getIdSubTasks());
        epics.put(epic.getId(), epic);
        checkEpicStatus(epic);
    }

    public Epic getEpic(int id) {
        return epics.getOrDefault(id, null);
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epics.remove(id);
            for (Integer subtaskId : epic.getIdSubTasks()) {
                subtasks.remove(subtaskId);
            }
            epic.setIdSubTasks(new ArrayList<>());
        }
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    //Аналогично для подзадач
    public void addSubtask(Subtask subtask) {
        subtask.setId(++id);
        subtasks.put(id, subtask);
        subtask.getEpic().getIdSubTasks().add(id);
        checkEpicStatus(subtask.getEpic());
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        checkEpicStatus(subtask.getEpic());
    }

    public Subtask getSubtask(int id) {
        return subtasks.getOrDefault(id, null);
    }

    public ArrayList<Subtask> getSubTaskList(int epicId) {//Нахождение по id Epic'а всех subTask'ов
        ArrayList<Integer> subTaskFromEpic = getEpic(epicId).getIdSubTasks(); // stream() с наскока не получился(
        // но тема понравилась, буду изучать если получится, то к следующему спринту реализую
        ArrayList<Subtask> subTaskArrayList = new ArrayList<>();
        for (Integer integer : subTaskFromEpic) {
            subTaskArrayList.add(subTaskArrayList.get(integer));
        }
        return subTaskArrayList;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Epic epic = subtasks.get(id).getEpic();
            epic.getIdSubTasks().remove((Integer) id);
            checkEpicStatus(epic);
            subtasks.remove(id);
        }
    }

    public void deleteAllSubtask() {
        ArrayList<Epic> epicsForStatusUpdate = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtask.getEpic().setIdSubTasks(new ArrayList<>());
            if (!epicsForStatusUpdate.contains(subtask.getEpic())) {
                epicsForStatusUpdate.add(subtask.getEpic());
            }
        }
        subtasks.clear();
        for (Epic epic : epicsForStatusUpdate) {
            epic.setStatus("NEW");
        }
    }

    // статусы эпиков
    private void checkEpicStatus(Epic epic) {
        if (epic.getIdSubTasks().size() == 0) {
            epic.setStatus("NEW");
            return;
        }

        boolean isAllTaskIsNew = true; //есть ли какое-то правило по написанию is/has/does
        boolean isAllTaskIsDone = true;

        for (Integer epicSubtaskId : epic.getIdSubTasks()) {
            String status = subtasks.get(epicSubtaskId).getStatus();
            if (!status.equals("NEW")) {
                isAllTaskIsNew = false;
            }
            if (!status.equals("DONE")) {
                isAllTaskIsDone = false;
            }
        }

        if (isAllTaskIsDone) {
            epic.setStatus("DONE");
        } else if (isAllTaskIsNew) {
            epic.setStatus("NEW");
        } else {
            epic.setStatus("IN_PROGRESS");
        }

    }

}