package manager.historyManagement;


import model.Task;

import java.util.Objects;

public class Node {

    private Task item;
    private Node next;
    private Node prev;

    Node(Task item, Node prev, Node next) {
        this.item = item;
        this.prev = prev;
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(item, node.item) && Objects.equals(next, node.next) && Objects.equals(prev, node.prev);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, next, prev);
    }

    public Task getItem() {
        return item;
    }

    public void setItem(Task item) {
        this.item = item;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}
