package manager;

import task.Task;
import task.SubTask;
import task.Epic;
import task.TaskStatus;
import manager.InMemoryHistoryManager;

import java.util.*;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {

    public HistoryManager historyManager = Managers.getDefaultHistory();

    private Map<Integer, Task> tasks = new HashMap<>();//здесь у тебя было замечание в прошлый раз, по поводу полиморфизма
    private Map<Integer, Epic> epics = new HashMap<>();
    private Map<Integer, SubTask> subTasks = new HashMap<>();
    private int i=0;

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
        return tasks.get(taskId);
    }

    @Override
    public void deleteAllTasks(){
        tasks.clear();
    }

    @Override
    public void updateTask(Task task){
        int idUpdateTask=0;
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()){
            if(Objects.equals(entry.getValue().getName(), task.getName())){
                idUpdateTask=entry.getValue().getId();
            }
            if (idUpdateTask>0){
                task.setId(idUpdateTask);
                tasks.put(idUpdateTask, task);
            }
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
        int idUpdateEpic = 0;
        for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
            if (Objects.equals(entry.getValue().getName(), epic.getName()))
                idUpdateEpic = entry.getValue().getId();
        }
        if (idUpdateEpic > 0) {
            epic.setId(idUpdateEpic);
            for (int i = 0; i < epics.get(idUpdateEpic).getSubTaskIDs().size(); i++) {
                SubTask subTask = subTasks.get(epics.get(idUpdateEpic).getSubTaskIDs().get(i));
                epic.setSubTaskID(subTask);
            }
            epics.put(idUpdateEpic, epic);
        }
    }

    @Override
    public Collection<Epic> getEpics(){
        return epics.values();
    }

    @Override
    public Epic getEpicById(int epicId){
        return epics.get(epicId);
    }

    @Override
    public void deleteAllEpics(){
        epics.clear();
        subTasks.clear();
    }

    @Override
    public void deleteEpicById(int epicId) {
        epics.remove(epicId);

        ArrayList<Integer> idDelSubtasks = new ArrayList<>();
        for (Map.Entry<Integer, SubTask> entry : subTasks.entrySet()) {
            if (entry.getValue().getEpicId() == epicId)
                idDelSubtasks.add(entry.getValue().getId());
        }

        for (Integer idDelSubtask : idDelSubtasks) {
            subTasks.remove(idDelSubtask);
        }
    }

    private void setEpicStatus(Epic epic) { //со stream() не получается, посмотри, если что, то могу тебе в Slack написать?
        TaskStatus oldTaskStatus = epic.getStatus();
        ArrayList<SubTask> subTasksUpd = new ArrayList<>();
        for (int i = 0; i < epic.getSubTaskIDs().size(); i++) {
            subTasksUpd.add(subTasks.get(epic.getSubTaskIDs().get(i)));
        }
        /*Stream subTaskStream = subTasksUpd.stream();
        if (subTaskStream.allMatch(x->x=="NEW") || (subTasksUpd.size() == 0)){
            epic.setStatus(TaskStatus.NEW);
        } else if (subTaskStream.allMatch(x->x=="DONE")){
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }*/
        int counterDone = 0;
        int counterNew = 0;
        for (SubTask subTask : subTasksUpd) {
            switch (subTask.getStatus()) {
                case NEW:
                    counterNew++;
                    break;
                case IN_PROGRESS:
                    break;
                case DONE:
                    counterDone++;
                    break;
            }
        }

        if (subTasksUpd.size() == 0) {
            epic.setStatus(TaskStatus.NEW);
        } else if (counterDone == subTasksUpd.size()) {
            epic.setStatus(TaskStatus.DONE);
        } else if (counterNew == subTasksUpd.size()) {
            epic.setStatus(oldTaskStatus);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    //SubTasks
    @Override
    public void addSubTask(SubTask subTask){
        int subTaskId = this.newId();
        subTask.setId(subTaskId);
        subTasks.put(subTaskId, subTask);
        int epicIdOfSubTask = subTask.getEpicId();
        Epic epic = epics.get(epicIdOfSubTask);
        if (epic != null){
            epic.setSubTaskID(subTask);
            this.setEpicStatus(epics.get(subTask.getEpicId()));
        }
    }

    @Override
    public ArrayList<SubTask> getSubTask(int epicId){
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

}