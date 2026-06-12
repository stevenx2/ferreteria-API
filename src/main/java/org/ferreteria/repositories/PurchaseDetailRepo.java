package org.ferreteria.repositories;

import org.ferreteria.entities.PurchaseDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseDetailRepo extends JpaRepository<PurchaseDetail,Long> {
}
