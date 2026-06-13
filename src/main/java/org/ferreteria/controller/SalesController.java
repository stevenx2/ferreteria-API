package org.ferreteria.controller;


import jakarta.validation.Valid;
import org.ferreteria.dto.SaleDto;
import org.ferreteria.services.SaleService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import static org.ferreteria.utils.JsonCreator.createFieldsErrorJson;

/**
 * Controladores de las ventas realizadas por la ferretería
 */
@RestController
@RequestMapping("/api/v1/sales")
public class SalesController {

    private final SaleService saleService;

    public SalesController(SaleService saleService) {
        this.saleService = saleService;
    }



    @GetMapping
    public ResponseEntity<List<SaleDto>> findAll(){
        return ResponseEntity.ok(saleService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<SaleDto> findById(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(saleService.findById(id));
    }



    @PostMapping
    public ResponseEntity<?> saveSale(
            @Valid @RequestBody SaleDto saleDto,
            BindingResult bindingResult
    ){


        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(createFieldsErrorJson(bindingResult.getFieldErrors()));
        }


        SaleDto saved = saleService.save(saleDto);


        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();


        return ResponseEntity.created(uri).body(saved);
    }





    @PutMapping("/{id}")
    public ResponseEntity<?> updateSale(
            @Valid @RequestBody SaleDto saleDto,
            BindingResult bindingResult,
            @PathVariable("id") Long id
    ){


        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(createFieldsErrorJson(bindingResult.getFieldErrors()));
        }


        saleDto.setId(id);
        SaleDto updated = saleService.update(saleDto);
        return ResponseEntity.ok(updated);

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSale( @PathVariable("id") Long id){
        saleService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
