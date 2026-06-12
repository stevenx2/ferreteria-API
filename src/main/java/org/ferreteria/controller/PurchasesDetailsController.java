package org.ferreteria.controller;

import jakarta.validation.Valid;
import org.ferreteria.dto.PurchaseDetailDto;
import org.ferreteria.services.PurchaseDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import  static org.ferreteria.utils.JsonCreator.createFieldsErrorJson;

/**
 * Controlador de los detalles de compra de productos hechos a proveedores por parte de la ferretería
 */
@RestController
@RequestMapping("/api/v1/purchasesDetails")
public class PurchasesDetailsController {


    private final PurchaseDetailService purchaseDetailService;


    public PurchasesDetailsController(PurchaseDetailService purchaseDetailService) {
        this.purchaseDetailService = purchaseDetailService;
    }



    @GetMapping
    public ResponseEntity<List<PurchaseDetailDto>> findAll(){
        return ResponseEntity.ok(purchaseDetailService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDetailDto> findById(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(purchaseDetailService.findById(id));
    }



    @PostMapping
    public ResponseEntity<?> saveDetail(
            @Valid @RequestBody PurchaseDetailDto purchaseDetailDto,
            BindingResult bindingResult
    ){

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(createFieldsErrorJson(bindingResult.getFieldErrors()));
        }


        PurchaseDetailDto saved = purchaseDetailService.save(purchaseDetailDto);


        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();


        return ResponseEntity.created(uri).body(saved);

    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateDetail(
            @Valid @RequestBody PurchaseDetailDto purchaseDetailDto,
            BindingResult bindingResult,
            @PathVariable("id") Long id
    ){

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(createFieldsErrorJson(bindingResult.getFieldErrors()));
        }

        purchaseDetailDto.setId(id);

        PurchaseDetailDto updated = purchaseDetailService.update(purchaseDetailDto);

        return ResponseEntity.ok(updated);
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDetail(
            @PathVariable("id") Long id
    ){
        purchaseDetailService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
