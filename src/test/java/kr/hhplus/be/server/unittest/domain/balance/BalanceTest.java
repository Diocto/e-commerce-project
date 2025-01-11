package kr.hhplus.be.server.unittest.domain.balance;

import kr.hhplus.be.server.domain.balance.Balance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BalanceTest {
    Balance utt;

    @BeforeEach
    public void setUp() {
        utt = new Balance(1L, 0L, 1L);
    }

    @Test
    public void charge_충전할포인트가_0보다작으면_IllegalArgumentException이_발생한다() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            utt.charge(-1L);
        });
    }

    @Test
    public void charge_충전할포인트가_0보다크면_포인트가_충전된다() {
        utt.charge(1L);
        assertEquals(1L, utt.getBalance());
    }

    @Test
    public void use_사용할포인트가_0보다작으면_IllegalArgumentException이_발생한다() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            utt.use(-1L);
        });
    }

    @Test
    public void use_포인트가_부족하면_IllegalArgumentException이_발생한다() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            utt.use(1L);
        });
    }

    @Test
    public void use_포인트가_충분하면_포인트가_사용된다() {
        utt.charge(1L);
        utt.use(1L);
        assertEquals(0L, utt.getBalance());
    }
}