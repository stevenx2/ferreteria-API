package org.ferreteria.services;

import org.ferreteria.dto.PurchaseDto;
import org.ferreteria.entities.Purchase;
import org.ferreteria.entities.Supplier;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.repositories.PurchaseRepo;
import org.ferreteria.repositories.SupplierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class PurchaseServiceImpl implements PurchaseService {


    private final PurchaseRepo purchaseRepo;

    private final SupplierRepo supplierRepo;


    @Autowired
    private MessageSource messageSource;

    public PurchaseServiceImpl(PurchaseRepo purchaseRepo, SupplierRepo supplierRepo) {
        this.purchaseRepo = purchaseRepo;
        this.supplierRepo = supplierRepo;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PurchaseDto> findAll() {
        return purchaseRepo.findAll().stream().map(this::mapToPurchaseDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public PurchaseDto findById(Long id) {
        return purchaseRepo.findById(id).map(this::mapToPurchaseDto).orElseThrow(() -> new ResourceNotFound(messageSource.getMessage("purchase.notfound", new Object[]{id}, LocaleContextHolder.getLocale())));
    }


    @Override
    public PurchaseDto save(PurchaseDto purchase) {

        Supplier supplier = supplierRepo.findById(purchase.getSupplierId()).orElseThrow(() -> new ResourceNotFound(
                messageSource.getMessage(
                        "supplier.search.not_found_by_id",
                        new Object[]{purchase.getSupplierId()},
                        LocaleContextHolder.getLocale()
                )
        ));


        Purchase purchaseToSave = new Purchase();
        purchaseToSave.setSupplier(supplier);
        purchaseToSave.setDate(purchase.getDate());

        Purchase saved = purchaseRepo.save(purchaseToSave);


        return this.mapToPurchaseDto(saved);
    }



    @Override
    public PurchaseDto update(PurchaseDto purchase) {

        Purchase fromDb = purchaseRepo.findById(purchase.getId()).orElseThrow(() -> new ResourceNotFound(messageSource.getMessage("purchase.notfound", new Object[]{purchase.getId()}, LocaleContextHolder.getLocale())));


        Long oldSupplierId = fromDb.getSupplier().getId();

        Supplier newSupplier = supplierRepo.findById(purchase.getSupplierId()).orElseThrow(() -> new ResourceNotFound(
                messageSource.getMessage(
                        "supplier.search.not_found_by_id",
                        new Object[]{purchase.getSupplierId()},
                        LocaleContextHolder.getLocale()
                )));


        if(!Objects.equals(oldSupplierId, newSupplier.getId())){
            fromDb.setSupplier(newSupplier);
        }

        fromDb.setDate(purchase.getDate());

        Purchase saved = purchaseRepo.save(fromDb);

        return this.mapToPurchaseDto(saved);
    }



    @Override
    public void deleteById(Long id) {

        if(!purchaseRepo.existsById(id)){
            throw new ResourceNotFound(
                    messageSource.getMessage("purchase.notfound", new Object[]{id}, LocaleContextHolder.getLocale())
            );
        }


        purchaseRepo.deleteById(id);

    }
}
