package org.ferreteria.services;


import org.ferreteria.AppConfig;
import org.ferreteria.TransactionCfg;
import org.ferreteria.dto.PurchaseDto;
import org.ferreteria.dto.SupplierDto;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.repositories.SupplierRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"classpath:/testcontainers/drop-scheme.sql", "classpath:/testcontainers/create-scheme.sql"})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@SpringJUnitConfig({ServicesCfg.class})
public class PurchaseServiceTest {


    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private SupplierService supplierService;



    @Test
    public void testFindAll(){
        List<PurchaseDto> purchases = purchaseService.findAll();

        assertNotNull(purchases);
        assertEquals(6,purchases.size());
        purchases.forEach(System.out::println);
    }



    @Test
    public void testFindById(){
        PurchaseDto purchase = purchaseService.findById(1L);

        assertNotNull(purchase);
        assertEquals(1,purchase.getSupplierId());
        assertEquals(LocalDate.of(2026,3,1),purchase.getDate());

        // lanza excepción cuando no se encuentra la compra
        assertThrows(ResourceNotFound.class,() -> purchaseService.findById(100L));
    }






    @Sql(
            value = "classpath:/testcontainers/delete-purchase.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    public void testSave(){
        PurchaseDto purchase = new PurchaseDto();
        purchase.setDate(LocalDate.of(2025,7,21));
        purchase.setSupplierId(1L);

        PurchaseDto saved = purchaseService.save(purchase);
        assertNotNull(saved);
        assertEquals("Proveedor A", supplierService.findById(1L).getName());
        assertEquals(LocalDate.of(2025,7,21),saved.getDate());
        assertEquals(7,purchaseService.findAll().size());

        // Lanza excepción cuando no se puede encontrar un proveedor con ese ID
        assertThrows(ResourceNotFound.class,()-> {
            PurchaseDto purchase2 = new PurchaseDto();
            purchase2.setDate(LocalDate.of(2025,7,21));
            purchase2.setSupplierId(100L);
            purchaseService.save(purchase2);
        });


    }


    @Sql(
            value = "classpath:/testcontainers/update-purchase.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    public void testUpdate(){


        PurchaseDto beforeUpdate = purchaseService.findById(1L);
        assertEquals(1L,beforeUpdate.getSupplierId());


        PurchaseDto dto = new PurchaseDto();
        dto.setId(1L);
        dto.setSupplierId(2L);
        dto.setDate(LocalDate.of(2026,3,2));

        purchaseService.update(dto);
        PurchaseDto afterUpdate = purchaseService.findById(1L);
        assertEquals(2L,afterUpdate.getSupplierId());
        assertEquals(LocalDate.of(2026,3,2),afterUpdate.getDate());


        //Lanza excepción cuando no existe el proveedor
        assertThrows(ResourceNotFound.class,()-> purchaseService.update(new PurchaseDto(1L,100L,LocalDate.of(2026,6,21))));


        //Lanza excepción cuando no existe la compra
        assertThrows(ResourceNotFound.class,()-> purchaseService.update(new PurchaseDto(100L,1L,LocalDate.of(2026,6,21))));
    }



    @Sql(
            value = "classpath:/testcontainers/insert-purchase.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Test
    public void testDelete(){

        // Se empieza con 7 registros
        assertEquals(7,purchaseService.findAll().size());


        purchaseService.deleteById(7L);
        assertEquals(6,purchaseService.findAll().size());

        // Lanza excepción porque intento eliminar compra que no existe (borrada anteriormente)
        assertThrows(ResourceNotFound.class,()-> purchaseService.deleteById(7L));

    }






}
