package com.sia.api.region.demo.configs;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author joonhokim
 * @date 2022/03/28
 * @description 공통 모듈 Bean 객체 등록
 */
@Configuration
public class AppConfig {

    /**
     *  공통 모듈 Bean 객체 등록
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }




}
