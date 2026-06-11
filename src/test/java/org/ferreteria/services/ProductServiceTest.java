package org.ferreteria.services;

import org.ferreteria.AppConfig;
import org.ferreteria.TransactionCfg;
import org.ferreteria.dto.ProductDto;
import org.ferreteria.dto.SupplierDto;
import org.ferreteria.problem.ResourceNotFound;
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
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;

import javax.sql.DataSource;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Todos los tests relacionados con la clase ProductServiceImpl
 */
@Sql({"classpath:/testcontainers/drop-scheme.sql", "classpath:/testcontainers/create-scheme.sql"})
@SpringJUnitConfig({ServicesCfg.class})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
public class ProductServiceTest {


    @Autowired
    private ProductService productService;



    @Test
    public void testFindAll(){
        List<ProductDto> products = productService.findAll();
        assertNotNull(products);
        assertEquals(6,products.size());
    }


    @Test
    public void testFindByNameLIke(){
        List<ProductDto> products = productService.findByNameLike("ll"); // busca productos que contengan "ll" en el nombre
        assertNotNull(products);
        assertEquals(2,products.size());

        products.forEach(p ->{
            assertThat(p.getName(),anyOf(is("Martillo"),is("Destornillador"))); // valido que los productos encontrados sean los que cumplen la coincidencia de nombre
        });


        //lanza error cuando no encuentra productos con esa coincidencia en el nombre.
        assertThrows(ResourceNotFound.class,() -> productService.findByNameLike("dffsfs"));
    }



    @Test
    public void testFindById(){
        // cuando el producto existe
        ProductDto product = productService.findById(1L);
        assertNotNull(product);
        assertEquals("Martillo",product.getName());
        assertEquals(50,product.getStock());

        //cuando el producto no existe verifico que se lanze una excepción
        assertThrows(ResourceNotFound.class,() -> productService.findById(100L));


    }


    @Sql(
            value = "classpath:/testcontainers/update-product.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    public void testUpdate(){

        ProductDto product = new ProductDto(1L, "Martillo actualizado", 30000, 100, 2L);
        productService.update(product);

        assertEquals("Martillo actualizado", productService.findById(1L).getName());

        /**
         * Valido que efectivamente ahora el proveedor 2 ahora tiene 2 productos (por el que fue agregado) y el
         * proveedor 1 tiene 0 (por el que se le fue quitado para darlo al proveedor 2)
         */
        assertEquals(2,productService.countBySupplierId(2L));
        assertEquals(0,productService.countBySupplierId(1L));


    }


    /**
     * Test para validar que el método "update" lanza una excepción en el caso que el id del producto o del proveedor no existan en la base de datos
     */
    @Test
    public void testUpdateThrowsException(){
        ProductDto product = new ProductDto(100L, "Martillo actualizado", 30000, 100, 2L);
        assertThrows(ResourceNotFound.class,() -> productService.update(product));

        ProductDto product2 = new ProductDto(1L, "Martillo actualizado", 30000, 100, 200L);
        assertThrows(ResourceNotFound.class,()->  productService.update(product2));
    }



    @Sql(
            value = "classpath:/testcontainers/delete-product.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @Test
    public void testSave(){
        ProductDto product = new ProductDto();
        product.setName("Nuevo producto para tests");
        product.setPrice(40000);
        product.setStock(300);
        product.setSupplierId(1L);

        assertEquals(1,productService.countBySupplierId(1L)); // al principio el proveedor con ID 1 debe tener un solo producto
        productService.save(product);
        ProductDto savedProduct = productService.findByNameLike("Nuevo producto para tests").get(0); // obtengo el objeto que acabo de guardar mediante su nombre
        assertEquals(40000,savedProduct.getPrice());
        assertEquals(300,savedProduct.getStock());
        assertEquals(2,productService.countBySupplierId(1L)); // Valido que el proveedor ahora tiene 2 productos contando este nuevo
        assertEquals(7,productService.findAll().size()); // ahora existen 7 productos contando el nuevo
    }

}
