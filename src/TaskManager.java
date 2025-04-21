import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager {
    private long i;
    private final Map<Long, Task> tasks = new HashMap<>();
    private final Map<Long, Epic> epics = new HashMap<>();
    private final Map<Long, Subtask> subtasks = new HashMap<>();

    private long generateId() {
        return ++i;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.status = Status.NEW;
        }
    }

    public Task getTaskById(long id) {
        return tasks.get(id);
    }

    public Epic getEpicById(long id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(long id) {
        return subtasks.get(id);
    }

    public void addTask(Task task) {
        if (task == null) return;
        task.id = generateId();
        putInMap(task);
    }

    public void updateTask(Task task) {
        if (task == null) return;
        if (task.getClass() == Epic.class) {
            for (Subtask subtask : subtasks.values()) {
                if (subtask.epic.id == task.id) {
                    Epic epic = (Epic) task;
                    epic.addSubtask(subtask);
                    subtask.epic = epic;
                }
            }
        }
        putInMap(task);
    }

    private void putInMap(Task task) {
        switch (task) {
            case Epic e -> {
                updateEpicStatus(e);
                epics.put(task.id, e);
            }
            case Subtask s -> {
                updateEpicStatus(s.epic);
                subtasks.put(task.id, s);
            }
            case Task t -> tasks.put(task.id, task);
        }
    }

    public void deleteTaskById(long id) {
        if (!tasks.containsKey(id)) return;
        tasks.remove(id);
    }

    public void deleteEpicById(long id) {
        if (!epics.containsKey(id)) return;
        Epic epic = epics.get(id);
        for (Subtask subtask : epic.getSubtaskList()) {
            subtasks.remove(subtask.id);
        }
        epics.remove(id);
    }

    public void deleteSubtaskById(long id) {
        if (!subtasks.containsKey(id)) return;
        Subtask subtask = subtasks.get(id);
        subtasks.remove(id);
        Epic epic = subtask.epic;
        List<Subtask> subtaskList = epic.getSubtaskList();
        subtaskList.remove(subtask);
        updateEpicStatus(epic);
    }

    private void updateEpicStatus(Epic epic) {
        List<Subtask> subtaskList = epic.getSubtaskList();
        if (subtaskList.isEmpty()) {
            epic.status = Status.NEW;
            return;
        }
        int doneCount = 0;
        int newCount = 0;
        for (Subtask subtaskInList : subtaskList) {
            if (subtaskInList.status == Status.DONE) {
                doneCount++;
            } else if (subtaskInList.status == Status.NEW) {
                newCount++;
            }
        }
        if (doneCount == subtaskList.size()) {
            epic.status = Status.DONE;
        } else if (newCount == subtaskList.size()) {
            epic.status = Status.NEW;
        } else {
            epic.status = Status.IN_PROGRESS;
        }
    }
}
