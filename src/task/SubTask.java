package task;

import java.util.Objects;

public class SubTask extends Task{

    private int epicId;

    public SubTask(String name, TaskType type, TaskStatus status, String description, int epicId) {
        super(name, type, status, description);
        this.epicId=epicId;
    }

    public void setEpicId(int epicId){
        this.epicId=epicId;
    }

    public int getEpicId(){
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", name='" + name +
                "', status=" + status +
                ", description='" + description +
                "', epicId=" + epicId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subtask = (SubTask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
