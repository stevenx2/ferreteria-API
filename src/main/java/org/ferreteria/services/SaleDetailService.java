package org.ferreteria.services;

import org.ferreteria.dto.SaleDetailDto;
import org.ferreteria.entities.SaleDetail;

import java.util.List;

public interface SaleDetailService {

    List<SaleDetail> findAll();

    SaleDetail findById(Long id);

    SaleDetail save(SaleDetailDto saleDetail);

    SaleDetail update(SaleDetailDto saleDetail);

    void deleteById(Long id);

}
