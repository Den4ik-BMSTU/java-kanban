package task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static task.TaskStatus.*;
import static task.TaskType.*;

public class Epic extends Task {

    private final List<SubTask> subTasks = new ArrayList<>();
    private List<Integer> subTaskIDs = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description);
        super.setType(EPIC);
        super.setStatus(NEW);
    }

    // Вычисляет время начала Эпика как время старта подзадачи, начинающейся раньше всех.
    public void calculateStartTime() {
        LocalDateTime epicStart = LocalDateTime.MAX;
        int nullCounter = 0;
        for (SubTask subTask : subTasks) {
            if (subTask.getStartTime() == null) {
                nullCounter++;
            } else if (subTask.getStartTime().isBefore(epicStart)) {
                epicStart = subTask.getStartTime();
            }
        }
        if (nullCounter == subTasks.size()) {
            epicStart = null;
        }
        setStartTime(epicStart);
    }

    // Вычисляет продолжительность Эпика как общую продолжительность всех его подзадач.
    public void calculateDuration() {
        int duration = 0;
        for (SubTask subTask : subTasks) {
            duration += subTask.getDuration();
        }
        setDuration(duration);
    }

    // Вычисляет время окончания Эпика как время окончания подзадачи, завершающейся позже всех.
    public void calculateEndTime() {
        LocalDateTime epicEnd = LocalDateTime.MIN;
        int nullCounter = 0;
        for (SubTask subTask : subTasks) {
            if (subTask.getEndTime() == null) {
                nullCounter++;
            } else if (subTask.getEndTime().isAfter(epicEnd)) {
                epicEnd = subTask.getEndTime();
            }
        }
        if (nullCounter == subTasks.size()) {
            epicEnd = null;
        }
        endTime = epicEnd;
    }

    public void setSubTaskID (SubTask subTask){
        subTaskIDs.add(subTask.getId());
    }

    public List<Integer> getSubTaskIDs() {
        return subTaskIDs;
    }

    public void removeSubTaskID(int subTaskId){
        subTaskIDs.remove(subTaskId);
    }


    @Override
    public String toString() {
        return super.toString()+subTaskIDs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return subTasks.equals(epic.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }

    public List<SubTask> getSubTasks() {
        return subTasks;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}