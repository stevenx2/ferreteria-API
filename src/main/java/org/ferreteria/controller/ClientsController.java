package org.ferreteria.controller;


import jakarta.validation.Valid;
import org.ferreteria.dto.ClientDto;
import org.ferreteria.entities.Client;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.ferreteria.utils.JsonCreator.createFieldsErrorJson;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * controlador de todo lo relacionado al cliente
 */
@RestController
@RequestMapping("/api/v1/clients")
public class ClientsController {

    private final ClientService clientService;


    @Autowired
    private MessageSource messageSource;

    public ClientsController(ClientService clientService) {
        this.clientService = clientService;
    }


    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(name = "name", required = false) String name) {

        if (name != null) {
            return ResponseEntity.ok(clientService.findByName(name));
        }
        return ResponseEntity.ok(clientService.findAllWithSales());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable("id") Long id) {
        Client client = clientService.findClientWithSales(id);
        return ResponseEntity.ok(client);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Client> deleteById(@PathVariable("id") Long id) {
        Client deletedClient = clientService.deleteById(id);
        return ResponseEntity.ok(deletedClient);
    }


    /**
     * Actualizar un cliente
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClientById(
            @PathVariable("id") Long id,
            @Valid @RequestBody ClientDto clientDto,
            BindingResult bindingResult

    ) {

        // Si el id del cliente no existe lanzo una excepción con el idioma del usuario. Este tipo de excepción es atrapada por el GlobalExcepción handler, el cual devuelve una respuesta del error en formato json
        if (!clientService.existsById(id)) {
            throw new ResourceNotFound(
                    messageSource.getMessage(
                            "error.client.notFound",
                            new Object[]{id},
                            LocaleContextHolder.getLocale()
                    )
            );
        }


        /**
         * Si un campo del cuerpo no cumple con las especificaciones:
         *  - nombre de más de 2 caracteres y menos de 41
         *  - teléfono de 10 dígitos
         *  - dirección no mayor a 100 caracteres
         *  entonces devuelvo la respuesta con código de estado 400 (bad request) y un json que tiene como claves el campo y como valores el tipo del error.
         */
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(createFieldsErrorJson(bindingResult.getFieldErrors()));
        }


        clientDto.setId(id);
        clientService.update(clientDto);
        return ResponseEntity.noContent().build();

    }


    @PostMapping
    public ResponseEntity<?> saveClient(
            @Valid @RequestBody ClientDto clientDto,
            BindingResult bindingResult
    ) {


        /**
         * Si un campo del cuerpo no cumple con las especificaciones:
         *  - nombre de más de 2 caracteres y menos de 41
         *  - teléfono de 10 dígitos
         *  - dirección no mayor a 100 caracteres
         *  entonces devuelvo la respuesta con código de estado 400 (bad request) y un json que tiene como claves el campo y como valores el tipo del error.
         */
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(createFieldsErrorJson(bindingResult.getFieldErrors()));
        }

        Client savedClient = clientService.save(clientDto);

        // url de donde se guardó el recurso por si se quiere acceder de nuevo a el. Se envía en el header de la respuesta
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedClient.getId())
                .toUri();

        return ResponseEntity.created(uri).body(savedClient);

    }


}
