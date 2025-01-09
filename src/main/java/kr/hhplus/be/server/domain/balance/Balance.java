package kr.hhplus.be.server.domain.balance;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Balance {
    @Id
    private Long id;
    private Long balance;
    private Long userId;

    public void charge(Long balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("충전할 포인트가 0보다 작습니다");
        }

        this.balance += balance;
    }

    public void use(Long balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("사용할 포인트가 0보다 작습니다");
        }
        if (this.balance < balance) {
            throw new IllegalArgumentException("포인트가 부족합니다");
        }
        this.balance -= balance;
    }
}
