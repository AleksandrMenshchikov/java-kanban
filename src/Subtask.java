public final class Subtask extends Task {
    private Integer epicId;

    public Subtask(int id, String title, String description) {
        super(id, title, description);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        String superString = super.toString();

        return superString.substring(0, superString.length() - 1) + ", " +
                "epicId=" + epicId +
                '}';
    }
}
