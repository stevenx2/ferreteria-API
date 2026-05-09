package org.ferreteria.services;

import org.ferreteria.entities.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {


    List<Client> findAll();
    List<Client> findAllWithSales();

    Client findClientWithSales(Long id);

    Client save(Client client);

    Client findById(Long id);

    List<Client> findByName(String name);


    void deleteById(Long id);
}
