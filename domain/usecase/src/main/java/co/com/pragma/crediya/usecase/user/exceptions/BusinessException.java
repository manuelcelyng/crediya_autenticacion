package co.com.pragma.crediya.usecase.user.exceptions;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String code;
    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }
}
