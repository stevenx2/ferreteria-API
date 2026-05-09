package org.ferreteria.controller;


import org.ferreteria.entities.Client;
import org.ferreteria.services.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controlador de todo lo relacionado al cliente
 */
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }


    @GetMapping
    public ResponseEntity<List<Client>> findAll(){
        return ResponseEntity.ok(clientService.findAllWithSales());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable("id") Long id){
        Client client = clientService.findClientWithSales(id);
        return ResponseEntity.ok(client);
    }
}
