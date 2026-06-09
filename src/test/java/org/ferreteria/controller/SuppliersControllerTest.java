package org.ferreteria.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.ferreteria.dto.ProductDto;
import org.ferreteria.dto.SupplierDto;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.services.SupplierService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig({MockConfig.class})
public class SuppliersControllerTest {


    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private List<SupplierDto> suppliers;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .apply(springSecurity())
                .build();

        suppliers = List.of(
                new SupplierDto(1L, "Proveedor A", "1111111111", "dirección 1"),
                new SupplierDto(1L, "Proveedor B", "1111111112", "dirección 2"),
                new SupplierDto(1L, "Proveedor C", "1111111113", "dirección 3"),
                new SupplierDto(1L, "Proveedor D", "1111111114", "dirección 4"),
                new SupplierDto(1L, "Proveedor E", "1111111115", "dirección 5")
        );
    }


    @WithMockUser
    @Test
    public void testFindAll() throws Exception {
        when(supplierService.findAll())
                .thenReturn(suppliers);


        mockMvc.perform(
                        get("/api/v1/suppliers")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(5)
                );

    }


    @WithMockUser
    @Test
    public void testFindByNameLike() throws Exception {
        when(supplierService.findByNameLike(anyString()))
                .thenReturn(List.of(suppliers.get(0)));


        mockMvc.perform(
                        get("/api/v1/suppliers")
                                .param("name", "cualquier nombre")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(1)
                );

    }


    @WithMockUser
    @Test
    public void testFindById() throws Exception {

        when(supplierService.findById(anyLong()))
                .thenReturn(suppliers.get(0));


        mockMvc.perform(get("/api/v1/suppliers/1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.name")
                                .value("Proveedor A")
                )
                .andExpect(
                        jsonPath("$.address")
                                .value("dirección 1")
                );

    }






    @WithMockUser(roles = "ADMIN")
    @Test
    public void testSaveSupplier() throws Exception {


        SupplierDto body = new SupplierDto();
        body.setName("Proveedor X");
        body.setPhoneNumber("4444444444");
        body.setAddress("dirección X");

        when(supplierService.save(any()))
                .thenReturn(suppliers.get(0));


        mockMvc.perform(
                post("/api/v1/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
        )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.name")
                                .value("Proveedor A")
                );

    }


    @WithMockUser(roles = "ADMIN")
    @Test
    public void testSaveIsBadRequest() throws Exception{
        SupplierDto body = new SupplierDto();
        body.setName("P");
        body.setPhoneNumber("444444444");
        body.setAddress("dirección X");

        when(supplierService.save(any()))
                .thenReturn(suppliers.get(0));


        mockMvc.perform(
                        post("/api/v1/suppliers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body))
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.name").exists()
                ).andExpect(jsonPath("$.phoneNumber").exists())
                .andExpect(jsonPath("$.address").doesNotExist());
    }



    @WithMockUser(roles = "ADMIN")
    @Test
    public void testUpdateSupplier() throws Exception{

        SupplierDto body = new SupplierDto();
        body.setName("Proveedor X");
        body.setPhoneNumber("4444444444");
        body.setAddress("dirección X");

        when(supplierService.update(any()))
                .thenReturn(suppliers.get(0));


        mockMvc.perform(
                put("/api/v1/suppliers/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
        )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.name")
                                .value("Proveedor A")
                );
    }






    @WithMockUser(roles = "ADMIN")
    @Test
    public void testDeleteSupplier() throws Exception{

        mockMvc.perform(delete("/api/v1/suppliers/2"))
                .andExpect(status().isNoContent());

    }


}
