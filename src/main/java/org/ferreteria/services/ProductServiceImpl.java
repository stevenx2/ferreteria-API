package org.ferreteria.services;


import org.ferreteria.dto.ProductDto;
import org.ferreteria.entities.Product;
import org.ferreteria.entities.Supplier;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.repositories.ProductRepo;
import org.ferreteria.repositories.SupplierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    private final SupplierRepo supplierRepo;


    @Autowired
    private MessageSource messageSource;

    public ProductServiceImpl(ProductRepo productRepo,SupplierRepo supplierRepo) {
        this.productRepo = productRepo;
        this.supplierRepo = supplierRepo;
    }

    /**
     * Obtengo todos los productos y realizo su correspondiente mapeo para la clase ClientDto
     */
    @Override
    public List<ProductDto> findAll() {
        return productRepo.findAll().stream().map(
                this::mapToProductDto
        ).toList();
    }


    /**
     * Busca por match de nombre del producto, si se pasa la cadena "Ques" se retorna productos que contienen esa cadena en el nombre.
     *
     * @param name Es la cadena de coincidencia del nombre
     * @return lista con los productos que contengan esa coincidencia en el nombre.
     */
    @Transactional(readOnly = true)
    @Override
    public List<ProductDto> findByNameLike(String name) {
        List<ProductDto> products = StreamSupport.stream(
                productRepo.findByNameLike(name).spliterator(),
                false
        ).map(this::mapToProductDto).toList();


        if (products.isEmpty()) {
            throw new ResourceNotFound(
                    messageSource.getMessage(
                            "product.name.search.empty",
                            new Object[]{name},
                            LocaleContextHolder.getLocale()
                    )
            );
        }

        return products;
    }


    @Transactional(readOnly = true)
    @Override
    public ProductDto findById(Long id) {
        return productRepo.findById(id)
                .map(this::mapToProductDto)
                .orElseThrow(() -> new ResourceNotFound(
                        messageSource.getMessage(
                                "product.search.not_found_by_id",
                                new Object[]{id},
                                LocaleContextHolder.getLocale()
                        )
                ));
    }


    @Override
    public ProductDto deleteById(Long id) {
        ProductDto deletedProduct = productRepo.findById(id).map(this::mapToProductDto).orElseThrow(
                () -> new ResourceNotFound(
                        messageSource.getMessage(
                                "product.search.not_found_by_id",
                                new Object[]{id},
                                LocaleContextHolder.getLocale()
                        ))
        );

        productRepo.deleteById(id);
        return deletedProduct;
    }


    /**
     * Guardo un producto con el proveedor correspondiente al ID pasado en el atributo supplierId del objeto ProductDto
     */
    @Override
    public ProductDto save(ProductDto product) {

        // mapeo el objeto DTO al de la base de datos para luego guardarlo en la base de datos
        Product mapped = Stream.of(product)
                .map(this::mapToEntityClass)
                .findFirst().get();


        Supplier supplier = supplierRepo.findById(product.getSupplierId()).orElseThrow( // obtengo el proveedor
                () -> new ResourceNotFound(
                        messageSource.getMessage(
                                "supplier.search.not_found_by_id",
                                new Object[]{product.getSupplierId()},
                                LocaleContextHolder.getLocale()
                        )
                ));


        // Agrego el nuevo producto al proveedor.
        supplier.addProduct(mapped);
        Product saved = productRepo.save(mapped);

        return  this.mapToProductDto(saved);
    }


    /**
     * Actualizar un producto de la base de datos. Este método además permite cambiar el proveedor del producto, por lo que se recomienda verificar el ID del proveedor que se proporciona
     * @param product objeto que representa el registro que se va a actualizar. Esta clase tiene campos como id (id del producto que se va a actualizar) y supplierId (id del nuevo proveedor del producto, si no se quiere modificar esto se debe pasar el ID anterior)
     * @return el producto que se insertó. Esto se usa para dar una respuesta tipo json en el controlador
     */
    @Override
    public ProductDto update(ProductDto product) {

        // 1. Obtener el producto actual de la base de datos
        Product fromDb = productRepo.findById(product.getId()).orElseThrow(
                () -> new ResourceNotFound(
                        messageSource.getMessage(
                                "product.search.not_found_by_id",
                                new Object[]{product.getId()},
                                LocaleContextHolder.getLocale()
                        ))
        );

        // 2. Obtener el NUEVO proveedor indicado en el DTO
        Supplier newSupplier = supplierRepo.findById(product.getSupplierId()).orElseThrow(
                () -> new ResourceNotFound(
                        messageSource.getMessage(
                                "supplier.search.not_found_by_id",
                                new Object[]{product.getSupplierId()},
                                LocaleContextHolder.getLocale()
                        )
                ));

        // 3. Verificar si el proveedor realmente cambió
        Supplier oldSupplier = fromDb.getSupplier();
        if (!Objects.equals(newSupplier.getId(), oldSupplier.getId())) {
            // Removemos el producto del proveedor VIEJO (en memoria)
            oldSupplier.removeProduct(fromDb);

            // Vinculamos el producto al NUEVO proveedor
            newSupplier.addProduct(fromDb);
        } else {
            // Si es el mismo proveedor, solo nos aseguramos de mantener seteada la relación
            // no es necesario, pero lo dejo por si acaso.
            fromDb.setSupplier(newSupplier);
        }

        // 4. Actualizar los campos propios del producto
        fromDb.setPrice(product.getPrice());
        fromDb.setStock(product.getStock());
        fromDb.setName(product.getName());

        // 5. Guardar los cambios.
        productRepo.save(fromDb);

        // 6. Mapear y retornar el DTO
        return this.mapToProductDto(fromDb);
    }

    @Override
    public boolean existsById(Long id) {
        return productRepo.existsById(id);
    }

    /**
     * Obtener el total de productos que tiene un proveedor
     * @param supplierId identificador de proveedor en la base de datos
     * @return un Long que representa el total de productos del proveedor
     */
    @Override
    public long countBySupplierId(Long supplierId) {
        return productRepo.countBySupplierId(supplierId);
    }
}
