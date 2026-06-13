package org.ferreteria.services;


import org.ferreteria.dto.SaleDetailDto;
import org.ferreteria.entities.SaleDetail;
import org.ferreteria.problem.ResourceNotFound;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Sql({"classpath:/testcontainers/drop-scheme.sql", "classpath:/testcontainers/create-scheme.sql"})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@SpringJUnitConfig({ServicesCfg.class})
public class SaleDetailServiceTest {


    @Autowired
    private SaleDetailService saleDetailService;



    @Test
    public void testFindAll(){
        List<SaleDetail> details = saleDetailService.findAll();
        assertNotNull(details);
        assertEquals(6,details.size());


        // la lista devuelve los detalles ordenadamente de forma ascendente por su cantidad, por lo cual, el último registro debe tener la mayor cantidad
        SaleDetail greaterQuantity = details.get(5);
        assertEquals(5,greaterQuantity.getQuantity());
        assertEquals("Destornillador",greaterQuantity.getProduct().getName());
    }




    @Test
    public void testFindById(){
        SaleDetail detail = saleDetailService.findById(1L);
        assertNotNull(detail);
        assertEquals("Martillo",detail.getProduct().getName());
        assertEquals(LocalDate.of(2026,4,1),detail.getSale().getDate());
        assertEquals(2,detail.getQuantity());

        assertThrows(ResourceNotFound.class,()-> saleDetailService.findById(100L));
    }




    @Sql(
            value = "classpath:/testcontainers/delete-saleDetail.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    public void testSave(){
        SaleDetailDto detail = new SaleDetailDto();
        detail.setSaleId(2L);
        detail.setProductId(2L);
        detail.setQuantity(20);

        SaleDetail savedDetail = saleDetailService.save(detail);
        assertEquals(7,saleDetailService.findAll().size()); // ahora deben de existir 7 registros
        assertEquals("Taladro",savedDetail.getProduct().getName()); // verifico que el nombre del producto en efecto corresponde al correcto
        assertEquals(LocalDate.of(2026,4,2),savedDetail.getSale().getDate());
        assertEquals(20,savedDetail.getQuantity());


        // El método debe lanzar una excepción en caso que el producto o venta que se le quiera dar a este detalle no exista
        assertThrows(ResourceNotFound.class,()-> saleDetailService.save(new SaleDetailDto(7L,100L,2L,20)));
        assertThrows(ResourceNotFound.class,()-> saleDetailService.save(new SaleDetailDto(7L,2L,100L,20)));

    }


    @Sql(
            value = "classpath:/testcontainers/update-saleDetail.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    public void testUpdate(){
        // voy a actualizar el detalle con ID 1.
        SaleDetailDto detail = new SaleDetailDto();
        detail.setId(1L);
        detail.setSaleId(2L);
        detail.setProductId(2L);
        detail.setQuantity(20);

        SaleDetail updated = saleDetailService.update(detail);
        assertEquals("Taladro",updated.getProduct().getName());
        assertEquals(LocalDate.of(2026,4,2),updated.getSale().getDate());
        assertEquals(20,updated.getQuantity());



        // El método debe lanzar una excepción en caso que el detalle que se quiere editar no exista o en el caso que el producto o venta que se le quiera dar a este detalle no exista
        assertThrows(ResourceNotFound.class,()-> saleDetailService.update(new SaleDetailDto(100L,1L,2L,20)));
        assertThrows(ResourceNotFound.class,()-> saleDetailService.update(new SaleDetailDto(1L,100L,2L,20)));
        assertThrows(ResourceNotFound.class,()-> saleDetailService.update(new SaleDetailDto(1L,2L,100L,20)));
    }
}
