package kr.hhplus.be.server.infrastructure.outbox;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OutboxMessageRepositoryImpl implements OutboxMessageRepository{
    private final OutboxMessageJpaRepository outboxMessageJpaRepository;

    public OutboxMessageRepositoryImpl(OutboxMessageJpaRepository outboxMessageJpaRepository) {
        this.outboxMessageJpaRepository = outboxMessageJpaRepository;
    }

    @Override
    public void save(OutboxMessage outboxMessage) {
        outboxMessageJpaRepository.save(outboxMessage);
    }

    @Override
    public OutboxMessage findById(Long id) {
        return outboxMessageJpaRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(OutboxMessage outboxMessage) {
        outboxMessageJpaRepository.delete(outboxMessage);
    }

    @Override
    public void deleteById(Long id) {
        outboxMessageJpaRepository.deleteById(id);
    }

    @Override
    public List<OutboxMessage> getPendingMessages(Long limit) {
        return outboxMessageJpaRepository.findTopNByStatusOrderByCreatedAtAsc(OutboxMessageStatus.PENDING, limit);
    }
}
