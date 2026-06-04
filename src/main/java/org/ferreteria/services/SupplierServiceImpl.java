package org.ferreteria.services;

import org.ferreteria.dto.SupplierDto;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.repositories.SupplierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService{


    private final SupplierRepo supplierRepo;


    @Autowired
    private MessageSource messageSource;


    public SupplierServiceImpl(SupplierRepo supplierRepo) {
        this.supplierRepo = supplierRepo;
    }


    @Transactional(readOnly = true)
    @Override
    public SupplierDto findById(Long id) {
        return supplierRepo.findById(1L)
                .map(this::mapToProductDto)
                .orElseThrow(
                        () -> new ResourceNotFound(
                                messageSource.getMessage(
                                        "supplier.search.not_found_by_id",
                                        new Object[]{id},
                                        LocaleContextHolder.getLocale()
                                )
                        )
                );
    }

    @Transactional(readOnly = true)
    @Override
    public int getProductsTotal(Long id) {
        return supplierRepo.findById(1L).orElseThrow(() ->
                new ResourceNotFound(
                        messageSource.getMessage(
                                "supplier.search.not_found_by_id",
                                new Object[]{id},
                                LocaleContextHolder.getLocale()
                        )
                )).getProducts().size();
    }


}
