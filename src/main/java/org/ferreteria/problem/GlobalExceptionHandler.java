package org.ferreteria.problem;


import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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




}
