package org.ferreteria.problem;


import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 * controlador de errores
 */

@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * lanzado automáticamente cuando un recurso no existe
     * @param resourceNotFound el tipo de excepción que fue lanzada con su mensaje descriptivo
     * @return respuesta json con la hora, mensaje y código de estado del error.
     */
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<?> resourceNotFound(ResourceNotFound resourceNotFound){

        ErrorDetails errorDetails = new ErrorDetails(
                resourceNotFound.getMessage(),
                HttpStatus.NOT_FOUND.value()
        );

        return new ResponseEntity<>(errorDetails,HttpStatus.NOT_FOUND);

    }


    /**
     * devuelve un json con un mensaje de error un usuario entra a un recurso al cual no tiene permisos,
     * ej: un usuario común pidiendo un recurso solo permitido para administradores
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDenied(AccessDeniedException exception){

        ErrorDetails errorDetails = new ErrorDetails(
                exception.getMessage(),
                HttpStatus.FORBIDDEN.value()
        );

        return new ResponseEntity<>(errorDetails,HttpStatus.FORBIDDEN);
    }




}
