package com.sia.api.region.demo.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * http://localhost:8080/api/authenticate 로
 * 인증 토큰 요청 시 UsernamePasswordAuthenticationToken 에서 사용
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String username;

    @Size(min = 3, max = 100)
    private String password;
}
