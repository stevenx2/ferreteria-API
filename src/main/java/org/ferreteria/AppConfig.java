package org.ferreteria;


import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * clase de configuración para el messageSource global de la aplicación
 */
@Configuration
public class AppConfig {

    /**
     *
     * este bean no pertenece directamente a las transacciones de la aplicación pero lo declaro aquí
     * para que exista en el contexto root de la aplicación (servicios, repositorios,etc) y poder devolver mensajes
     * en diferentes idiomas (español o inglés por defecto)
     */
    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setBasenames("i18n/global");
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }
}
