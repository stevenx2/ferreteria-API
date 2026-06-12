package org.ferreteria.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.ferreteria.dto.PurchaseDetailDto;
import org.ferreteria.services.PurchaseDetailService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringJUnitWebConfig(MockConfig.class)
public class PurchasesDetailsControllerTest {

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private List<PurchaseDetailDto> details;



    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(ctx)
                .apply(springSecurity())
                .build();


        details = List.of(
                new PurchaseDetailDto(1L,1L,1L,40),
                new PurchaseDetailDto(2L,1L,2L,50)
        );
    }



    @WithMockUser
    @Test
    public void testFindAll() throws Exception{

        when(purchaseDetailService.findAll())
                .thenReturn(details);



        mockMvc.perform(get("/api/v1/purchasesDetails"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                );

    }


    @WithMockUser
    @Test
    public void testFindById() throws Exception{

        when(purchaseDetailService.findById(anyLong()))
                .thenReturn(details.get(0));



        mockMvc.perform(get("/api/v1/purchasesDetails/3"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.productId")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.quantity")
                                .value(40)
                );
    }



    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void testSave() throws Exception{


        PurchaseDetailDto body = new PurchaseDetailDto();
        body.setPurchaseId(1L);
        body.setProductId(2L);
        body.setQuantity(20);

        when(purchaseDetailService.save(any()))
                .thenReturn(details.get(0));


        mockMvc.perform(
                post("/api/v1/purchasesDetails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))

        ).andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.id")
                                .value(1)
                ).andExpect(
                        jsonPath("$.quantity")
                                .value(40)
                );

    }


    /**
     * En este test se verifica que cuando un objeto con la anotación @Valid no cumpla con las especificaciones, entonces
     * se devuelve un json de error. Solo voy a hacer el test para el método de guardar, ya que para el actualizado es lo mismo
     */
    @WithMockUser(roles = "ADMIN")
    @Test
    public void testValidationError() throws Exception{


        // este cuerpo va a devolver el json de error porque el ID del producto es nulo y la cantidad no es mínimo 1
        PurchaseDetailDto body = new PurchaseDetailDto();
        body.setPurchaseId(1L);
        body.setProductId(null);
        body.setQuantity(0);

        when(purchaseDetailService.save(any()))
                .thenReturn(details.get(0));


        mockMvc.perform(
                post("/api/v1/purchasesDetails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))

        )
                .andExpect(jsonPath("$.quantity").exists())
                .andExpect(jsonPath("$.productId").exists())
                .andExpect(jsonPath("$.purchaseId").doesNotExist());
    }




    @WithMockUser(roles = "ADMIN")
    @Test
    public void testUpdate() throws Exception{

        PurchaseDetailDto body = new PurchaseDetailDto();
        body.setPurchaseId(1L);
        body.setProductId(2L);
        body.setQuantity(20);


        when(purchaseDetailService.update(any()))
                .thenReturn(details.get(0));




        mockMvc.perform(
                        put("/api/v1/purchasesDetails/4")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body))

                ).andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(1)
                ).andExpect(
                        jsonPath("$.quantity")
                                .value(40)
                );
    }
}
