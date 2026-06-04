package org.ferreteria.services;

import org.ferreteria.dto.ClientDto;
import org.ferreteria.entities.Client;

import java.util.List;

public interface ClientService {


    List<Client> findAll();
    List<Client> findAllWithSales();

    Client findClientWithSales(Long id);

    Client save(ClientDto client);

    void update(ClientDto client);

    Client findById(Long id);

    List<Client> findByName(String name);


    Client deleteById(Long id);

    boolean existsById(Long id);

}
