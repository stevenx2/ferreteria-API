package org.ferreteria.repositories;

import org.ferreteria.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepo extends JpaRepository<Client,Long> {


    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.sales WHERE c.name=:name")
    Iterable<Client> findByName(@Param("name") String name);


    /**
     * Buscar clientes que contengan ese string en su nombre, no tiene por qué tener toda la cadena tal cual.
     * @param name nombre del cliente
     * @return lista de clientes que contengan dicho match
     */
    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.sales WHERE c.name LIKE %?1%")
    Iterable<Client> findByNameLike(String name);

    /**
     * Por defecto hibernate no carga listas de una entidad. Este metodo lo permite
     */
    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.sales WHERE c.id =:client_id")
    Optional<Client> findClientWithSales(@Param("client_id") Long id);


    @Query("SELECT c FROM Client c LEFT JOIN FETCH c.sales")
    Iterable<Client> findAllWithSales();

}
