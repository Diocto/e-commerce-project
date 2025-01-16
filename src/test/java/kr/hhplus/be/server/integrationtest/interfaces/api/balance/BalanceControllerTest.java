package kr.hhplus.be.server.integrationtest.interfaces.api.balance;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BalanceControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    void 사용자_잔액_조회_없는유저() throws Exception {
        mockMvc.perform(get("/balances").header("Authorization", "Bearer 1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(0));
    }

    @Test
    void 사용자_잔액_충전_없는유저() throws Exception {
        mockMvc.perform(post("/balances").header("Authorization", "Bearer 1").contentType("application/json").content("{\"balance\":1000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalBalance").value(1000));
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

}
