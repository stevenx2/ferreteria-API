package org.ferreteria.services;


import org.ferreteria.dto.PurchaseDetailDto;
import org.ferreteria.problem.ResourceNotFound;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"classpath:/testcontainers/drop-scheme.sql", "classpath:/testcontainers/create-scheme.sql"})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@SpringJUnitConfig(ServicesCfg.class)
public class PurchaseDetailServiceTest {


    @Autowired
    private PurchaseDetailService purchaseDetailService;


    @Test
    public void testFindAll() {
        List<PurchaseDetailDto> details = purchaseDetailService.findAll();
        assertNotNull(details);
        assertEquals(6, details.size());
        details.forEach(System.out::println);
    }


    @Test
    public void testFindById() {
        PurchaseDetailDto detail = purchaseDetailService.findById(1L);
        assertNotNull(detail);
        assertEquals(1, detail.getPurchaseId());
        assertEquals(1, detail.getProductId());
        assertEquals(20, detail.getQuantity());

        assertThrows(ResourceNotFound.class, () -> purchaseDetailService.findById(100L));
    }





    @Sql(
            value = "classpath:/testcontainers/delete-purchaseDetail.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    public void testSave(){

        // se empieza con 6 registros
        assertEquals(6,purchaseDetailService.findAll().size());

        PurchaseDetailDto detail = new PurchaseDetailDto();
        detail.setPurchaseId(1L);
        detail.setQuantity(200);
        detail.setProductId(1L);

        PurchaseDetailDto saved = purchaseDetailService.save(detail);
        assertEquals(1L,saved.getPurchaseId());
        assertEquals(1L,saved.getProductId());
        assertEquals(200,detail.getQuantity());

        // ahora hay 7 registros
        assertEquals(7,purchaseDetailService.findAll().size());

        // Lanza excepción cuando no existe la compra que se le quiere asignar a este detalle
        assertThrows(ResourceNotFound.class,()-> purchaseDetailService.save(new PurchaseDetailDto(7L,100L,1L,200)));

        // Lanza excepción cuando no existe el producto que se le quiere asignar a este detalle
        assertThrows(ResourceNotFound.class,()-> purchaseDetailService.save(new PurchaseDetailDto(7L,1L,100L,200)));

    }




    @Sql(
            value = "classpath:/testcontainers/update-purchaseDetail.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    public void testUpdate(){

        // Verifico los datos antes de actualizarlos. Este detalle pertenece al producto con ID 1 y se compraron 20 unidades
        PurchaseDetailDto beforeUpdate = purchaseDetailService.findById(1L);
        assertEquals(1L,beforeUpdate.getProductId());
        assertEquals(20,beforeUpdate.getQuantity());
        assertEquals(1L,beforeUpdate.getPurchaseId()); // este campo no se actualizará


        // Voy a actualizar ese detalle cambiando al producto con ID 2 con una cantidad de 20 unidades
        PurchaseDetailDto detail = new PurchaseDetailDto(1L, 1L, 2L, 10);

        PurchaseDetailDto afterUpdate = purchaseDetailService.update(detail);
        assertNotNull(afterUpdate);
        assertEquals(2L,afterUpdate.getProductId());
        assertEquals(10,afterUpdate.getQuantity());
        assertEquals(1L,afterUpdate.getPurchaseId()); // este campo sigue igual


        // Lanza excepción cuando no existe el detalle de compra que se quiere actualizar
        assertThrows(ResourceNotFound.class,()-> purchaseDetailService.update(new PurchaseDetailDto(100L,1L,1L,200)));

        // Lanza excepción cuando no existe la compra que se le quiere asignar a este detalle
        assertThrows(ResourceNotFound.class,()-> purchaseDetailService.update(new PurchaseDetailDto(1L,100L,1L,200)));

        // Lanza excepción cuando no existe el producto que se le quiere asignar a este detalle
        assertThrows(ResourceNotFound.class,()-> purchaseDetailService.update(new PurchaseDetailDto(1L,1L,100L,200)));

    }


    @Sql(
            value = "classpath:/testcontainers/insert-purchaseDetail.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Test
    public void testDelete(){

        // Verifico que el detalle que quiero borrar realmente existe (se insertó mediante la anotación @Sql)
        PurchaseDetailDto detail = purchaseDetailService.findById(7L);
        assertNotNull(detail);
        assertEquals(200,detail.getQuantity());
        assertEquals(7,purchaseDetailService.findAll().size()); // Deben existir 7 registros antes de eliminar uno

        // Lo elimino y verifico que ya no existe
        purchaseDetailService.deleteById(7L);
        assertThrows(ResourceNotFound.class,()-> purchaseDetailService.deleteById(7L));
        assertEquals(6,purchaseDetailService.findAll().size());

    }
}
