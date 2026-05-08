package org.ferreteria.repositories;

import org.ferreteria.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepo extends JpaRepository<Client,Long> {

    Iterable<Client> findByName(String name);

    /**
     * por defecto hibernate no carga listas de una entidad. este metodo lo permite
     */
    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.sales WHERE c.id =:client_id")
    Optional<Client> findClientWithSales(@Param("client_id") Long id);
}
