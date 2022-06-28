package task;

import java.util.Objects;

public class Task {
    protected int id;
    protected String name;
    protected TaskStatus status;
    protected String desription;

    public Task (String name, TaskStatus status, String desription){
        this.name=name;
        this.status=status;
        this.desription=desription;
    }

    //Set-еры
    public void setId(int id){
        this.id=id;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setStatus(TaskStatus status){
        this.status=status;
    }

    public void setDesription(String desription){
        this.desription=desription;
    }

    //Get-еры
    public int getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public TaskStatus getStatus(){
        return status;
    }

    public String getDesription(){
        return desription;
    }

    //Переопределения

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", desription='" + desription + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(status, task.status)
                && Objects.equals(desription, task.desription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, desription);
    }
}
