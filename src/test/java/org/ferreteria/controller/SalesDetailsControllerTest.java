package org.ferreteria.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.ferreteria.dto.SaleDetailDto;
import org.ferreteria.entities.Product;
import org.ferreteria.entities.Sale;
import org.ferreteria.entities.SaleDetail;
import org.ferreteria.services.SaleDetailService;
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
import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig({MockConfig.class})
public class SalesDetailsControllerTest {

    @Autowired
    private SaleDetailService saleDetailService;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private List<SaleDetail> details;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .apply(springSecurity())
                .build();


        details = Stream.of(1, 2, 3, 4)
                .map(v -> {
                    SaleDetail saleDetail = new SaleDetail();
                    saleDetail.setQuantity(v);
                    saleDetail.setId(Long.valueOf(v));
                    saleDetail.setSale(new Sale());
                    saleDetail.setProduct(new Product());
                    return saleDetail;
                }).toList();
    }


    @WithMockUser
    @Test
    public void testFindAll() throws Exception {
        when(saleDetailService.findAll())
                .thenReturn(details);


        mockMvc.perform(get("/api/v1/salesDetails"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4));
    }


    @WithMockUser
    @Test
    public void testFindById() throws Exception {

        when(saleDetailService.findById(anyLong()))
                .thenReturn(details.get(0));


        mockMvc.perform(get("/api/v1/salesDetails/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

    }


    @WithMockUser(roles = "ADMIN")
    @Test
    public void testSaveSaleDetail() throws Exception{


        when(saleDetailService.save(any(SaleDetailDto.class)))
                .thenReturn(details.get(0));


        SaleDetailDto body = new SaleDetailDto();
        body.setSaleId(1L);
        body.setProductId(1L);
        body.setQuantity(20);


        mockMvc.perform(
                post("/api/v1/salesDetails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantity").value(1));

    }


    @WithMockUser(roles = "ADMIN")
    @Test
    public void testUpdateSaleDetail() throws Exception{

        when(saleDetailService.update(any()))
                .thenReturn(details.get(0));


        SaleDetailDto body = new SaleDetailDto();
        body.setSaleId(1L);
        body.setProductId(1L);
        body.setQuantity(20);

        mockMvc.perform(
                        put("/api/v1/salesDetails/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body))
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(1));


    }



    @WithMockUser(roles = "ADMIN")
    @Test
    public void testDelete() throws Exception{

        mockMvc.perform(delete(("/api/v1/salesDetails/1")))
                .andExpect(status().isOk());

    }
}
