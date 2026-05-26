package org.ferreteria.controller;

import org.ferreteria.entities.Client;
import org.ferreteria.entities.Sale;
import org.ferreteria.problem.GlobalExceptionHandler;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.security.SecurityCfg;
import org.ferreteria.services.ClientService;
import org.ferreteria.services.ClientServiceImpl;
import org.ferreteria.web.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests para el controlador de clientes con Mocks
 */

@SpringJUnitWebConfig({WebConfig.class, SecurityCfg.class, ClientControllerTest.MockConfig.class})
@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Autowired
    private ClientService service;

    private List<Client> clients;

    /**
     * se ejecuta antes de cada método. instancio el mock y creo clientes que devuelven las respuestas
     */
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .apply(springSecurity())
                .build();


        Client client1 = new Client();
        client1.setName("client 1");
        client1.setAddress("address 1");
        client1.setPhoneNumber("1111111111");

        Client client2 = new Client();
        client2.setName("client 2");
        client2.setAddress("address 2");
        client2.setPhoneNumber("1111111112");
        Sale sale1 = new Sale();
        sale1.setClient(client2);
        sale1.setDate(LocalDate.of(2005,9,12));
        Sale sale2 = new Sale();
        sale2.setClient(client2);
        sale2.setDate(LocalDate.of(2020,3,8));
        client2.setSales(List.of(sale1,sale2));



        Client client3 = new Client();
        client3.setName("client 3");
        client3.setAddress("address 3");
        client3.setPhoneNumber("1111111113");


        clients = List.of(
                client1,client2,client3
        );
    }



    @WithMockUser
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



    @WithMockUser
    @Test
    public void testFindById() throws Exception {

        when(service.findClientWithSales(anyLong()))
                .thenReturn(clients.get(0));


        mockMvc.perform(get("/api/v1/clients/" + 1 ))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.name")
                                .value("client 1")
                );
    }



    @WithMockUser
    @Test
    public void testFindByIdThrowsException() throws Exception{
        when(service.findClientWithSales(anyLong()))
                .thenThrow(new ResourceNotFound("client couldn't be found"));


        mockMvc.perform(get("/api/v1/clients/" + 1 ))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.message")
                                .value("client couldn't be found")
                );

    }


    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void testDeleteClient() throws Exception{


        when(service.deleteById(anyLong()))
                .thenReturn(clients.get(1));


        mockMvc.perform(delete("/api/v1/clients/" + 1))
                .andExpect(status().isOk())

                .andExpect(
                        jsonPath("$.name")
                                .value("client 2")
                )
                .andExpect(
                        jsonPath("$.sales.length()")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$.sales[0].date")
                                .value("2005-09-12")
                );
    }


    /**
     * Validar que solo administradores puedan eliminar un cliente.
     */
    @WithMockUser(roles = "USER")
    @Test
    public void testNotAdminDeleteClient() throws Exception{

        when(service.deleteById(anyLong()))
                .thenReturn(clients.get(1));


        mockMvc.perform(delete("/api/v1/clients/" + 1))
                .andExpect(status().isForbidden())
                .andExpect(
                        jsonPath("$.status") // Se espera una respuesta json donde el atributo status sea 403, que significa que el recurso se denegó.
                                .value(403)
                );
    }




    @Configuration
    static class MockConfig{


        @Bean
        public ClientService clientService(){
            return Mockito.mock(ClientService.class);
        }
    }


}
