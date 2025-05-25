package manager.historyManagement;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Long, Node> historyMap = new HashMap<>();

    Node head;
    Node tail;

    @Override
    public void add(Task task) {
        long id = task.getId();
        if (historyMap.containsKey(id)) {
            removeNode(historyMap.get(id));
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
        if (!historyMap.containsKey(id)) return;
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


}
