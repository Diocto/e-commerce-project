package kr.hhplus.be.server.domain.point;

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
public class Point {
    @Id
    private Long id;
    private Long point;
    private Long userId;

    public void charge(Long point) {
        if (point < 0) {
            throw new IllegalArgumentException("충전할 포인트가 0보다 작습니다");
        }

        this.point += point;
    }

    public void use(Long point) {
        if (point < 0) {
            throw new IllegalArgumentException("사용할 포인트가 0보다 작습니다");
        }
        if (this.point < point) {
            throw new IllegalArgumentException("포인트가 부족합니다");
        }
        this.point -= point;
    }
}
