package org.ferreteria.controller;

import org.ferreteria.entities.Client;
import org.ferreteria.services.ClientServiceImpl;
import org.ferreteria.web.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * tests para el controlador de clientes con Mocks
 */
@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClientServiceImpl service;

    @InjectMocks
    private ClientController controller;


    private List<Client> clients;

    /**
     * se ejecuta antes de cada método. instancio el mock y creo clientes que devuelven las respuestas
     */
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        Client client1 = new Client();
        client1.setName("client 1");
        client1.setAddress("address 1");
        client1.setPhoneNumber("1111111111");

        Client client2 = new Client();
        client2.setName("client 2");
        client2.setAddress("address 2");
        client2.setPhoneNumber("1111111112");


        Client client3 = new Client();
        client3.setName("client 3");
        client3.setAddress("address 3");
        client3.setPhoneNumber("1111111113");


        clients = List.of(
                client1,client2,client3
        );
    }



    @Test
    public void testFindAll() throws Exception{

        when(service.findAllWithSales())
                .thenReturn(clients);


        mockMvc.perform(get("/api/v1/clients"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(3)
                )
                .andExpect(
                        jsonPath("$[0].name")
                                .value("client 1")
                );

    }



}
