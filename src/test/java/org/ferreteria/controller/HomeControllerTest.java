package org.ferreteria.controller;


import org.ferreteria.services.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test del homeController. Solo es para validar que la aplicación arranca.
 */
@ExtendWith(MockitoExtension.class)
public class HomeControllerTest {


    @Mock
    private HomeController controller;

    private MockMvc mockMvc;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    /**
     * Valida código 200 y cuerpo de la petición
     */
    @Test
    public void homeTest() throws Exception {

        when(controller.home())
                .thenReturn("<h1>Funciona<h1>");

        mockMvc.perform(
                        get("/api/v1")
                )
                .andExpect(status().isOk())
                .andExpectAll(content().string(containsString("Funciona")));

    }
}
