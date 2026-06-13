package org.ferreteria.repositories;

import org.ferreteria.entities.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleDetailRepo extends JpaRepository<SaleDetail,Long> {
}
