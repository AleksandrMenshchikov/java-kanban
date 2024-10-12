package constants;

public enum StatusCode {
    OK(200, null),
    CREATED(201, "Ресурс успешно создан"),
    BAD_REQUEST(400, null),
    NOT_FOUND(404, "Ресурс не найден"),
    METHOD_NOT_ALLOWED(405, "Метод не поддерживается"),
    NOT_ACCEPTABLE(406, null);

    private final int code;
    private final String text;

    StatusCode(int value, String text) {
        this.code = value;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}

