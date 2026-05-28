package org.ferreteria.services;


import org.ferreteria.TransactionCfg;
import org.ferreteria.dto.ClientDto;
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
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing del servicio de clientes con base de datos
 */

@Sql({"classpath:/testcontainers/drop-scheme.sql", "classpath:/testcontainers/create-scheme.sql"})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@SpringJUnitConfig(classes = {ClientServiceTest.ClientServiceTestCfg.class})
public class ClientServiceTest {


    @Autowired
    private ClientService service;


    @Test
    public void testFindAll() {
        List<Client> clients = service.findAll();
        assertEquals(6, clients.size());
        clients.forEach(System.out::println);
    }


    @Test
    public void TestFindClientWithSales() {
        Client client = service.findClientWithSales(1L);
        assertNotNull(client);
        assertEquals(1, client.getSales().size());

        LocalDate saleDate = client.getSales().get(0).getDate();
        assertEquals(LocalDate.of(2026, 4, 1), saleDate);

    }


    @SqlGroup(
            @Sql(value = "classpath:/testcontainers/delete-pepe.sql",
                    executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    )
    @Test
    public void testSaveClient() {
        ClientDto clientDto = new ClientDto("Pepe test", "4444444444", "calle 203");

        service.save(clientDto);
        assertEquals(7, service.findAll().size());

    }



    @Test
    public void testFindById() {
        Client client = service.findById(1L);
        assertNotNull(client);
        assertEquals("Johan Steven Mendoza Ruiz", client.getName());
    }


    @Test
    public void testFindByName() {
        //búsqueda por nombre exacto
        List<Client> clients = service.findByName("Sofia Lopez");
        assertEquals(1, clients.size());
        assertEquals("3006666666", clients.get(0).getPhoneNumber());

        // búsqueda por nombre inexacto
        List<Client> sofia = service.findByName("Sofia");
        assertEquals(1, sofia.size());
        assertEquals("3006666666", sofia.get(0).getPhoneNumber());

        //esperar una excepción cuando no se encuentra un cliente con ese nombre
        assertThrows(ResourceNotFound.class, () -> service.findByName("fdfdfdfdf"));
    }


    @Import(TransactionCfg.class)
    @Configuration
    static class ClientServiceTestCfg {

        @Bean
        public DataSource dataSource() {
            return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
        }
    }


}
