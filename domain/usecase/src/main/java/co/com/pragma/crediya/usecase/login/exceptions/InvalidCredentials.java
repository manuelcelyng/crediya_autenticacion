package co.com.pragma.crediya.usecase.login.exceptions;

import co.com.pragma.crediya.usecase.BusinessException;

public class InvalidCredentials extends BusinessException {

    public InvalidCredentials(String code, String message) {
        super( code, message);
    }
}
