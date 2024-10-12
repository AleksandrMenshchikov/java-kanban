package models;

import controllers.HistoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> history = new HashMap<>();
    Node<Task> head;
    Node<Task> tail;

    @Override
    public void add(Task task) {
        if (task != null) {
            remove(task.getId());
            history.put(task.getId(), linkLast(task));
        }
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            Node<Task> taskNode = history.get(id);
            removeNode(taskNode);
            history.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> taskList = new ArrayList<>();

        for (Node<Task> value : history.values()) {
            taskList.add(value.getData());
        }

        return taskList;
    }

    private Node<Task> linkLast(Task task) {
        Node<Task> taskNode = new Node<>(task);

        if (head == null) {
            head = taskNode;
        } else {
            if (tail == null) {
                taskNode.setPrev(head);
                tail = taskNode;
                head.setNext(tail);
            } else {
                taskNode.setPrev(tail);
                tail = taskNode;
            }
        }

        return taskNode;
    }

    private void removeNode(Node<Task> taskNode) {
        Node<Task> prev = taskNode.getPrev();
        Node<Task> next = taskNode.getNext();

        if (prev != null && next != null) {
            prev.setNext(next);
            next.setPrev(prev);
        } else if (prev == null && next != null) {
            tail = null;
            head = next;
            next.setPrev(null);
        } else if (prev == null) {
            head = null;
            tail = null;
        }
    }
}
