package co.com.pragma.crediya.shared.errors;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final String code;
    private final String message;
    private final int status;
    private final String path;
    private final Instant timestamp;
    private final String correlationId;
    private final List<ErrorDetail> details;

    public ErrorResponse(String code, String message, int status, String path, Instant timestamp, String correlationId, List<ErrorDetail> details) {
        this.code = code;
        this.message = message;
        this.status = status;
        this.path = path;
        this.timestamp = timestamp;
        this.correlationId = correlationId;
        this.details = details;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
    public int getStatus() { return status; }
    public String getPath() { return path; }
    public Instant getTimestamp() { return timestamp; }
    public String getCorrelationId() { return correlationId; }
    public List<ErrorDetail> getDetails() { return details; }

    public static ErrorResponse of(String code, String message, int status, String path, List<ErrorDetail> details, String correlationId) {
        return new ErrorResponse(code, message, status, path, Instant.now(), correlationId, details);
    }
}