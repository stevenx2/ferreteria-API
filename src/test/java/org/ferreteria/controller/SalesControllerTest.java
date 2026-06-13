package org.ferreteria.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.ferreteria.dto.SaleDto;
import org.ferreteria.services.SaleService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig({MockConfig.class})
public class SalesControllerTest {


    @Autowired
    private SaleService saleService;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private ObjectMapper objectMapper;


    private MockMvc mockMvc;


    private List<SaleDto> sales;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(ctx)
                .apply(springSecurity())
                .build();


        sales = List.of(
                new SaleDto(1L, LocalDate.of(2023, 4, 12), 1L),
                new SaleDto(2L, LocalDate.of(2026, 1, 1), 3L)
        );
    }


    @WithMockUser
    @Test
    public void testFindAll() throws Exception {

        when(saleService.findAll())
                .thenReturn(sales);


        mockMvc.perform(get("/api/v1/sales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].date").value("2023-04-12"));

    }


    @WithMockUser
    @Test
    public void testFindById() throws Exception {

        when(saleService.findById(anyLong()))
                .thenReturn(sales.get(1));


        mockMvc.perform(get("/api/v1/sales/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2026-01-01"));

    }





    @WithMockUser(roles = "ADMIN")
    @Test
    public void testSaveSale() throws Exception{

        when(saleService.save(any()))
                .thenReturn(sales.get(0));



        SaleDto body = new SaleDto();
        body.setDate(LocalDate.of(2023,3,3));
        body.setClientId(2L);


        mockMvc.perform(
                post("/api/v1/sales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

    }



    @WithMockUser(roles = "ADMIN")
    @Test
    public void testUpdateSale() throws Exception{

        when(saleService.update(any()))
                .thenReturn(sales.get(0));


        SaleDto body = new SaleDto();
        body.setDate(LocalDate.of(2023,3,3));
        body.setClientId(2L);



        mockMvc.perform(
                        put("/api/v1/sales/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

    }



    @WithMockUser(roles = "ADMIN")
    @Test
    public void testDeleteSale() throws Exception{

        mockMvc.perform(delete("/api/v1/sales/1"))
                .andExpect(status().isOk());

    }
}
