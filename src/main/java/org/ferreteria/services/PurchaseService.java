package org.ferreteria.services;

import org.ferreteria.dto.PurchaseDto;
import org.ferreteria.entities.Purchase;

import java.util.List;

/**
 * servicio de compras por parte de la ferreteria
 */
public interface PurchaseService {

    // mapear al objeto DTO
    default PurchaseDto mapToPurchaseDto(Purchase purchase){
        return new PurchaseDto(purchase.getId(),purchase.getSupplier().getId(),purchase.getDate());
    }


    List<PurchaseDto> findAll();

    PurchaseDto findById(Long id);

    PurchaseDto save(PurchaseDto purchase);

    PurchaseDto update(PurchaseDto purchase);

    void deleteById(Long id);
}
