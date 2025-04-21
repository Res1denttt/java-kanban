public class Subtask extends Task {
    Epic epic;

    public Subtask(String name, String description, Status status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
        epic.addSubtask(this);
    }
}
