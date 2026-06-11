package org.ferreteria.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.ferreteria.dto.PurchaseDto;
import org.ferreteria.services.PurchaseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig({MockConfig.class})
public class PurchasesControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<PurchaseDto> purchases;


    private MockMvc mockMvc;


    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .apply(springSecurity())
                .build();


        purchases = List.of(
                new PurchaseDto(1L,1L, LocalDate.of(2026,5,22)),
                new PurchaseDto(2L,2L, LocalDate.of(2024,6,16)),
                new PurchaseDto(3L,3L, LocalDate.of(2025,7,12))
        );
    }



    @WithMockUser
    @Test
    public void testFindAll() throws Exception{

        when(purchaseService.findAll())
                .thenReturn(purchases);


        mockMvc.perform(get("/api/v1/purchases"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(3)
                ).andExpect(
                        jsonPath("$[0].date")
                                .value("2026-05-22")
                );
    }


    @WithMockUser
    @Test
    public void testFindById() throws Exception{

        when(purchaseService.findById(anyLong()))
                .thenReturn(purchases.get(0));


        mockMvc.perform(get("/api/v1/purchases/1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.supplierId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.date")
                                .value("2026-05-22")
                );
    }




    @WithMockUser(roles = "ADMIN")
    @Test
    public void testSave() throws Exception{

        PurchaseDto body = new PurchaseDto();
        body.setSupplierId(1L);
        body.setDate(LocalDate.of(2020,5,10));

        when(purchaseService.save(any(PurchaseDto.class)))
                .thenReturn(purchases.get(0));


        mockMvc.perform(
                post("/api/v1/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
        )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.id")
                                .value(1)
                );

    }


    /**
     * Cuando es cuerpo de la petición no cumple las validaciones debe devolver estado de BAD_REQUEST junto con un json
     */
    @WithMockUser(roles = "ADMIN")
    @Test
    public void testSaveValidationError() throws Exception{
        // Este objeto no va a pasar la validación porque el ID debe ser nulo y la fecha de la compra no debe serlo
        PurchaseDto body = new PurchaseDto();
        body.setSupplierId(1L);
        body.setId(1L);
        body.setDate(null); // no es necesario, pero lo dejo por claridad

        when(purchaseService.save(any(PurchaseDto.class)))
                .thenReturn(purchases.get(0));


        mockMvc.perform(
                post("/api/v1/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
        )
                .andExpect(status().isBadRequest())
                // Se debe devolver un json de error en los que existan los campos que fallaron la validación (date y id)
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.supplierId").doesNotExist())
                .andExpect(jsonPath("$.id").exists());
    }



    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void testUpdate() throws Exception{

        PurchaseDto body = new PurchaseDto();
        body.setSupplierId(1L);
        body.setDate(LocalDate.of(2020,5,10));

        when(purchaseService.update(any(PurchaseDto.class)))
                .thenReturn(purchases.get(0));


        mockMvc.perform(
                put("/api/v1/purchases/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
        )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(1)
                );

    }
}
