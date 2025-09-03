package co.com.pragma.crediya.model.user.solicitudes;

import java.math.BigDecimal;

public interface UserLiteView {
    String getNombre();
    Long getSalarioBase();
    // opcional
    String getCorreoElectronico();
}