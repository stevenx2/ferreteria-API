package org.ferreteria.controller;


import jakarta.validation.Valid;
import org.ferreteria.dto.SaleDetailDto;
import org.ferreteria.entities.SaleDetail;
import org.ferreteria.services.SaleDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.ferreteria.utils.Mapper;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import static org.ferreteria.utils.JsonCreator.createFieldsErrorJson;

@RestController
@RequestMapping("/api/v1/salesDetails")
public class SalesDetailsController {

    private final SaleDetailService saleDetailService;


    public SalesDetailsController(SaleDetailService saleDetailService) {
        this.saleDetailService = saleDetailService;
    }


    @GetMapping
    public ResponseEntity<List<SaleDetailDto>> findAll() {
        return ResponseEntity.ok(saleDetailService.findAll().stream().map(Mapper::toSaleDetailDto).toList());
    }


    @GetMapping("/{id}")
    public ResponseEntity<SaleDetailDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                Optional.of(saleDetailService.findById(id)).map(Mapper::toSaleDetailDto).get()
        );
    }




    @PostMapping
    public ResponseEntity<?> saveSaleDetail(
            @Valid @RequestBody SaleDetailDto saleDetailDto,
            BindingResult bindingResult
    ){

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(createFieldsErrorJson(bindingResult.getFieldErrors()));
        }


        SaleDetail saved = saleDetailService.save(saleDetailDto);


        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();


        return ResponseEntity.created(uri).body(Mapper.toSaleDetailDto(saved));

    }




    @PutMapping("/{id}")
    public ResponseEntity<?> updateSaleDetail(
            @Valid @RequestBody SaleDetailDto saleDetailDto,
            BindingResult bindingResult,
            @PathVariable("id") Long id
    ){

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(createFieldsErrorJson(bindingResult.getFieldErrors()));
        }


        saleDetailDto.setId(id);
        SaleDetail updated = saleDetailService.update(saleDetailDto);

        return ResponseEntity.ok(Mapper.toSaleDetailDto(updated));
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSaleDetail(@PathVariable("id") Long id){
        saleDetailService.deleteById(id);
        return ResponseEntity.ok().build();
    }

}
