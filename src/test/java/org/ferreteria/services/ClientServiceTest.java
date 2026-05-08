package org.ferreteria.services;


import org.ferreteria.TransactionCfg;
import org.ferreteria.entities.Client;
import org.ferreteria.problem.ResourceNotFound;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Sql({"classpath:/testcontainers/drop-scheme.sql","classpath:/testcontainers/create-scheme.sql"})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@SpringJUnitConfig(classes = {ClientServiceTest.ClientServiceTestCfg.class})
public class ClientServiceTest {



    @Autowired
    private ClientService service;


    @Test
    public void testFindAll(){
        List<Client> clients = service.findAll();
        assertEquals(6,clients.size());
        clients.forEach(System.out::println);
    }



    @Test
    public void TestFindClientWithSales(){
        Client client = service.findClientWithSales(1L);
        assertNotNull(client);
        assertEquals(1,client.getSales().size());

        LocalDate saleDate = client.getSales().get(0).getDate();
        assertEquals(LocalDate.of(2026,4,1),saleDate);

    }



    @Test
    public void testFindById(){
        assertThrows(ResourceNotFound.class,() -> service.findById(7L));

        Client client = service.findById(1L);
        assertNotNull(client);
        assertEquals("Johan Steven Mendoza Ruiz",client.getName());
    }



    @Test
    public void testFindByName(){
        List<Client> clients = service.findByName("Sofia Lopez");
        assertEquals(1,clients.size());
        assertEquals("3006666666",clients.get(0).getPhoneNumber());
    }


    @Import(TransactionCfg.class)
    @Configuration
    static class ClientServiceTestCfg{

        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
        }
    }



}
