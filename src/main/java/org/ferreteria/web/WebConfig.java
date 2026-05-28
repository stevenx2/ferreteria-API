package org.ferreteria.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.validation.Validator;
import org.ferreteria.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * clase de configuraciones web
 */
@Import(AppConfig.class)
@ComponentScan(basePackages = {"org.ferreteria.controller","org.ferreteria.problem"})
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {


    @Autowired
    private MessageSource messageSource;

    /**
     *
     * Redijo por defecto al endpoint /api
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        WebMvcConfigurer.super.addViewControllers(registry);
        registry.addRedirectViewController("/","/api/v1");
    }


    /**
     * Este es el bean que valida los valores cuando se quiere actualizar o crear un nuevo registro de cualquier entidad,
     * evitando pasar por ejemplo nombre de clientes vacio. Además le especifico el messageSource que usará para devolver los
     * mensajes de errores en diferentes idiomas.
     */
    @Bean
    public Validator validator(){
        var validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource);
        return validator;
    }


    @Override
    public Validator getValidator() {
        return validator();
    }


    /**
     * Creo un mapper para usar en los tests, de esta manera puedo enviar parámetros requeridos de un endpoint en formato json.
     * También registro el modulo que me permite trabajar con fechas como LocalDate, evitando así excepciones.
     */
    @Bean
    public ObjectMapper objectMapper(){
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    /**
     * registro un modulo para que jackson maneje las fechas modernas  de java
     *
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter jsonConverter) {
                jsonConverter.getObjectMapper()
                        .registerModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            }
        }
    }

}
