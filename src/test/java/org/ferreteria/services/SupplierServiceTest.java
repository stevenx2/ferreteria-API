package org.ferreteria.services;


import org.ferreteria.AppConfig;
import org.ferreteria.TransactionCfg;
import org.ferreteria.dto.SupplierDto;
import org.ferreteria.problem.ResourceNotFound;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"classpath:/testcontainers/drop-scheme.sql", "classpath:/testcontainers/create-scheme.sql"})
@SpringJUnitConfig(classes = {ServicesCfg.class})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public class SupplierServiceTest {

    @Autowired
    private SupplierService supplierService;




    @Test
    public void testFindById(){
        SupplierDto supplier = supplierService.findById(1L);
        assertNotNull(supplier);
        assertEquals("Proveedor A",supplier.getName());
        assertEquals("Bogota",supplier.getAddress());

        assertThrows(ResourceNotFound.class, () -> supplierService.findById(100L));
    }



    @Test
    public void testFindAll(){
        List<SupplierDto> suppliers = supplierService.findAll();
        assertNotNull(suppliers);
        assertEquals(6,suppliers.size());
        assertEquals("Barranquilla",suppliers.get(2).getAddress());
    }



    @Sql(
            value = "classpath:/testcontainers/delete-supplier.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    public void testSave(){

        SupplierDto supplier = new SupplierDto();
        supplier.setName("Nuevo proveedor");
        supplier.setAddress("Calle fejfejfe");
        supplier.setPhoneNumber("9999999999");

        SupplierDto saved = supplierService.save(supplier);
        assertEquals(7,saved.getId());
        assertEquals("Nuevo proveedor",saved.getName());
        assertEquals(7,supplierService.findAll().size());
    }


    @Test
    public void testFindByNameLike(){
        List<SupplierDto> suppliers = supplierService.findByNameLike("vee");
        assertNotNull(suppliers);
        assertEquals(6,suppliers.size());
    }



    @Sql(
            value = "classpath:/testcontainers/update-supplier.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    public void testUpdate(){

        SupplierDto old = supplierService.findById(1L);

        // Verifico el nombre de sus campos antes de actualizar
        assertEquals("Proveedor A",old.getName());
        assertEquals("3101111111",old.getPhoneNumber());
        assertEquals("Bogota",old.getAddress());

        // Solo voy a actualizar el nombre, pero debo usar el set a todos los valores, ya que no deben ser nulos como se puede ver en la clase SupplierDto. Esto evita que se lanze una excepción
        SupplierDto supplier = new SupplierDto();
        supplier.setId(1L); // voy a actualizar el proveedor con ID 1, por ello el objeto DTO debe tener ese ID
        supplier.setName("Proveedor editado");
        supplier.setPhoneNumber("3101111111");
        supplier.setAddress("Bogota");
        supplierService.update(supplier);


        // vuelvo a obtener el mismo proveedor y verifico que el nombre cambió
        SupplierDto updated = supplierService.findById(1L);
        assertEquals("Bogota",updated.getAddress());
        assertEquals("3101111111",updated.getPhoneNumber());
        assertEquals("Proveedor editado",updated.getName());

    }




    @Sql(
            value = "classpath:/testcontainers/insert-supplier.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Test
    public void testDelete(){
        SupplierDto supplier = supplierService.findByNameLike("Proveedor para tests").get(0);

        assertEquals(7,supplierService.findAll().size()); // debe haber 7 proveedores contando este nuevo insertado para el test

        supplierService.delete(supplier.getId());
        assertEquals(6,supplierService.findAll().size()); // quedan 6 registros eliminando este


        // si se intenta eliminar un proveedor que tiene productos se lanza una excepción.
        assertThrows(DataIntegrityViolationException.class,() -> supplierService.delete(1L));
    }


}
