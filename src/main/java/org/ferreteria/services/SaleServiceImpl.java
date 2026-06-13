package org.ferreteria.services;

import org.ferreteria.dto.SaleDto;
import org.ferreteria.entities.Client;
import org.ferreteria.entities.Sale;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.repositories.ClientRepo;
import org.ferreteria.repositories.SaleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class SaleServiceImpl implements SaleService {


    private final SaleRepo saleRepo;

    private final ClientRepo clientRepo;

    @Autowired
    private MessageSource messageSource;

    public SaleServiceImpl(SaleRepo saleRepo, ClientRepo clientRepo) {
        this.saleRepo = saleRepo;
        this.clientRepo = clientRepo;
    }

    @Transactional(readOnly = true)
    @Override
    public List<SaleDto> findAll() {
        return saleRepo.findAll()
                .stream().map(this::mapToSaleDto)
                .toList();
    }


    @Override
    public SaleDto findById(Long id) {
        return saleRepo
                .findById(id)
                .map(this::mapToSaleDto)
                .orElseThrow(()-> new ResourceNotFound(
                        messageSource.getMessage(
                                "sale.not.found",
                                new Object[]{id},
                                LocaleContextHolder.getLocale()
                        )
                ));
    }




    @Override
    public SaleDto save(SaleDto sale) {

        Client client = clientRepo.findById(sale.getClientId()).orElseThrow(() -> new ResourceNotFound(
                messageSource.getMessage(
                        "error.client.notFound",
                        new Object[]{sale.getClientId()},
                        LocaleContextHolder.getLocale()
                )
        ));


        Sale newSale = new Sale();
        newSale.setDate(sale.getDate());
        newSale.setClient(client);

        Sale saved = saleRepo.save(newSale);
        return this.mapToSaleDto(saved);
    }


    @Override
    public SaleDto update(SaleDto sale) {


        Sale saleFromDb = saleRepo
                .findById(sale.getId())
                .orElseThrow(() -> new ResourceNotFound(
                        messageSource.getMessage(
                                "sale.not.found",
                                new Object[]{sale.getId()},
                                LocaleContextHolder.getLocale()
                        )
                ));


        Client client = clientRepo.findById(sale.getClientId()).orElseThrow(() -> new ResourceNotFound(
                messageSource.getMessage(
                        "error.client.notFound",
                        new Object[]{sale.getClientId()},
                        LocaleContextHolder.getLocale()
                )
        ));




        saleFromDb.setDate(sale.getDate());
        saleFromDb.setClient(client);

        Sale updated = saleRepo.save(saleFromDb);
        return this.mapToSaleDto(updated);
    }




    @Override
    public void deleteById(Long id) {

        if(!saleRepo.existsById(id)){
            throw new ResourceNotFound(
                    messageSource.getMessage(
                            "sale.not.found",
                            new Object[]{id},
                            LocaleContextHolder.getLocale()
                    )
            );
        }

        saleRepo.deleteById(id);

    }
}
