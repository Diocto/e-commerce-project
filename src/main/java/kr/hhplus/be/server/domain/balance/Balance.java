package kr.hhplus.be.server.domain.balance;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", unique = true)
    private Long userId;
    private Long balance;
    @Version
    private Long version;
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
