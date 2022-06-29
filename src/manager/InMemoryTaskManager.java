package manager;

import task.Task;
import task.SubTask;
import task.Epic;
import task.TaskStatus;
import manager.InMemoryHistoryManager;

import java.util.*;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {

    private HistoryManager historyManager;
    private Map<Integer, Task> tasks;
    private Map<Integer, Epic> epics;
    private Map<Integer, SubTask> subTasks;
    private int i=0;

    public InMemoryTaskManager(){
        this.epics= new HashMap<>();
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();

    }

    private int newId(){
        return ++i;
    }
    // Task
    @Override
    public void createTask(Task task){
        task.setId(newId());
    }

    @Override
    public Collection<Task> getTasks(){
        return tasks.values();
    }

    @Override
    public Task getTaskById(int taskId){
        historyManager.add(tasks.get(taskId));
        return tasks.get(taskId);
    }

    @Override
    public void deleteAllTasks(){
        tasks.clear();
    }

    @Override
    public void updateTask(Task task){
        if (tasks.containsKey(task.getId())){
            tasks.put(task.getId(),task);
        }

    }

    @Override
    public void deleteTaskById(int taskId){
        tasks.remove(taskId);
    }

    //Epic
    @Override
    public void createEpic(Epic epic){
        epic.setId(newId());
        epics.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            this.setEpicStatus(epic);
        }
    }

    @Override
    public Collection<Epic> getEpics(){
        return epics.values();
    }

    @Override
    public Epic getEpicById(int epicId){
        historyManager.add(epics.get(epicId));
        return epics.get(epicId);
    }

    @Override
    public void deleteAllEpics(){
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteEpicById(int epicId) { //ты имел ввиду вот так, покороче код сделать?
        Epic epic = epics.get(epicId);
        for (Integer subTaskIds : epic.getSubTaskIDs()){
            subTasks.remove(subTaskIds);
        }
        epics.remove(epicId);
    }



    //SubTasks
    @Override
    public void addSubTask(SubTask subTask){
        int epicIdOfSubTask = subTask.getEpicId();
        Epic epic = epics.get(epicIdOfSubTask);
        if (epic != null){
            int subTaskId = this.newId();
            subTask.setId(subTaskId);
            subTasks.put(subTaskId, subTask);
            epic.setSubTaskID(subTask);
            this.setEpicStatus(epics.get(subTask.getEpicId()));
        }
    }

    @Override
    public Collection<SubTask> getSubTask(int epicId){
        Epic epic = epics.get(epicId);
        ArrayList<SubTask> subTasksArrayList = new ArrayList<>();
        List<Integer> subTasksFromEpic = epic.getSubTaskIDs();
        for (Integer integer : subTasksFromEpic) {
            subTasksArrayList.add(subTasks.get(integer));
        }
        return subTasksArrayList;
    }

    @Override
    public Collection<SubTask> getAllSubTaks(){
        return subTasks.values();
    }

    @Override
    public SubTask getSubtaskById(int subTaskId){
        historyManager.add(subTasks.get(subTaskId));
        return subTasks.get(subTaskId);
    }

    @Override
    public void deleteSubTaskById(int subTaskId){
        int epicIdOfSubTask = subTasks.get(subTaskId).getEpicId();
        Epic epic = epics.get(epicIdOfSubTask);
        subTasks.remove(subTaskId);
        List<Integer> subTasksFromEpic = epic.getSubTaskIDs();
        for (int i = 0; i < subTasksFromEpic.size(); i++){
            if (subTasksFromEpic.get(i) == subTaskId){
                epic.removeSubTaskID(i);
            }
        }
        this.setEpicStatus(epic);
    }

    @Override
    public void deleteAllSubTasks(){
        subTasks.clear();
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        int idUpdateSubtask = 0;
        for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
            if (Objects.equals(entry.getValue().getName(), subTask.getName()))
                idUpdateSubtask = entry.getValue().getId();
        }
        if (idUpdateSubtask > 0) {
            subTask.setId(idUpdateSubtask);
            subTasks.put(idUpdateSubtask, subTask);
            this.setEpicStatus(epics.get(subTask.getEpicId()));
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void setEpicStatus(Epic epic) { //перенёс
        ArrayList<SubTask> subTasksUpd = new ArrayList<>();
        for (int i = 0; i < epic.getSubTaskIDs().size(); i++) {
            subTasksUpd.add(subTasks.get(epic.getSubTaskIDs().get(i)));
        }
        Stream subTaskStreamNew = subTasksUpd.stream(); //ошибку не выдаёт
        Stream subTaskStreamDone = subTasksUpd.stream();
        if (subTaskStreamNew.allMatch(x->x=="NEW") || (subTasksUpd.size() == 0)){
            epic.setStatus(TaskStatus.NEW);
        } else if (subTaskStreamDone.allMatch(x->x=="DONE")){
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

}