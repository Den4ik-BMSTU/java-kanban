package manager;

import task.Task;
import task.SubTask;
import task.Epic;
import task.TaskStatus;


import java.util.*;


public class InMemoryTaskManager implements TaskManager {

    protected HistoryManager historyManager;
    protected Map<Integer, Task> tasks;
    protected Map<Integer, Epic> epics;
    protected Map<Integer, SubTask> subTasks;
    protected int id=0;

    public InMemoryTaskManager(){
        this.epics= new HashMap<>();
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();

    }

    private int newId(){
        return ++id;
    }
    // Task
    @Override
    public void createTask(Task task){
        task.setId(newId());
        tasks.put(task.getId(), task);
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
        historyManager.remove(taskId);
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
    public void deleteEpicById(int epicId) {
        Epic epic = epics.remove(epicId);
        for (Integer subTaskIds : epic.getSubTaskIDs()){
            subTasks.remove(subTaskIds);
            historyManager.remove(subTaskIds);
        }
        historyManager.remove(epicId);
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
        historyManager.remove(subTaskId);
    }

    @Override
    public void deleteAllSubTasks(){
        subTasks.clear();
        for (Integer numbers : subTasks.keySet()){
            setEpicStatus(epics.get(numbers));
        }
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        if (subTasks.containsKey(subTask.getId())) {
            subTasks.put(subTask.getId(), subTask);
            this.setEpicStatus(epics.get(subTask.getEpicId()));
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected HistoryManager getHistoryManager() {
        return historyManager;
    }

    private void setEpicStatus(Epic epic) {
        ArrayList<SubTask> subTasksUpd = new ArrayList<>();
        for (int i = 0; i < epic.getSubTaskIDs().size(); i++) {
            subTasksUpd.add(subTasks.get(epic.getSubTaskIDs().get(i)));
        }
        boolean isAllSubtaskNew = subTasksUpd.stream().allMatch(subtask -> subtask.getStatus().toString().equals("NEW"));
        boolean isAllSubtaskDONE = subTasksUpd.stream().allMatch(subtask -> subtask.getStatus().toString().equals("DONE"));

        if (isAllSubtaskNew){
            epic.setStatus(TaskStatus.NEW);
        } else if (isAllSubtaskDONE){
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

}