package org.ferreteria.controller;


import jakarta.validation.Valid;
import org.ferreteria.dto.PurchaseDto;
import org.ferreteria.services.PurchaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.ferreteria.utils.JsonCreator.createFieldsErrorJson;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/purchases")
public class PurchasesController {



    private final PurchaseService purchaseService;

    public PurchasesController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }


    @GetMapping
    public ResponseEntity<List<PurchaseDto>> findAll(){
        return ResponseEntity.ok(purchaseService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDto> findById(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(purchaseService.findById(id));
    }




    @PostMapping
    public ResponseEntity<?> savePurchase(
            @Valid @RequestBody PurchaseDto purchaseDto,
            BindingResult bindingResult
    ){

        if(bindingResult.hasErrors()){
            return  ResponseEntity.badRequest().body(createFieldsErrorJson(bindingResult.getFieldErrors()));
        }


        PurchaseDto saved = purchaseService.save(purchaseDto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity.created(uri).body(saved);
    }





    @PutMapping("/{id}")
    public ResponseEntity<?> updatePurchase(
            @Valid @RequestBody PurchaseDto purchaseDto,
            BindingResult bindingResult,
            @PathVariable("id") Long id
    ){

        if(bindingResult.hasErrors()){
            return  ResponseEntity.badRequest().body(createFieldsErrorJson(bindingResult.getFieldErrors()));
        }


        purchaseDto.setId(id);
        PurchaseDto updated = purchaseService.update(purchaseDto);
        return ResponseEntity.ok(updated);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePurchase(@PathVariable("id") Long id){
        purchaseService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
