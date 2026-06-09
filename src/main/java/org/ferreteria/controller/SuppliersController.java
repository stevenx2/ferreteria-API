package org.ferreteria.controller;


import jakarta.validation.Valid;
import org.ferreteria.dto.SupplierDto;
import org.ferreteria.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.ferreteria.utils.JsonCreator.createFieldsErrorJson;


@RestController
@RequestMapping("/api/v1/suppliers")
public class SuppliersController {


    private final SupplierService supplierService;

    public SuppliersController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(name = "name",required = false) String name
    ){

        if(name != null){
            return ResponseEntity.ok(supplierService.findByNameLike(name));
        }

        return ResponseEntity.ok(supplierService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<SupplierDto> findById(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(supplierService.findById(id));
    }





    @PostMapping
    public ResponseEntity<?> saveSupplier(
            @Valid @RequestBody SupplierDto supplierDto,
            BindingResult bindingResult
    ){

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(createFieldsErrorJson(bindingResult.getFieldErrors()));
        }


        SupplierDto saved = supplierService.save(supplierDto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(uri).body(saved);
    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateSupplier(
            @Valid @RequestBody SupplierDto supplierDto,
            BindingResult bindingResult,
            @PathVariable("id") Long id
    ){

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(createFieldsErrorJson(bindingResult.getFieldErrors()));
        }


        supplierDto.setId(id);
        SupplierDto updated = supplierService.update(supplierDto);
        return ResponseEntity.ok(updated);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSupplier(
            @PathVariable("id") Long id
    ){
        supplierService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
