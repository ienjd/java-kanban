package Tasks;
import Enums.Statuses;
import Main.Main;
import java.util.Objects;

public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected Statuses status;

    public Task() {
    }

    public Task(String title, String description, int id, Statuses status) {
        this.title = title;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public static Task updateTask(int id) {
        Task task = (Task) Main.TaskMaster.findTask(id);
        Statuses status = ((Task) Main.TaskMaster.findTask(id)).status == Statuses.NEW ? task.setStatus(Statuses.IN_PROGRESS)
                : task.setStatus(Statuses.DONE);
        task.setStatus(status);
        addTaskToList(task);
        Main.TaskMaster.TASK_LIST.put(task.id, task);
        return task;
    }

    public Statuses setStatus(Statuses status) {
        this.status = status;
        return status;
    }

    public static Task createTask(String title, String description) {
        Task newTask = new Task(title, description, Main.TaskMaster.getIdCount() + 1, Statuses.NEW);
        Main.TaskMaster.setIdCount();
        return newTask;
    }

    public static void addTaskToList(Task task) {
        Main.TaskMaster.TASK_LIST.put(task.getId(), task);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id && Objects.equals(title, task.title);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "Task{ " +
                "title= " + title + '\'' +
                ", description= " + description + '\'' +
                ", id= " + id +
                ", status= " + status + ", class= " +
                getClass() + " " +
                " }";
    }
}

