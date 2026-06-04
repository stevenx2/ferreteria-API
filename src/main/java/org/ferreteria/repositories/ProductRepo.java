package org.ferreteria.repositories;

import org.ferreteria.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {

    @Query("SELECT p FROM Product p WHERE p.name LIKE CONCAT('%', :name, '%')")
    Iterable<Product> findByNameLike(@Param("name") String name);

    Iterable<Product> findByPriceGreaterThanEqual(Double price);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.supplier.id = :supplierId")
    long countBySupplierId(@Param("supplierId") Long supplierId);

}
