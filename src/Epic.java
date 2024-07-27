import java.util.HashMap;

public final class Epic extends Task {
    private final HashMap<Integer, Integer> subtaskIds = new HashMap<>();

    public Epic(String title, String description) {
        super(title, description);
    }

    public void addSubtask(int subtaskId) {
        subtaskIds.put(subtaskId, subtaskId);
    }

    public HashMap<Integer, Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void deleteSubtaskId(int subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    @Override
    public String toString() {
        String superString = super.toString();

        return superString.substring(0, superString.length() - 1) + ", " +
                "subtaskIds=" + subtaskIds.values() +
                '}';
    }
}
