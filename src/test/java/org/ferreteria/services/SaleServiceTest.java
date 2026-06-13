package org.ferreteria.services;


import org.ferreteria.dto.SaleDto;
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
public class SaleServiceTest {

    @Autowired
    private SaleService saleService;




    @Test
    public void testFindAll(){

        List<SaleDto> sales = saleService.findAll();
        assertNotNull(sales);
        assertEquals(6,sales.size());
        sales.forEach(System.out::println);
    }



    @Test
    public void testFindById(){
        SaleDto sale = saleService.findById(1L);
        assertNotNull(sale);
        assertEquals(1L,sale.getClientId());
        assertEquals(LocalDate.of(2026,4,1),sale.getDate());
    }


    @Sql(
            value = "classpath:/testcontainers/delete-sale.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    public void testSave(){

        SaleDto sale = new SaleDto();
        sale.setClientId(1L);
        sale.setDate(LocalDate.of(2026,6,10));

        SaleDto saved = saleService.save(sale);
        assertEquals(1L,saved.getClientId());
        assertEquals(LocalDate.of(2026,6,10),saved.getDate());

        assertEquals(7,saleService.findAll().size()); // ahora deben existir 7 registros

        // Lanza error en caso de que el cliente no exista
        assertThrows(ResourceNotFound.class,()-> saleService.save(new SaleDto(1L,LocalDate.now(),100L)));

    }


    /**
     * Luego del test se va a ejecutar el script de la anotación @Sql, dejando el registro como estaba antes de ser actualizado
     */
    @Sql(
            value = "classpath:/testcontainers/update-sale.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    public void testUpdate(){
        SaleDto sale = new SaleDto();
        sale.setId(1L); // voy a actualizar el registro 1
        sale.setClientId(2L);
        sale.setDate(LocalDate.of(2026,6,10));

        // primero verifico sus datos antes de ser actualizado
        SaleDto beforeUpdate = saleService.findById(1L);
        assertEquals(1L,beforeUpdate.getClientId());
        assertEquals(LocalDate.of(2026,4,1),beforeUpdate.getDate());


        // ahora veo sus nuevos datos luego de ser actualizado
        SaleDto afterUpdate = saleService.update(sale);
        assertEquals(2L,afterUpdate.getClientId());
        assertEquals(LocalDate.of(2026,6,10),afterUpdate.getDate());

        // Lanza error tanto en el caso de que la venta que se quiera actualizar no exista o que el cliente no exista
        assertThrows(ResourceNotFound.class,()-> saleService.update(new SaleDto(100L,LocalDate.now(),1L)));
        assertThrows(ResourceNotFound.class,()-> saleService.update(new SaleDto(1L,LocalDate.now(),100L)));

    }
}
