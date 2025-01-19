package kr.hhplus.be.server.unittest.domain.balance;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.infrastructure.balance.BalanceRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

@Transactional
public class BalanceServiceTest {
    private BalanceService uut;

    @Mock
    private BalanceRepositoryImpl balanceRepositoryImpl;

    @Mock
    private Balance balance;

    @BeforeEach
    public void setUp() {
        // Mock 초기화
        MockitoAnnotations.initMocks(this);

        uut = new BalanceService(balanceRepositoryImpl);
    }

    @Test
    public void charge_Repository_조회_후_charge_호출후_저장한다() {
        // given
        Long userId = 1L;
        Long point = 1L;

        when(balanceRepositoryImpl.findByUserIdWithLock(userId)).thenReturn(Optional.of(balance));
        uut.charge(userId, point);

        // then
        verify(balanceRepositoryImpl).findByUserIdWithLock(userId);
        verify(balance).charge(point);
        verify(balanceRepositoryImpl).save(balance);

    }

    @Test
    public void use_Repository_조회_후_use_호출후_저장한다() {
        // given
        Long userId = 1L;
        Long point = 1L;

        when(balanceRepositoryImpl.findByUserIdWithLock(userId)).thenReturn(Optional.of(balance));
        uut.use(userId, point);

        // then
        verify(balanceRepositoryImpl).findByUserIdWithLock(userId);
        verify(balance).use(point);
        verify(balanceRepositoryImpl).save(balance);
    }
}
