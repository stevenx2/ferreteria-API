package org.ferreteria.services;

import org.ferreteria.dto.SupplierDto;
import org.ferreteria.entities.Supplier;
import org.ferreteria.problem.ResourceNotFound;
import org.ferreteria.repositories.SupplierRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class SupplierServiceImpl implements SupplierService {


    private final SupplierRepo supplierRepo;


    @Autowired
    private MessageSource messageSource;


    public SupplierServiceImpl(SupplierRepo supplierRepo) {
        this.supplierRepo = supplierRepo;
    }


    @Transactional(readOnly = true)
    @Override
    public SupplierDto findById(Long id) {
        return supplierRepo.findById(id)
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
    public List<SupplierDto> findByNameLike(String name) {
        return StreamSupport.stream(
                        supplierRepo.findByNameLike(name).spliterator(),
                        false
                )
                .map(this::mapToProductDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<SupplierDto> findAll() {
        return supplierRepo.findAll()
                .stream().map(this::mapToProductDto)
                .toList();
    }

    @Override
    public SupplierDto save(SupplierDto supplier) {
        Supplier saved = supplierRepo.save(this.mapToEntityObject(supplier));
        return this.mapToProductDto(saved);
    }

    @Override
    public SupplierDto update(SupplierDto supplier) {
        Supplier fromDb = supplierRepo.findById(supplier.getId())
                .orElseThrow(
                        () -> new ResourceNotFound(
                                messageSource.getMessage(
                                        "supplier.search.not_found_by_id",
                                        new Object[]{supplier.getId()},
                                        LocaleContextHolder.getLocale()
                                )
                        )
                );


        fromDb.setName(supplier.getName());
        fromDb.setAddress(supplier.getAddress());
        fromDb.setPhoneNumber(supplier.getPhoneNumber());

        Supplier saved = supplierRepo.save(fromDb);
        return this.mapToProductDto(saved);
    }



    @Override
    public void delete(Long id) {

        if(!supplierRepo.existsById(id)){
            throw new ResourceNotFound(
                    messageSource.getMessage(
                            "supplier.search.not_found_by_id",
                            new Object[]{id},
                            LocaleContextHolder.getLocale()
                    )
            );
        }

        supplierRepo.deleteById(id);
    }


}
