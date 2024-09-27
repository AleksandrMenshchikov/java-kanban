package models;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public final class Epic extends Task {
    private final ArrayList<Integer> subtaskIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(int id, String title, String description, LocalDateTime startTime, Duration duration) {
        super(id, title, description, startTime, duration);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void addSubtask(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void deleteSubtaskId(int subtaskId) {
        for (int i = 0; i < subtaskIds.size(); i++) {
            if (subtaskIds.get(i).equals(subtaskId)) {
                subtaskIds.remove(i);
                break;
            }
        }
    }

    @Override
    public String toString() {
        String superString = super.toString();

        return superString.substring(0, superString.length() - 1) + ", " +
                "endTime=" + endTime +
                "subtaskIds=" + subtaskIds +
                '}';
    }
}
