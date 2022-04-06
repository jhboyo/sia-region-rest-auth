package com.sia.api.region.demo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sia.api.region.demo.account.LoginDto;
import com.sia.api.region.demo.account.TokenDto;
import com.sia.api.region.demo.common.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author joonhokim
 * @date 2022/04/06
 * @description :
 */
public class AccessTokenTests extends BaseTest {

    @Test
    @DisplayName("token을 정상적으로 생성하는 테스트")
    public void requestToken() throws Exception {
        //Token 요청
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("joonho3");
        loginDto.setPassword("joonho3");

        ResultActions resultActionsForToken = mockMvc.perform(post("/api/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaTypes.HAL_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andDo(print())
                .andExpect(status().isOk())
                ;

        MvcResult result = resultActionsForToken.andReturn();

        TokenDto tokenDto = new ObjectMapper().readValue(result.getResponse().getContentAsString(), TokenDto.class);

        assertThat(tokenDto.getToken()).isNotEmpty();
        assertThat(tokenDto.getToken()).isNotNull();
    }

}
