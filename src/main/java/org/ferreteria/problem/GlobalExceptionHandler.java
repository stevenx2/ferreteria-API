package org.ferreteria.problem;


import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

/**
 * Controlador de errores
 */

@RestControllerAdvice
public class GlobalExceptionHandler {


    @Autowired
    private MessageSource messageSource;


    /**
     * Lanzado automáticamente cuando un recurso no existe
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
     * Devuelve un json con un mensaje de error un usuario entra a un recurso al cual no tiene permisos,
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


    /**
     * Cuando se realiza algo en la base de datos que viola la integridad de la base de datos, ej: Querer eliminar un proveedor que tiene productos
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrity(DataIntegrityViolationException ex) {
        ErrorDetails errorDetails = new ErrorDetails(
                ex.getMessage(),
                HttpStatus.CONFLICT.value()
        );

        return new ResponseEntity<>(errorDetails,HttpStatus.CONFLICT);
    }




    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<?> invalidFormatException(InvalidFormatException ex){

        ErrorDetails errorDetails = new ErrorDetails(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value()
        );

        return new ResponseEntity<>(errorDetails,HttpStatus.BAD_REQUEST);
    }




}
