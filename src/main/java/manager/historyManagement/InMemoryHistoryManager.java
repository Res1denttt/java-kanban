package manager.historyManagement;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Long, Node> historyMap = new HashMap<>();

    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        if (exists(task)) {
            removeNode(historyMap.get(task.getId()));
        }
        Node newNode = linkLast(task);
        historyMap.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(long id) {
        if (!exists(id)) return;
        Node node = historyMap.get(id);
        removeNode(node);
        historyMap.remove(id);
    }

    private void removeNode(Node node) {
        List<Task> lastViewedTasks = getTasks();
        if (lastViewedTasks.size() == 1) {
            head = null;
            tail = null;
        } else if (node.equals(head)) {
            head = node.getNext();
            head.setPrev(null);
        } else if (node.equals(tail)) {
            tail = node.getPrev();
            tail.setNext(null);
        } else {
            Node prevNode = node.getPrev();
            Node nextNode = node.getNext();
            prevNode.setNext(nextNode);
            nextNode.setPrev(prevNode);
        }
    }

    private Node linkLast(Task task) {
        final Node last = tail;
        final Node newNode = new Node(task, last, null);
        tail = newNode;
        if (last == null)
            head = newNode;
        else
            last.setNext(newNode);
        return newNode;
    }

    private List<Task> getTasks() {
        Node current = head;
        List<Task> lastViewedTasks = new ArrayList<>();
        while (current != null) {
            lastViewedTasks.add(current.getItem());
            current = current.getNext();
        }
        return lastViewedTasks;
    }

    private boolean exists(Task task) {
        return historyMap.containsKey(task.getId());
    }

    private boolean exists(long id) {
        return historyMap.containsKey(id);
    }
}
