package org.ferreteria.services;

import org.ferreteria.AppConfig;
import org.ferreteria.TransactionCfg;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;


/**
 * Todas las clases de test de servicios usan esta configuración
 */
@Configuration
@Import({TransactionCfg.class, AppConfig.class})
public class ServicesCfg {

    /**
     * Bean de una base de datos en memoria
     */
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
    }
}
