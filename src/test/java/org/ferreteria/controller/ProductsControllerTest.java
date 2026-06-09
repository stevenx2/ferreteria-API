package org.ferreteria.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.ferreteria.dto.ProductDto;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.security.SecurityCfg;
import org.ferreteria.services.ProductService;
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

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringJUnitWebConfig(classes = {MockConfig.class})
public class ProductsControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ProductService service;

    @Autowired
    private WebApplicationContext ctx;


    private List<ProductDto> products;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
                .apply(springSecurity())
                .build();


        products = List.of(
                new ProductDto(1L, "Queso", 30000, 30,1L),
                new ProductDto(2L, "libra arroz", 2400, 40,2L),
                new ProductDto(3L, "Pan", 2000, 50,3L),
                new ProductDto(4L, "Frijol", 1400, 60,4L),
                new ProductDto(5L, "Lentajeas", 1000, 70,5L)
        );

    }


    @WithMockUser
    @Test
    public void testFindAll() throws Exception {

        when(service.findAll())
                .thenReturn(products);


        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.length()")
                                .value(5)

                );


    }


    @WithMockUser
    @Test
    public void testFindByParamName() throws Exception {

        when(service.findByNameLike(anyString()))
                .thenReturn(List.of(products.get(0)));


        mockMvc.perform(
                        get("/api/v1/products")
                                .param("name","Que")
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$[0].name")
                                .value("Queso")
                );
    }



    @WithMockUser
    @Test
    public void testFindById() throws Exception{

        when(service.findById(anyLong()))
                .thenReturn(new ProductDto(100L,"Queso",30000,100,1L));



        mockMvc.perform(get("/api/v1/products/100"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(100L)
                ).andExpect(
                        jsonPath("$.name")
                                .value("Queso")
                );
    }




    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void testDelete() throws Exception{

        when(service.deleteById(anyLong()))
                .thenReturn(new ProductDto(1L,"Queso",20000,100,1L));

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.name")
                                .value("Queso")
                )
                .andExpect(
                        jsonPath("$.price")
                                .value(20000)
                );

    }




    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void testDeleteProductNotFound() throws Exception{

        when(service.deleteById(anyLong()))
                .thenThrow(new ResourceNotFound("producto no encontrado"));

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isNotFound())
                .andExpect(
                        jsonPath("$.message")
                                .value("producto no encontrado")
                )
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                );
    }



    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void testSaveProduct() throws Exception{

        ProductDto body = new ProductDto();
        body.setName("Queso");
        body.setPrice(30000);
        body.setStock(100);
        body.setSupplierId(1L);

        when(service.save(any(ProductDto.class)))
                .thenReturn( new ProductDto(100L,"Queso",30000,100,1L));



        mockMvc.perform(
                post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body))
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.name")
                                .value("Queso")
                )
                .andExpect(header().string("Location", containsString("/api/v1/products/100")));

    }


    /**
     * Test cuando hay errores en validar el cuerpo que se le pasa a la petición para guardar el registro
     */
    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void testSaveProductDtoValidationError() throws Exception{

        ProductDto body = new ProductDto();
        //todo los valores de los campos darán y marcarán la petición como bad request
        body.setName("Q"); // error de nombre menor a 3 caracteres
        body.setPrice(30); // precio menor a 2000
        body.setStock(12); //Este stock en válido, lo dejo así para luego validar que en la respuesta json este campo no existe. Para que este campo sea inválido tiene que ser un valor negativo
        body.setSupplierId(1L);

        when(service.save(any(ProductDto.class)))
                .thenReturn( new ProductDto(100L,"Queso",30000,100,1L));



        mockMvc.perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body))
                )
                .andExpect(status().isBadRequest())
                /**
                 * Verifico que se creó un json con las claves de los campos que dieron error
                 */
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.stock").doesNotExist()) // este campo fue válido, por ello su clave en la respuesta json no existe
                .andExpect(jsonPath("$.price").exists());


    }



    @WithMockUser(roles = {"ADMIN"})
    @Test
    public void testUpdateProduct() throws Exception{

        ProductDto body = new ProductDto(100L,"Snacks",20000,200,1L);

        when(service.update(any(ProductDto.class)))
                .thenReturn(body);

        body.setId(null); // lo convierto en nulo porque si paso un valor en el cuerpo de la petición me lanza un error (el ID debe ser nulo)

        mockMvc.perform(
                        put("/api/v1/products/100")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body))
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.name")
                                .value("Snacks")
                );
    }






    @Configuration
    static class MockConfig {


        @Bean
        ProductService productService() {
            return Mockito.mock(ProductService.class);
        }
    }
}
