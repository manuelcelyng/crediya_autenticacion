package co.com.pragma.crediya.usecase.exceptions;

public class RolNotFoundException extends BusinessException {



    public RolNotFoundException(String code, String idRol) {
        super(code,  "El rol  no fue encontrado, id: " + idRol);
    }
}
