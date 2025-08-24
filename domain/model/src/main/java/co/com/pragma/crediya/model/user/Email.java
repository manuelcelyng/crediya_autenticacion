package co.com.pragma.crediya.model.user;
import java.util.regex.Pattern;


public record Email (
        String email
){

    private static final Pattern SIMPLE =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    public Email {
        if(email==null || email.isBlank())
            throw new IllegalArgumentException("El correo electrónico no puede ser nulo ni vacio");
        String emailLowerCase = email.trim().toLowerCase();

        if(!SIMPLE.matcher(emailLowerCase).matches())
            throw new IllegalArgumentException("Correo electronico con formato inválido");
        email = emailLowerCase;
    }
}
