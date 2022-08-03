package task;

import java.util.Objects;

public class SubTask extends Task{

    private int epicId;

    public SubTask(String name, TaskStatus status, String desription, int epicId) {
        super(name, status, desription);
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
                ", description='" + desription +
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
