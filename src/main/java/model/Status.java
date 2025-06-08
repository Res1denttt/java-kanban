package model;

public enum Status {
    NEW,
    IN_PROGRESS,
    DONE;

    public static Status parseStatus (String value) throws IllegalStateException {
        return switch(value) {
            case "NEW" -> NEW;
            case "IN_PROGRESS" -> IN_PROGRESS;
            case "DONE" -> DONE;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }
}
