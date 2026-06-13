package org.ferreteria.services;


import org.ferreteria.dto.SaleDto;
import org.ferreteria.entities.Sale;

import java.util.List;

public interface SaleService {


    /**
     * Mapear un objeto de la clase entidad al objeto de tranferencia de datos.
     */
    default SaleDto mapToSaleDto(Sale sale){
        return new SaleDto(
                sale.getId(),
                sale.getDate(),
                sale.getClient().getId()
        );
    }



    List<SaleDto> findAll();
    SaleDto findById(Long id);
    SaleDto save(SaleDto sale);
    SaleDto update(SaleDto sale);
    void deleteById(Long id);
}
