package com.sia.api.region.demo.account;

import com.sia.api.region.demo.security.jwt.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

/**
 * @author joonhokim
 * @date 2022/04/05
 * @description :
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Account signup(UserDto userDto) {
        if (accountRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 사용자입니다.");
        }
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build()
                ;

        Account account = Account.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build()
                ;
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public Optional<Account> getAccountWithAuthorities(String username) {
        return accountRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<Account> getMyAccountWithAuthorities() {
        return SecurityUtil.getCurrentUsername().flatMap(accountRepository::findOneWithAuthoritiesByUsername);
    }

}
