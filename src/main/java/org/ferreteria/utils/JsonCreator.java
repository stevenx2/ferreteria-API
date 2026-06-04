package org.ferreteria.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * En esta clase van a estar todos los métodos utilizados para crear respuestas json que se utilizan en
 * varias partes de la aplicación, para de esa manera evitar la duplicación de código
 */
public class JsonCreator {


    /**
     * Usado para crear respuestas json donde las claves son los nombres de campos y los valores es el tipo de error en
     * objetos que tengan la anotación @Valid en un método de un  controlador y algún campo no cumpla con las validaciones, ya sea por un campo vacío u otra.
     * @param fieldErrors una lista de los campos de errores, esta lista se obtiene usando el método del getFieldErrors() del bindingResult.
     * @return un mapa donde las claves son los/el nombre(s) de los campos y los valores son el mensaje de error. Los mensajes son aquellos que están en anotaciones como @NotBlank del objeto DTO (paquete dto)
     */
    public static Map<String, String> createFieldsErrorJson(List<FieldError> fieldErrors){
        Map<String,String> errores = new HashMap<>();
        fieldErrors.forEach(fieldError -> {
            errores.put(fieldError.getField(),fieldError.getDefaultMessage());
        });

        return errores;
    }
}
