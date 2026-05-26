package org.ferreteria.services;

import org.ferreteria.entities.Client;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.repositories.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * servicio con métodos para obtener, editar y eliminar clientes
 */
@Service
@Transactional
public class ClientServiceImpl implements ClientService {


    private final ClientRepo repo;

    @Autowired
    private MessageSource messageSource;


    public ClientServiceImpl(ClientRepo repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    @Override
    public List<Client> findAll() {
        return repo.findAll();
    }

    @Override
    public List<Client> findAllWithSales() {
        return StreamSupport.stream(
                repo.findAllWithSales().spliterator(),
                false
        ).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Client findClientWithSales(Long id) {
        return repo.findClientWithSales(id).orElseThrow(() -> new ResourceNotFound(
                messageSource.getMessage(
                        "error.client.notFound",
                        new Object[]{id},
                        LocaleContextHolder.getLocale()
                )
        ));
    }

    @Override
    public Client save(Client client) {
        return repo.save(client);
    }

    @Transactional(readOnly = true)
    @Override
    public Client findById(Long id) {
        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFound(
                        messageSource.getMessage(
                                "error.client.notFound",
                                new Object[]{id},
                                LocaleContextHolder.getLocale()
                        )
                ));
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
    public Client deleteById(Long id) {

        Client client = repo.findById(id).orElseThrow(() -> new ResourceNotFound(
                messageSource.getMessage(
                        "error.client.toDelete.notFound",
                        new Object[]{id},
                        LocaleContextHolder.getLocale()
                )
        ));

        repo.deleteById(id);
        return  client;
    }
}
