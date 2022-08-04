package manager;

import task.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import static task.TaskType.SUBTASK;

public class FileBackedTaskManager extends  InMemoryTaskManager implements TaskManager{
    private final File file;

    public FileBackedTaskManager (File file) {
        super();
        this.file = file;
    }

    public static void main(String[] args) {

        File fileForExample = new File("src/manager/saveReports2.csv");
        FileBackedTaskManager fileBackedTasksManager = new FileBackedTaskManager(fileForExample);
        fileBackedTasksManager.createTask(new Task("Таск 1", TaskType.TASK, TaskStatus.NEW, "Задача1"));
        fileBackedTasksManager.createTask(new Task("Таск 2", TaskType.TASK, TaskStatus.NEW, "Задача2"));
        fileBackedTasksManager.createEpic(new Epic("Epic 1", TaskType.EPIC, TaskStatus.NEW, "Эпик 1"));
        fileBackedTasksManager.createEpic(new Epic("Epic 2", TaskType.EPIC, TaskStatus.NEW, "Эпик 2"));
        fileBackedTasksManager.addSubTask(new SubTask("SubTask1", TaskType.SUBTASK, TaskStatus.NEW, "Субтаск 1", 3));
        fileBackedTasksManager.addSubTask(new SubTask("SubTask 2", TaskType.SUBTASK, TaskStatus.NEW, "Субтаск 2", 3));
        fileBackedTasksManager.addSubTask(new SubTask("SubTask 3", TaskType.SUBTASK, TaskStatus.NEW, "Субтаск 3", 2));

        for (int i = 1; i <= 2; i++) {
            fileBackedTasksManager.getTaskById(i);
        }
        for (int i = 3; i <= 4; i++) {
            fileBackedTasksManager.getEpicById(i);
        }

        for (int i = 5; i <= 7; i++) {
            fileBackedTasksManager.getSubtaskById(i);
        }

        for (int i = 1; i <= 2; i++) {
            fileBackedTasksManager.getTaskById(i);
        }

        fileBackedTasksManager.getSubTask(3);
        File fileForExmaple2 = new File("src/manager/saveReports2.csv");
        FileBackedTaskManager fileBackedTasksManager2 = loadFromFile(fileForExmaple2);
        System.out.println(fileBackedTasksManager2);
    }

    public static List<Integer> historyFromString (String value){ // для сохранения и восстановления менеджера истории из CSV.
        List<Integer> history = new ArrayList<>();
        if (value.isEmpty()) {
          return history;
        }
        String[] line = value.split(",");
        for (String str : line){
            history.add(Integer.parseInt(str));
        }
        return history;
    }

    public Task fromString(String value) { // метод создания задачи из строки
        String name;
        String description;
        TaskStatus status;
        TaskType type;
        int id;

        String[] line = value.split(",");
        name = line[2];
        description = line[4];
        id = Integer.parseInt(line[0]);
        status = TaskStatus.valueOf(line[3]);
        type = TaskType.valueOf(line[1]);
        switch (type) {
            case EPIC:
                Epic epic = new Epic(name, type, status, description);
                epic.setId(id);
                return epic;
            case SUBTASK:
                SubTask subTask = new SubTask(name, type, status, description, Integer.parseInt(line[5]));
                subTask.setId(id);
                return subTask;
            case TASK:
                Task task = new Task(name, type, status, description);
                task.setId(id);
                return task;
        }
        return null;
    }

    public String toStringFormat(Task task) {
        return String.format(
                "%s,%s,%s,%s,%s,%s\n",
                task.getId(),
                task.getType(),
                task.getName(),
                task.getStatus(),
                task.getDesription(),
                null);
    }

    public void save() { // сохранять текущее состояние менеджера в указанный файл
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");

            Collection<Task> tasks = super.getTasks();
            Collection<Epic> epics = super.getEpics();
            Collection<SubTask> subTasks = super.getAllSubTaks();

            for (Task task : tasks) {
                writer.write(toStringFormat(task));
            }
            for (Epic epic : epics) {
                writer.write(toStringFormat(epic));
            }
            for (SubTask sub : subTasks) {
                writer.write(toStringFormat(sub));
            }
            writer.write("\n");
            writer.write(toString(getHistoryManager()));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public static String toString(HistoryManager manager) { // для сохранения и восстановления менеджера истории из CSV.
        StringBuilder line = new StringBuilder();
        if (manager.getHistory().size() != 0) {
            for (Task task : manager.getHistory()) {
                line.append(task.getId()).append(",");
            }
        }
        return line.toString();
    }

    public static FileBackedTaskManager loadFromFile(File file) { // восстанавливать данные менеджера из файла при запуске программы
        FileBackedTaskManager fileBackedTasksManager = new FileBackedTaskManager(file);
        try {
            String s = Files.readString(Path.of(String.valueOf(file)));
            if (s.isEmpty())
                return fileBackedTasksManager;
            String[] lines = s.split("\n");
            int i = 1;
            while (!lines[i].isEmpty()) {
                Task task = fileBackedTasksManager.fromString(lines[i]);
                ++i;
                fileBackedTasksManager.createTask(task);

                if (lines.length == i)
                    break;
            }
            if (i == lines.length) {
                return fileBackedTasksManager;
            } else {
                List<Integer> history = historyFromString(lines[lines.length - 1]);
                for (Integer idHistory : history) {
                    if (fileBackedTasksManager.getEpicById(idHistory) != null) {
                        fileBackedTasksManager.getHistoryManager().add(fileBackedTasksManager.getEpicById(idHistory));

                    }
                    if (fileBackedTasksManager.getSubtaskById(idHistory) != null) {
                        fileBackedTasksManager.getHistoryManager().add(fileBackedTasksManager.getSubtaskById(idHistory));

                    }
                    if (fileBackedTasksManager.getTaskById(idHistory) != null) {
                        fileBackedTasksManager.getHistoryManager().add(fileBackedTasksManager.getTaskById(idHistory));
                    }
                    return fileBackedTasksManager;
                }
            }
            return fileBackedTasksManager;
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public Task getTaskById(int taskId) {
        Task task = super.getTaskById(taskId);
        save();
        return task;
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic task = super.getEpicById(epicId);
        save();
        return task;
    }

    @Override
    public SubTask getSubtaskById(int subtaskId) {
        SubTask task = super.getSubtaskById(subtaskId);
        save();
        return task;
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void addSubTask(SubTask subTask) {
        super.addSubTask(subTask);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteSubTaskById(int subtaskId) {
        super.deleteSubTaskById(subtaskId);
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        super.updateSubtask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }

}
