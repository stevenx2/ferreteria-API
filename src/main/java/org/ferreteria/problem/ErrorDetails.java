package org.ferreteria.problem;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * clase modelo para mostrar respuestas de error en formato json.
 */
@Getter
public class ErrorDetails {

    private LocalDateTime timestamp;

    private String message;

    private int status;

    public ErrorDetails(String message, int status) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.status = status;
    }
}
