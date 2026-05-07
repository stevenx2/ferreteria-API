package org.ferreteria;


import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.sql.DataSource;
import java.util.Properties;


/**
 * clase de configuraciones para las transacciones e jpa usando hibernate
 */


@Configuration
@Import(BasicDataSourceCfg.class)
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "org.ferreteria.repositories")
@ComponentScan(basePackages = {"org.ferreteria.services","org.ferreteria.repositories"})
public class TransactionCfg {

    /**
     * El dataSource viene de la configuración que se importó.
     */
    @Autowired
    DataSource dataSource;


    /**
     * Uso la implementación de hibernate
     */
    @Bean
    public JpaVendorAdapter vendorAdapter(){
        return new HibernateJpaVendorAdapter();
    }


    @Bean
    public PlatformTransactionManager transactionManager(){
        var transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }


    /**
     * Uno de los bean más importantes. Configuro el entityManager con el paquete donde busca las entidades y la fuente de datos (base de datos)
     * @return El entityManager usado por spring para las consultas
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        var manager = new LocalContainerEntityManagerFactoryBean();
        manager.setJpaVendorAdapter(vendorAdapter());
        manager.setPackagesToScan("org.ferreteria.entities");
        manager.setDataSource(dataSource);
        manager.setJpaProperties(hibernateProperties());
        return manager;
    }


    /**
     * propiedades de hibernate en la base de datos. en este caso no permito que edite la estructura de
     * mi base de datos con la propiedad HBM2DDL_AUTO = 'nonr'
     * @return las propiedades de hibernate
     */
    @Bean
    public Properties hibernateProperties(){
        Properties jpaProps = new Properties();
        jpaProps.put(Environment.HBM2DDL_AUTO, "none");
        jpaProps.put(Environment.FORMAT_SQL, false);
        jpaProps.put(Environment.STATEMENT_BATCH_SIZE, 30);
        jpaProps.put(Environment.USE_SQL_COMMENTS, false);
        jpaProps.put(Environment.SHOW_SQL, false);
        return jpaProps;
    }
}
