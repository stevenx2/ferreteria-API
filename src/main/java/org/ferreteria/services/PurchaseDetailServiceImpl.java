package org.ferreteria.services;

import org.ferreteria.dto.PurchaseDetailDto;
import org.ferreteria.entities.Product;
import org.ferreteria.entities.Purchase;
import org.ferreteria.entities.PurchaseDetail;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.repositories.ProductRepo;
import org.ferreteria.repositories.PurchaseDetailRepo;
import org.ferreteria.repositories.PurchaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *  servicio de los detalles de cada compra
 */
@Service
@Transactional
public class PurchaseDetailServiceImpl implements PurchaseDetailService {


    private final PurchaseDetailRepo purchaseDetailRepo;

    private final PurchaseRepo purchaseRepo;

    private final ProductRepo productRepo;

    @Autowired
    private MessageSource messageSource;


    public PurchaseDetailServiceImpl(PurchaseDetailRepo purchaseDetailRepo, PurchaseRepo purchaseRepo, ProductRepo productRepo) {
        this.purchaseDetailRepo = purchaseDetailRepo;
        this.purchaseRepo = purchaseRepo;
        this.productRepo = productRepo;
    }

    @Transactional(readOnly = true)
    @Override
    public List<PurchaseDetailDto> findAll() {
        return purchaseDetailRepo.findAll()
                .stream().map(this::mapToPurchaseDetailDto)
                .toList();
    }

    @Override
    public PurchaseDetailDto findById(Long id) {
        return purchaseDetailRepo.findById(id)
                .map(this::mapToPurchaseDetailDto)
                .orElseThrow(() -> new ResourceNotFound(
                        messageSource.getMessage(
                                "exception.purchaseDetail.notFound",
                                new Object[]{id},
                                LocaleContextHolder.getLocale()
                        )
                ));
    }




    @Override
    public PurchaseDetailDto save(PurchaseDetailDto purchaseDetailDto) {
        PurchaseDetail detail = new PurchaseDetail();

        Purchase purchase = purchaseRepo.findById(purchaseDetailDto.getPurchaseId())
                .orElseThrow(() -> new ResourceNotFound(messageSource.getMessage("purchase.notfound", new Object[]{purchaseDetailDto.getPurchaseId()}, LocaleContextHolder.getLocale())));


        Product product = productRepo.findById(purchaseDetailDto.getProductId())
                .orElseThrow(() -> new ResourceNotFound(messageSource.getMessage("product.search.not_found_by_id", new Object[]{purchaseDetailDto.getProductId()}, LocaleContextHolder.getLocale())));


        detail.setQuantity(purchaseDetailDto.getQuantity());
        detail.setPurchase(purchase);
        detail.setProduct(product);

        PurchaseDetail saved = purchaseDetailRepo.save(detail);

        return this.mapToPurchaseDetailDto(saved);
    }





    @Override
    public PurchaseDetailDto update(PurchaseDetailDto purchaseDetailDto) {

        PurchaseDetail detail = purchaseDetailRepo.findById(purchaseDetailDto.getId())
                .orElseThrow(() -> new ResourceNotFound(messageSource.getMessage(
                        "exception.purchaseDetail.notFound",
                        new Object[]{purchaseDetailDto.getId()},
                        LocaleContextHolder.getLocale()
                )));


        Purchase purchase = purchaseRepo.findById(purchaseDetailDto.getPurchaseId())
                .orElseThrow(() -> new ResourceNotFound(messageSource.getMessage("purchase.notfound", new Object[]{purchaseDetailDto.getPurchaseId()}, LocaleContextHolder.getLocale())));


        Product product = productRepo.findById(purchaseDetailDto.getProductId())
                .orElseThrow(() -> new ResourceNotFound(messageSource.getMessage("product.search.not_found_by_id", new Object[]{purchaseDetailDto.getProductId()}, LocaleContextHolder.getLocale())));



        detail.setQuantity(purchaseDetailDto.getQuantity());
        detail.setPurchase(purchase);
        detail.setProduct(product);

        PurchaseDetail saved = purchaseDetailRepo.save(detail);

        return this.mapToPurchaseDetailDto(saved);
    }




    @Override
    public void deleteById(Long id) {

        if(!purchaseDetailRepo.existsById(id)){
            throw new ResourceNotFound(messageSource.getMessage(
                    "exception.purchaseDetail.notFound",
                    new Object[]{id},
                    LocaleContextHolder.getLocale()
            ));
        }

        purchaseDetailRepo.deleteById(id);
    }
}
