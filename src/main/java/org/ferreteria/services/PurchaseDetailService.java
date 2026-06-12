package org.ferreteria.services;

import org.ferreteria.dto.PurchaseDetailDto;
import org.ferreteria.entities.PurchaseDetail;

import java.util.List;

public interface PurchaseDetailService {

    /**
     * Mapear al objeto dto
     */
    default PurchaseDetailDto mapToPurchaseDetailDto(PurchaseDetail purchaseDetail){
        return new PurchaseDetailDto(
                purchaseDetail.getId(),
                purchaseDetail.getPurchase().getId(),
                purchaseDetail.getProduct().getId(),
                purchaseDetail.getQuantity()
        );
    }



    List<PurchaseDetailDto> findAll();

    PurchaseDetailDto findById(Long id);

    PurchaseDetailDto save(PurchaseDetailDto purchaseDetailDto);

    PurchaseDetailDto update(PurchaseDetailDto purchaseDetailDto);

    void deleteById(Long id);
}
