package org.ferreteria;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * clase de configuración para el bean datasource
 * */
@Slf4j
@Configuration
@PropertySource("classpath:db/database.properties")
public class BasicDataSourceCfg {

    @Value("${jdbc.username}")
    private String username;

    @Value("${jdbc.password}")
    private String password;

    @Value("${jdbc.url}")
    private String url;

    @Value("${jdbc.driverClassName}")
    private String driverClassName;


    @Bean(destroyMethod = "close")
    public DataSource dataSource(){

        try {
            HikariConfig hc = new HikariConfig();
            hc.setUsername(username);
            hc.setPassword(password);
            hc.setJdbcUrl(url);
            hc.setDriverClassName(driverClassName);

            HikariDataSource hd = new HikariDataSource(hc);
            hd.setMaximumPoolSize(10);
            return  hd;
        } catch (Exception e){
            log.error("Bean datasource cannot be created:{}", String.valueOf(e));
            return null;
        }
    }
}
