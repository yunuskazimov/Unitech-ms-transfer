package az.unibank.unitechmstransfer.dao.repository;

import az.unibank.unitechmstransfer.dao.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepo extends JpaRepository<TransferEntity, Long> {
}
