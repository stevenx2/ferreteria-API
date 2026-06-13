package org.ferreteria.services;

import org.ferreteria.dto.SaleDetailDto;
import org.ferreteria.entities.Product;
import org.ferreteria.entities.Sale;
import org.ferreteria.entities.SaleDetail;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.repositories.ProductRepo;
import org.ferreteria.repositories.SaleDetailRepo;
import org.ferreteria.repositories.SaleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class SaleDetailServiceImpl implements SaleDetailService {


    private final SaleDetailRepo saleDetailRepo;

    private final SaleRepo saleRepo;

    private final ProductRepo productRepo;

    @Autowired
    private MessageSource messageSource;

    public SaleDetailServiceImpl(SaleDetailRepo saleDetailRepo,SaleRepo saleRepo,ProductRepo productRepo) {
        this.saleDetailRepo = saleDetailRepo;
        this.saleRepo = saleRepo;
        this.productRepo = productRepo;
    }

    @Override
    public List<SaleDetail> findAll() {
        return saleDetailRepo.findAll(Sort.by("quantity"));
    }

    @Override
    public SaleDetail findById(Long id) {
        return saleDetailRepo.findById(id).orElseThrow(() -> new ResourceNotFound(
                messageSource.getMessage(
                        "sale.detail.not.found",
                        new Object[]{id},
                        LocaleContextHolder.getLocale()
                )
        ));
    }



    @Override
    public SaleDetail save(SaleDetailDto saleDetail) {

        Sale sale = saleRepo
                .findById(saleDetail.getSaleId())
                .orElseThrow(() -> new ResourceNotFound(
                        messageSource.getMessage(
                                "sale.not.found",
                                new Object[]{saleDetail.getSaleId()},
                                LocaleContextHolder.getLocale()
                        )
                ));


        Product product = productRepo.findById(saleDetail.getProductId())
                .orElseThrow(() -> new ResourceNotFound(
                        messageSource.getMessage(
                                "product.search.not_found_by_id",
                                new Object[]{saleDetail.getProductId()},
                                LocaleContextHolder.getLocale()
                        )
                ));




        SaleDetail detail = new SaleDetail();
        detail.setSale(sale);
        detail.setProduct(product);
        detail.setQuantity(saleDetail.getQuantity());


        return saleDetailRepo.save(detail);
    }




    @Override
    public SaleDetail update(SaleDetailDto saleDetail) {


        SaleDetail detailFromDb = saleDetailRepo.findById(saleDetail.getId()).orElseThrow(() -> new ResourceNotFound(
                messageSource.getMessage(
                        "sale.detail.not.found",
                        new Object[]{saleDetail.getId()},
                        LocaleContextHolder.getLocale()
                )
        ));

        Sale sale = saleRepo
                .findById(saleDetail.getSaleId())
                .orElseThrow(() -> new ResourceNotFound(
                        messageSource.getMessage(
                                "sale.not.found",
                                new Object[]{saleDetail.getSaleId()},
                                LocaleContextHolder.getLocale()
                        )
                ));


        Product product = productRepo.findById(saleDetail.getProductId())
                .orElseThrow(() -> new ResourceNotFound(
                        messageSource.getMessage(
                                "product.search.not_found_by_id",
                                new Object[]{saleDetail.getProductId()},
                                LocaleContextHolder.getLocale()
                        )
                ));



        detailFromDb.setSale(sale);
        detailFromDb.setProduct(product);
        detailFromDb.setQuantity(saleDetail.getQuantity());

        return saleDetailRepo.save(detailFromDb);
    }

    @Override
    public void deleteById(Long id) {

        if(!saleDetailRepo.existsById(id)){
            throw new ResourceNotFound(
                    messageSource.getMessage(
                            "sale.detail.not.found",
                            new Object[]{id},
                            LocaleContextHolder.getLocale()
                    )
            );
        }

        saleDetailRepo.deleteById(id);

    }
}
