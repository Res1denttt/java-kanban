import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private List<Subtask> subtaskList;

    public Epic(String name, String description, Status status) {
        super(name, description, status);
        subtaskList = new ArrayList<>();
    }

    public List<Subtask> getSubtaskList() {
        return subtaskList;
    }

    public void addSubtask(Subtask subtask) {
        subtaskList.add(subtask);
    }
}
