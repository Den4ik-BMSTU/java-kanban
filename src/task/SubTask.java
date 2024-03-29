package task;

import java.time.LocalDateTime;
import java.util.Objects;

public class SubTask extends Task {
    private int epicId;

    // Полная версия конструктора.
    public SubTask(String name, String description, TaskStatus status, int epicId, int duration, LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        super.setType(TaskType.SUBTASK);
        this.epicId = epicId;
    }

    // Короткая версия для тестов.
    public SubTask(String name, String description, int epicId) {
        super(name, description);
        super.setType(TaskType.SUBTASK);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return super.toString() + epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}