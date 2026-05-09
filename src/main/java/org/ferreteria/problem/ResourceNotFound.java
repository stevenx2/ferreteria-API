package org.ferreteria.problem;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * excepción lanzada cuando un recurso no puedo ser encontrado, ya sea porque el id usado para buscar el recurso no
 * corresponde a ninguno u otro motivo
 */
public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String message) {
        super(message);
    }
}
