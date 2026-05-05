package org.ferreteria;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:db/database.properties")
public class BasicDataSourceCfg {

    private String username;

    private String password;

    private String url;

    private String driverClassName;
}
