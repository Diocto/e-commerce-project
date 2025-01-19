package kr.hhplus.be.server.unittest.domain.balance;

import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.balance.BalanceUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class BalanceUseCaseTest {
    BalanceUseCase uut;

    @Mock
    BalanceService balanceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        uut = new BalanceUseCase(balanceService);
    }

    @Test
    public void charge_balanceService_charge_호출한다() {
        // given
        Long userId = 1L;
        Long point = 1L;

        // when
        uut.charge(userId, point);

        // then
        verify(balanceService).charge(userId, point);
    }

    @Test
    public void get_balanceService_get_호출한다() {
        // given
        Long userId = 1L;

        // when
        uut.get(userId);

        // then
        verify(balanceService).get(userId);
    }

}
