package model;

public enum TaskTypes {
    TASK,
    EPIC,
    SUBTASK;

    public static TaskTypes parseType(String value) throws IllegalStateException {
        return switch (value) {
            case "TASK" -> TASK;
            case "EPIC" -> EPIC;
            case "SUBTASK" -> SUBTASK;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }
}
