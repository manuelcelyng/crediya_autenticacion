package co.com.pragma.crediya.usecase.exceptions;

public class InvalidCredentials extends BusinessException {

    public InvalidCredentials(String code) {
        super( code, "Usuario o contraseña inválidos");
    }
}
