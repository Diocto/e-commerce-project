package kr.hhplus.be.server.infrastructure.balance;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.balance.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceJpaRepository extends JpaRepository<Balance, Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Balance b where b.id = :id")
    Optional<Balance> findByIdWithLock(Long id);
}
