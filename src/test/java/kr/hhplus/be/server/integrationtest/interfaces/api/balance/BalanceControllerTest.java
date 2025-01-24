package kr.hhplus.be.server.integrationtest.interfaces.api.balance;

import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.IBalanceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BalanceControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    IBalanceRepository balanceRepository;

    @BeforeEach
    void setUp() {
        for (int i = 1; i <= 10; i++) {
            balanceRepository.save(Balance.builder().userId((long) i).balance(0L).build());
        }
    }

    @AfterEach
    void tearDown() {
        balanceRepository.deleteAll();
    }

    @Test
    void 사용자_잔액_조회_없는유저() throws Exception {
        mockMvc.perform(get("/balances").header("Authorization", "Bearer 99"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void 사용자_잔액_충전_없는유저() throws Exception {
        mockMvc.perform(post("/balances").header("Authorization", "Bearer 99").contentType("application/json").content("{\"balance\":1000}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void 사용자_잔액_조회_있는유저() throws Exception {
        // given
        // when
        // then

        mockMvc.perform(post("/balances").header("Authorization", "Bearer 1").contentType("application/json").content("{\"balance\":1000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBalance").value(1000));

        mockMvc.perform(get("/balances").header("Authorization", "Bearer 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    void 사용자_잔액_충전_있는유저() throws Exception {
        // given
        // when
        // then

        mockMvc.perform(post("/balances").header("Authorization", "Bearer 1").contentType("application/json").content("{\"balance\":1000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBalance").value(1000));

        mockMvc.perform(get("/balances").header("Authorization", "Bearer 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(1000));

        mockMvc.perform(post("/balances").header("Authorization", "Bearer 1").contentType("application/json").content("{\"balance\":1000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBalance").value(2000));

        mockMvc.perform(get("/balances").header("Authorization", "Bearer 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(2000));
    }

    @Test
    void 여러_사용자가_동시애_10번_충전시_동시성이_보장된다() throws Exception {
        /// given
        /// 10명의 사용자의 잔액 초기화
        IntStream.range(1, 11).parallel().forEach(i -> {
            try {
                mockMvc.perform(post("/balances").header("Authorization", "Bearer " + i).contentType("application/json").content("{\"balance\":1000}"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.totalBalance").value(1000));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Test
    void 한_사용자가_동시에_10번_충전시_동시성이_보장된다_낙관적락() throws Exception {
        Long userId = 1L;
        AtomicReference<Long> succeedCount = new AtomicReference<>(0L);
        IntStream.range(1, 11).parallel().forEach(i -> {
            try {
                MvcResult result = mockMvc.perform(post("/balances").header("Authorization", "Bearer " + userId).contentType("application/json").content("{\"balance\":1000}"))
                        .andReturn();
                if(result.getResponse().getStatus() == 200) {
                    succeedCount.getAndSet(succeedCount.get() + 1);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Verify the total balance for the user
        mockMvc.perform(get("/balances").header("Authorization", "Bearer " + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(succeedCount.get() * 1000));
    }
}
