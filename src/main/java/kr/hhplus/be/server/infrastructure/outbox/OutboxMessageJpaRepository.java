package kr.hhplus.be.server.infrastructure.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OutboxMessageJpaRepository extends JpaRepository<OutboxMessage, Long> {
    @Query("SELECT om FROM OutboxMessage om WHERE om.status = ?1 ORDER BY om.aggregateId ASC LIMIT ?2")
    List<OutboxMessage> findTopNByStatusOrderByCreatedAtAsc(OutboxMessageStatus outboxMessageStatus, Long limit);

    Optional<OutboxMessage> findByKeyValue(String keyValue);
}
