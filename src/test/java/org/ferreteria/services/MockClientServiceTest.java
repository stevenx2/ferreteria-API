package org.ferreteria.services;


import org.ferreteria.entities.Client;
import org.ferreteria.entities.Sale;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.repositories.ClientRepo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Estos tests fueron con el simple propósito de practicar con mocks, no voy a agregar más tests a esta clase.
 */
@ExtendWith(MockitoExtension.class)
public class MockClientServiceTest {

    @Mock
    private ClientRepo repository;

    @InjectMocks
    private ClientServiceImpl service;


    Client client1;
    Client client2;

    /**
     * Datos de prubas de clientes
     */
    @BeforeEach
    void beforeEach(){
        Sale sale = new Sale();


        client1 = new Client();
        client1.setName("client1");
        client1.setAddress("Medellín");
        client1.setPhoneNumber("3234323232");
        sale.setClient(client1);
        sale.setDate(LocalDate.of(2020,8,20));
        client1.addSale(sale);


        client2 = new Client();
        client2.setName("client2");
        client2.setAddress("Bogotá");
        client2.setPhoneNumber("3234323444");
    }


    /**
     * Test con mocks al buscar todos los clientes
     */
    @Test
    void testFindAll(){

        when(repository.findAll())
                .thenReturn(List.of(client1,client2));


        List<Client> clients = service.findAll();

        assertEquals(2, clients.size());

        verify(repository).findAll();

    }


    /**
     * Test al buscar un cliente por id de forma correcta
     */
    @Test
    void testFindById(){
        when(repository.findById(anyLong()))
                .thenAnswer(invocation -> {


                    Long id = invocation.getArgument(0);

                    if(id == 1){
                        return Optional.of(client1);
                    }

                    throw new ResourceNotFound("Client with id " + id + " couldn't be found");
                });


        Client client = service.findById(1L);

        assertEquals("client1",client.getName());

        verify(repository).findById(1L);


    }



    @Test
    void testFindClientThrowsException(){


        when(repository.findById(anyLong()))
                .thenAnswer(invocation -> {
                    Long id = invocation.getArgument(0);

                    if(id == 1){
                        return Optional.of(client1);
                    }

                    throw new ResourceNotFound("Client with id " + id + " couldn't be found");
                });



        assertThrows(
                ResourceNotFound.class,
                () -> service.findById(2L)
        );
    }
}
