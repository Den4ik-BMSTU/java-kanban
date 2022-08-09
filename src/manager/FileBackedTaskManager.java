package manager;

import exception.ManagerSaveException;
import task.*;

import java.io.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class FileBackedTaskManager extends  InMemoryTaskManager {

    public static void main(String[] args) {

        File fileForExample = new File("src/testFile.csv");
        FileBackedTaskManager fileBackedTasksManager = new FileBackedTaskManager(fileForExample);
        fileBackedTasksManager.createTask(new Task("Таск 1", TaskType.TASK, TaskStatus.NEW, "Задача1"));
        fileBackedTasksManager.createTask(new Task("Таск 2", TaskType.TASK, TaskStatus.NEW, "Задача2"));
        fileBackedTasksManager.createEpic(new Epic("Epic 1", TaskType.EPIC, TaskStatus.NEW, "Эпик 1"));
        fileBackedTasksManager.createEpic(new Epic("Epic 2", TaskType.EPIC, TaskStatus.NEW, "Эпик 2"));
        fileBackedTasksManager.addSubTask(new SubTask("SubTask1", TaskType.SUBTASK, TaskStatus.NEW, "Субтаск 1", 3));
        fileBackedTasksManager.addSubTask(new SubTask("SubTask 2", TaskType.SUBTASK, TaskStatus.NEW, "Субтаск 2", 2));


        fileBackedTasksManager.getTaskById(2);
        fileBackedTasksManager.getSubtaskById(6);
        fileBackedTasksManager.getTaskById(2);
        fileBackedTasksManager.getEpicById(3);
        fileBackedTasksManager.getEpicById(1);
        fileBackedTasksManager.getSubtaskById(5);
        fileBackedTasksManager.getEpicById(4);
        //fileBackedTasksManager.getSubtaskById(5);
        //fileBackedTasksManager.getSubtaskById(7);
        //fileBackedTasksManager.getSubtaskById(6);

        FileBackedTaskManager fileBackedTasksManager2 = FileBackedTaskManager.loadFromFile(
                new File("src/testFile.csv"));
        System.out.println(fileBackedTasksManager2);
    }

    private final File file;
    private static final String HEADER = "id,type,name,status,description,epic";

    public FileBackedTaskManager (File file) {
        super();
        this.file = file;
    }

    private static List<Integer> historyFromString (String value){ // для сохранения и восстановления менеджера истории из CSV.
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

    private static Task fromString(String value) { // метод создания задачи из строки

        String[] line = value.split(",");
        int id = Integer.parseInt(line[0]);
        TaskType type = TaskType.valueOf(line[1]);
        String name = line[2];
        TaskStatus status = TaskStatus.valueOf(line[3]);
        String description = line[4];

        switch (type) {
            case EPIC:
                Epic epic = new Epic(name, type, status, description);
                epic.setId(id);
                return epic;
            case SUBTASK:
                SubTask subTask = new SubTask(name, type, status, description, Integer.parseInt(line[5].trim()));
                subTask.setId(id);
                return subTask;
            case TASK:
                Task task = new Task(name, type, status, description);
                task.setId(id);
                return task;
        }
        return null;
    }

    private String toStringFormat(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId())
                .append(",").append(task.getType())
                .append(",").append(task.getName())
                .append(",").append(task.getStatus())
                .append(",").append(task.getDesription())
                .append(",");
        TaskType type = task.getType();
        if (type == TaskType.SUBTASK) {
            SubTask subTusk = (SubTask) task;
            sb.append(subTusk.getEpicId());
        }
        return sb.toString();
    }

   /* private void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("id,type,name,status,description,epic\n");
            for (Task task : tasks.values()) {
                bw.write(toStringFormat(task));
                bw.newLine();
            }
            for (Task epic : epics.values()) {
                bw.write(toStringFormat(epic));
                bw.newLine();
            }
            for (Task subTask : subTasks.values()) {
                bw.write(toStringFormat(subTask));
                bw.newLine();
            }
            bw.write("\n");
            bw.write(toString(this.historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла", e);
        }
    }*/

    protected void save() {
        try (FileWriter fileWriter = new FileWriter(file, StandardCharsets.UTF_8)){
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(HEADER);
            writer.newLine();

            for (Task task : getTasks().values()) {
                writer.write(toStringFormat(task));
                writer.newLine();
            }
            for (Task task : getEpics().values()) {
                writer.write(toStringFormat(task));
                writer.newLine();
            }
            for (Task task : getAllSubTaks().values()) {
                writer.write(toStringFormat(task));
                writer.newLine();
            }
            writer.newLine();
            writer.write(toString(getHistory()));
            writer.newLine();
            writer.flush();
        } catch (IOException e){
            throw new ManagerSaveException("Mistayke", e);
        }
    }

    private static String toString(HistoryManager manager) { // для сохранения и восстановления менеджера истории из CSV.
        StringBuilder line = new StringBuilder();
        if (manager.getHistory().size() != 0) {
            for (Task task : manager.getHistory()) {
                line.append(task.getId()).append(",");
            }
        }
        return line.toString();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        int maxId = 0;
        int oldId;
        String dataFile;
        FileBackedTaskManager fileBackedTasksManager = new FileBackedTaskManager(file);
        try {
            dataFile = Files.readString(Path.of(file.getAbsolutePath()));
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла", e);
        }
        String[] data = dataFile.split("\n");
        int lastLine = data.length - 1;
        for (String dataLine : data) {
            String[] line = dataLine.split(",");
            if (line[0].equals("id") || dataLine.isEmpty()) {
                continue;
            }
            if (data[lastLine].equals(dataLine)) {
                loadHistory(fileBackedTasksManager, dataLine);
            } else {
                TaskType type = TaskType.valueOf(line[1]);
                Task task = fromString(dataLine);
                oldId = task.getId();
                switch (type) {
                    case TASK:
                        fileBackedTasksManager.tasks.put(oldId, task);
                        break;
                    case SUBTASK:
                        fileBackedTasksManager.subTasks.put(oldId, (SubTask) task);
                        break;
                    case EPIC:
                        fileBackedTasksManager.epics.put(oldId, (Epic) task);
                        break;
                }
                if (maxId < oldId) {
                    maxId = oldId;
                }
            }
        }
        fileBackedTasksManager.id = maxId;
        return fileBackedTasksManager;
    }

    private static void loadHistory(FileBackedTaskManager fileBackedTasksManager, String value) {
        List<Integer> history = historyFromString(value);
        for (Integer taskId : history) {
            if (fileBackedTasksManager.getTaskById(taskId) != null) {
                fileBackedTasksManager.historyManager
                        .add(fileBackedTasksManager.getTaskById(taskId));
            } else if (fileBackedTasksManager.getEpicById(taskId) != null) {
                fileBackedTasksManager.historyManager
                        .add(fileBackedTasksManager.getEpicById(taskId));
            } else if (fileBackedTasksManager.getSubtaskById(taskId) != null) {
                fileBackedTasksManager.historyManager
                        .add(fileBackedTasksManager.getSubtaskById(taskId));
            }
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
