package kr.hhplus.be.server.infrastructure.outbox;

import java.util.List;

public interface OutboxMessageRepository {
    public void save(OutboxMessage outboxMessage);
    public OutboxMessage findById(Long id);
    public OutboxMessage findByKey(String key);
    public void delete(OutboxMessage outboxMessage);
    public void deleteById(Long id);
    public List<OutboxMessage> getPendingMessages(Long limit);
}
