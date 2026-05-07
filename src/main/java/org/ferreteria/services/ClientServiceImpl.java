package org.ferreteria.services;

import org.ferreteria.entities.Client;
import org.ferreteria.repositories.ClientRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
}
