package org.ferreteria.services;


import org.ferreteria.dto.SupplierDto;
import org.ferreteria.entities.Supplier;

import java.util.List;

public interface SupplierService {

    // para mapear objeto a su clase DTO y controlar los campos que quiero mostrar en la respuesta json
    default SupplierDto mapToProductDto(Supplier supplier){
        return new SupplierDto(supplier.getId(), supplier.getName(), supplier.getPhoneNumber(), supplier.getAddress());
    }



    SupplierDto findById(Long id);


    int getProductsTotal(Long id);


}
