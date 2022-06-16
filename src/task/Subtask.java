package task;

public class Subtask extends Task {
    private Epic id;
    // Поменял на ID. А если в следующем ТЗ мне от эпика понадобится извлечь что-то иное, кроме АйДи то нужно будет
    // возвращаться обратно к объекту эпик?


    public Subtask(String title, String description, String status) {
        super(title, description, status);
    }

    public Subtask(String title, String description, String status, Epic id) {
        super(title, description, status);
        this.id = id;
    }

    public Epic getEpic() {
        return id;
    }

    public void setEpic(Epic id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Подзадача{" +
                "№=" + id +
                ", Название='" + title + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус='" + status + '\'' +
                '}';
    }
}