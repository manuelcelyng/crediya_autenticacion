package co.com.pragma.crediya.usecase.exceptions;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends BusinessException {

    private final String email;

    public UserAlreadyExistsException(String code, String email) {
        super(code, "Ya existe un usuario con el correo " + email);
        this.email = email;
    }

}
