package co.com.pragma.crediya.shared.errors;

public class ErrorDetail {
    private final String field;
    private final String message;

    public ErrorDetail(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() { return field; }
    public String getMessage() { return message; }
}