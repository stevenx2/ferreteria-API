package org.ferreteria.repositories;

import org.ferreteria.entities.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepo extends JpaRepository<Supplier,Long> {

    @Query("SELECT s FROM Supplier s LEFT JOIN FETCH s.products WHERE s.id =:id")
    Iterable<Supplier> findByIdWithProducts(@Param("id") Long id);


}
