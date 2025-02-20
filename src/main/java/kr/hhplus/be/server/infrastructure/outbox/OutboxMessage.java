package kr.hhplus.be.server.infrastructure.outbox;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OutboxMessage {
    /// Kafka msg 처리의 안정성 보장을 위해 outbox pattern을 사용하기 위한 entity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aggregateId;
    private String aggregateType;
    private String payload;
    private String topic;
    private String key_value;
    private OutboxMessageStatus status;
    private String result;
    private int retryCount;

    public void published() {
        this.status = OutboxMessageStatus.PUBLISHED;
    }

    public void success(String result) {
        this.status = OutboxMessageStatus.SUCCESS;
        this.result = result;
    }

    public void fail(String result) {
        this.status = OutboxMessageStatus.FAIL;
        this.result = result;
    }
}
