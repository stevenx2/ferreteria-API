package org.ferreteria.controller;


import jakarta.validation.Valid;
import org.ferreteria.dto.ProductDto;
import org.ferreteria.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.ferreteria.utils.JsonCreator.createFieldsErrorJson;

import java.net.URI;
import java.util.List;

/**
 * controlador de productos
 */
@RestController
@RequestMapping("/api/v1/products")
public class ProductsController {

    private final ProductService productService;


    public ProductsController(ProductService productService) {
        this.productService = productService;

    }


    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(name = "name",required = false) String name
    ){

        if(name != null){
            return ResponseEntity.ok(productService.findByNameLike(name));
        }
        return ResponseEntity.ok(productService.findAll());
    }



    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(
            @PathVariable("id") Long id

    ){
        return ResponseEntity.ok(productService.findById(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto> delete(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(productService.deleteById(id));
    }



    @PostMapping
    public ResponseEntity<?> saveProduct(
            @Valid @RequestBody ProductDto productDto,
            BindingResult bindingResult
    ){

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(createFieldsErrorJson(bindingResult.getFieldErrors()));
        }


        ProductDto saved = productService.save(productDto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();


        return ResponseEntity.created(uri).body(saved);
    }




    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @Valid @RequestBody ProductDto productDto,
            BindingResult bindingResult,
            @PathVariable("id") Long id
    ){

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(createFieldsErrorJson(bindingResult.getFieldErrors()));
        }

        productDto.setId(id);
        ProductDto updated = productService.update(productDto);

        return ResponseEntity.ok(updated);
    }

}
