package org.ferreteria.problem;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * Esta clase tiene la única finalidad de crear un errorHandling para usarse luego en la clase de seguridad
 * SecurityCfg. Esta clase permite que cuando un usuario entre a un recurso para el cual no tiene autorización,
 * el servidor devuelva una respuesta en formato json.
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // 1. Creamos tu estructura de error idéntica a la del Advice
        ErrorDetails errorDetails = new ErrorDetails(
                accessDeniedException.getMessage(),
                HttpStatus.FORBIDDEN.value()
        );

        // 2. Configuramos la respuesta HTTP como JSON y estado 403
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 3. Convertimos el objeto ErrorDetails a JSON usando Jackson de Spring
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}