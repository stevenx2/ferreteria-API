package org.ferreteria.services;

import org.ferreteria.dto.ProductDto;
import org.ferreteria.entities.Product;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductService {

    /**
     * las implementaciones usan este método para devolver un objeto dto y no la clase entidad
     */
    default ProductDto mapToProductDto(Product product){
        return new ProductDto(product.getId(),product.getName(),product.getPrice(),product.getStock(), product.getSupplier().getId());
    }

    /**
     * Mapeo el objeto de tranferencia de datos al objeto de la base de datos (Product). Esto para evitar la redundancia en los diferentes métodos que implementen esta interfaz
     * @param product objeto que se quiere mapear
     * @return el objeto entidad el cual jpa puede usar para guardar un nuevo registro.
     */
    default Product mapToEntityClass(ProductDto product){
        Product mapped = new Product();
        mapped.setName(product.getName());
        mapped.setStock(product.getStock());
        mapped.setPrice(product.getPrice());
        return mapped;
    }

    List<ProductDto> findAll();

    List<ProductDto> findByNameLike(String name);

    ProductDto findById(Long id);

    ProductDto deleteById(Long id);

    ProductDto save(ProductDto product);

    ProductDto update(ProductDto product);

    boolean existsById(Long id);

    long countBySupplierId(Long supplierId);

}
