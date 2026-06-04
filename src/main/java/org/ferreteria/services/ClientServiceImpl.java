package org.ferreteria.services;

import org.ferreteria.dto.ClientDto;
import org.ferreteria.entities.Client;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.repositories.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;
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
    public Client save(ClientDto client) {
        Client clientToSave = new Client();
        clientToSave.setName(client.getName());
        clientToSave.setAddress(client.getAddress());
        clientToSave.setPhoneNumber(client.getPhoneNumber());

        return repo.save(clientToSave);
    }

    @Override
    public void update(ClientDto client) {
        Client clientToSave = repo.findById(client.getId()).orElse(null);

        clientToSave = Stream.of(clientToSave)
                .map(c -> {
                    c.setName(client.getName());
                    c.setAddress(client.getAddress());
                    c.setPhoneNumber(client.getPhoneNumber());
                    return c;
                }).findFirst().orElse(null);

        repo.save(clientToSave);
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


    /**
     * Busca un cliente por su nombre, este no necesariamente debe ser exacto, con solo contener una parte
     * del nombre cuenta. ej: si buscas joh automáticamente te devuelve los clientes que lleven joh en su nombre.
     * @param name nombre del cliente
     * @return lista con los clientes que tiene ese nombre o parte de el.
     */
    @Transactional(readOnly = true)
    @Override
    public List<Client> findByName(String name) {
        List<Client> clients = StreamSupport.stream(
                repo.findByNameLike(name).spliterator(),
                false
        ).toList();

        if(clients.isEmpty()){
            throw new ResourceNotFound(
                    messageSource.getMessage(
                            "error.client.notFound.byName",
                            new Object[]{name},
                            "por defecto",
                            LocaleContextHolder.getLocale()
                    )
            );
        }

        return clients;
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

    @Override
    public boolean existsById(Long id) {
        return repo.existsById(id);
    }


}
