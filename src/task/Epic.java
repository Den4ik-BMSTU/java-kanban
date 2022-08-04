package task;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Integer> subTaskIDs = new ArrayList<>();
    public Epic(String name, TaskType type, TaskStatus status, String description){
        super(name, type, status, description);
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
        return "Epic{" +
                "id=" + id +
                ", name='" + name +
                "', status=" + status +
                ", description='" + desription +
                "', subTaskIDs=" + subTaskIDs +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTaskIDs, epic.subTaskIDs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTaskIDs);
    }
}
