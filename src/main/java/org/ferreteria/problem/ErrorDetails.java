package org.ferreteria.problem;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * clase modelo para mostrar respuestas de error en formato json.
 */
@Getter
public class ErrorDetails {

    //es de tipo string y no LocalDataTime por unos errores en cuanto a mapeo.
    private String timestamp;

    private String message;

    private int status;

    public ErrorDetails(String message, int status) {
        this.timestamp = LocalDateTime.now().toString();
        this.message = message;
        this.status = status;
    }
}
