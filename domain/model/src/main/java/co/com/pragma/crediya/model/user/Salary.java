package co.com.pragma.crediya.model.user;

import java.math.BigDecimal;

public record Salary (
        BigDecimal cantidad
){

    private static final BigDecimal MIN = new BigDecimal("0");
    private static final BigDecimal MAX = new BigDecimal("15000000");

    public Salary {
        if(cantidad == null) throw new IllegalArgumentException("El salario no puede ser nulo");
        if(cantidad.compareTo(MIN) < 0 || cantidad.compareTo(MAX) > 0 )
            throw new IllegalArgumentException("El salario debe estar entre 0 y 15000000");
    }

}
