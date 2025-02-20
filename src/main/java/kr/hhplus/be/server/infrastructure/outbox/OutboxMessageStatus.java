package kr.hhplus.be.server.infrastructure.outbox;

public enum OutboxMessageStatus {
    PENDING,
    PUBLISHED,
    SUCCESS,
    FAIL
}
