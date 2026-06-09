package org.ferreteria.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ferreteria.dto.ClientDto;
import org.ferreteria.entities.Client;
import org.ferreteria.entities.Sale;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.security.SecurityCfg;
import org.ferreteria.services.ClientService;
import org.ferreteria.web.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasItems;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests para el controlador de clientes con Mocks
 */

@SpringJUnitWebConfig({MockConfig.class})
public class ClientsControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    private MockMvc mockMvc;

    @Autowired
    private ClientService service;

    private List<Client> clients;

    @Autowired
    private ObjectMapper objectMapper;

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
        sale1.setDate(LocalDate.of(2005, 9, 12));
        Sale sale2 = new Sale();
        sale2.setClient(client2);
        sale2.setDate(LocalDate.of(2020, 3, 8));
        client2.setSales(List.of(sale1, sale2));


        Client client3 = new Client();
        client3.setName("client 3");
        client3.setAddress("address 3");
        client3.setPhoneNumber("1111111113");


        clients = List.of(
                client1, client2, client3
        );
    }


    @WithMockUser
    @Test
    public void testFindAll() throws Exception {

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
    public void testFindByName() throws Exception {


        Client johan = new Client();
        johan.setName("Johan");
        johan.setAddress("Calle 201");

        when(service.findByName(anyString()))
                .thenReturn(List.of(johan));


        mockMvc.perform(get("/api/v1/clients").param("name", "johan"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$[0].name")
                                .value("Johan")
                ).andExpect(
                        jsonPath("$[0].address")
                                .value("Calle 201")
                ).andExpect(
                        jsonPath("$.length()")
                                .value(1)
                );

    }


    @WithMockUser
    @Test
    public void testFindByNameThrowsException() throws Exception {
        Client johan = new Client();
        johan.setName("Johan");
        johan.setAddress("Calle 201");

        when(service.findByName(anyString()))
                .thenThrow(new ResourceNotFound("Resource couldn't be found"));


        mockMvc.perform(get("/api/v1/clients").param("name", "johan"))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                ).andExpect(
                        jsonPath("$.message")
                                .value("Resource couldn't be found")
                );
    }


    @WithMockUser
    @Test
    public void testFindById() throws Exception {

        when(service.findClientWithSales(anyLong()))
                .thenReturn(clients.get(0));


        mockMvc.perform(get("/api/v1/clients/" + 1))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.name")
                                .value("client 1")
                );
    }


    @WithMockUser
    @Test
    public void testFindByIdThrowsException() throws Exception {
        when(service.findClientWithSales(anyLong()))
                .thenThrow(new ResourceNotFound("client couldn't be found"));


        mockMvc.perform(get("/api/v1/clients/" + 1))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.message")
                                .value("client couldn't be found")
                );

    }


    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void testDeleteClient() throws Exception {


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


    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void testUpdateClient() throws Exception {

        when(service.existsById(anyLong())).thenReturn(true);

        ClientDto client = new ClientDto(100L,"Franco", "4444444444", "Carrera 13");

        mockMvc.perform(
                put("/api/v1/clients/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(client))
        ).andExpect(status().isNoContent());
    }


    /**
     * Valido que el código de estado sea 400 cuando Validator encuentre un error en los valores dados actualizar un cliente,
     * estos valores incorrectos pueden ser: nombre vacio, teléfono con longitud distinta a 10, etc.
     */
    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void testUpdateThrowsException() throws Exception {

        when(service.existsById(anyLong())).thenReturn(true);
        /**
         * Paso todos los valores de manera incorrecta para ocasionar el error de validación al actualizar
         */
        ClientDto client = new ClientDto(100L,"F", "444444444", "CarreraCarreraCarreraCarreraCarreraCarreraCarreraCarreraCarreraCarreraCarreraCarreraCarreraCarreraCarreraCarreraCarreraCarrera");


        mockMvc.perform(
                        put("/api/v1/clients/" + 1)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsBytes(client))
                ).andExpect(status().isBadRequest())
                //verifico que devuelve un json en la respuesta con los campos inválidos como claves.
                .andExpect(jsonPath("$.keys()").value(hasItems("name", "phoneNumber", "address")));

    }


    /**
     * Test al guardar un cliente, valido que la url que se crea al recurso sea correcta y el código de estado sea 201
     */
    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void testSaveClient() throws Exception {

        Client response = new Client();
        response.setName("Johan");
        response.setAddress("Calle 120");
        response.setPhoneNumber("5555555555");
        response.setId(100L);


        ClientDto param = new ClientDto(100L,"Johan", "5555555555", "Calle 13");

        when(service.save(any(ClientDto.class)))
                .thenReturn(response);


        mockMvc.perform(
                        post("/api/v1/clients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(param))
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.name")
                                .value("Johan")
                )
                .andExpect(header().string("Location", containsString("/api/v1/clients/100"))); // el atributo Location del header debe contener la url base de la aplicación junto con el nuevo id del cliente guardado

    }


    /**
     * Validar que solo administradores puedan eliminar un cliente.
     */
    @WithMockUser(roles = "USER")
    @Test
    public void testNotAdminDeleteClient() throws Exception {

        when(service.deleteById(anyLong()))
                .thenReturn(clients.get(1));


        mockMvc.perform(delete("/api/v1/clients/" + 1))
                .andExpect(status().isForbidden())
                .andExpect(
                        jsonPath("$.status") // Se espera una respuesta json donde el atributo status sea 403, que significa que el recurso se denegó.
                                .value(403)
                );
    }


}
