package co.com.pragma.crediya.usecase.user.exceptions;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends BusinessException {

    private final String email;

    public UserAlreadyExistsException(String email) {
        super("USER_ALREADY_EXISTS", "Ya existe un usuario con el correo " + email);
        this.email = email;
    }

}
