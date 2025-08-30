package co.com.pragma.crediya.usecase.rol.exceptions;

import co.com.pragma.crediya.usecase.BusinessException;

public class RolNotFoundException extends BusinessException {

    public RolNotFoundException(String code, String message) {
        super(code, message);
    }
}
