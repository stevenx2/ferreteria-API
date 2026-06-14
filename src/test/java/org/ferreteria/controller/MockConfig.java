package org.ferreteria.controller;

import org.ferreteria.security.SecurityCfg;
import org.ferreteria.services.*;
import org.ferreteria.web.WebConfig;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Contiene todas las configuraciones necesarias para los tests de los controladores, incluido las clases mocks de los servicios
 */
@Import({WebConfig.class, SecurityCfg.class})
@Configuration
public class MockConfig {

    @Bean
    ProductService productService() {
        return Mockito.mock(ProductService.class);
    }


    @Bean
    public ClientService clientService() {
        return Mockito.mock(ClientService.class);
    }

    @Bean
    public SupplierService supplierService(){return Mockito.mock(SupplierService.class);}

    @Bean
    public PurchaseService purchaseService(){return Mockito.mock(PurchaseService.class);};

    @Bean
    public PurchaseDetailService purchaseDetailService(){return Mockito.mock(PurchaseDetailService.class);};

    @Bean
    public SaleService saleService(){return Mockito.mock(SaleService.class);};

    @Bean
    public SaleDetailService saleDetailService(){return Mockito.mock(SaleDetailService.class);}
}
