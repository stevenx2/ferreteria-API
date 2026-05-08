package org.ferreteria.services;

import org.ferreteria.entities.Client;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.repositories.ClientRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;


@Service
@Transactional
public class ClientServiceImpl implements ClientService{


    private final ClientRepo repo;

    public ClientServiceImpl(ClientRepo repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Client> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Client findClientWithSales(Long id) {
        return repo.findClientWithSales(id).orElse(null);
    }

    @Override
    public Client save(Client client) {
        return repo.save(client);
    }

    @Transactional(readOnly = true)
    @Override
    public Client findById(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFound("Client with id " + id + " couldn't be found. doesn't exit"));
    }


    @Transactional(readOnly = true)
    @Override
    public List<Client> findByName(String name) {
        return StreamSupport.stream(
                repo.findByName(name).spliterator(),
                false
        ).toList();
    }

    @Override
    public void deleteById(Long id) {
        if(!repo.existsById(id)){
            throw new ResourceNotFound("The client with id " + id + " that you want to delete doesn't exist");
        }

        repo.deleteById(id);
    }
}
