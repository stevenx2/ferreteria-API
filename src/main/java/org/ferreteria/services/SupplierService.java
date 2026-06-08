package org.ferreteria.services;


import org.ferreteria.dto.SupplierDto;
import org.ferreteria.entities.Supplier;

import java.util.List;

public interface SupplierService {

    // para mapear objeto a su clase DTO y controlar los campos que quiero mostrar en la respuesta json
    default SupplierDto mapToProductDto(Supplier supplier){
        return new SupplierDto(supplier.getId(), supplier.getName(), supplier.getPhoneNumber(), supplier.getAddress());
    }

    // Mapeo el objeto DTO al objeto de la entidad. Utilizado cuando se recibe el DTO del cuerpo de una petición y se quiere mapear para luego guardar en la base de datos
    default  Supplier mapToEntityObject(SupplierDto supplier){
        Supplier mapped = new Supplier();
        mapped.setAddress(supplier.getAddress());
        mapped.setName(supplier.getName());
        mapped.setPhoneNumber(supplier.getPhoneNumber());
        return mapped;
    }


    SupplierDto findById(Long id);



    List<SupplierDto> findByNameLike(String name);

    List<SupplierDto> findAll();

    SupplierDto save(SupplierDto supplier);

    SupplierDto update(SupplierDto supplier);

    void delete(Long id);

}
