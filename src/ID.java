public final class ID {
    private static int id;

    public static int getId() {
        id += 1;
        return id;
    }
}
