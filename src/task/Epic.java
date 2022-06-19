package task;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> idSubTasks;

    public Epic(String title, String description) {
        super(title, description, "");
        this.idSubTasks = new ArrayList<>();
    }

    public void setIdSubTasks(ArrayList<Integer> idSubTasks) {
        this.idSubTasks = idSubTasks;
    }

    public ArrayList<Integer> getIdSubTasks() {
        return idSubTasks;
    }

    @Override
    public String toString() {
        return "Эпик{" +
                "№=" + id +
                ", Название='" + title +
                "', Описание='" + description +
                "', Статус='" + status +
                "'}";
    }
}