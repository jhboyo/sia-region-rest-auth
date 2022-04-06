package com.sia.api.region.demo.account;

import com.sia.api.region.demo.security.jwt.JwtFilter;
import com.sia.api.region.demo.security.jwt.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author joonhokim
 * @date 2022/04/04
 * @description :
 */

@RestController
@RequestMapping("/api")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final AccountService accountService;

    public AccountController(TokenProvider tokenProvider,
                             AuthenticationManagerBuilder authenticationManagerBuilder,
                             AccountService accountService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.accountService = accountService;
    }


    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // token 생성
        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }


    @PostMapping("/signup")
    public ResponseEntity<Account> signup(@Valid @RequestBody UserDto userDto) {
        return ResponseEntity.ok(accountService.signup(userDto));
    }

    /**
     *  USER 와 ADMIN Role 모두 호출 가능
     */
    @GetMapping("/account")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Account> getMyAccountInfo() {
        return ResponseEntity.ok(accountService.getMyAccountWithAuthorities().get());
    }

    /**
     *  ADMIN Role 만 호출 가능
     */
    @GetMapping("/account/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')") // ADMIN Role 호출 가능
    public ResponseEntity<Account> getAccountInfo(@PathVariable String username) {
        return ResponseEntity.ok(accountService.getAccountWithAuthorities(username).get());
    }
}