package com.sia.api.region.demo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SiaRegionRestApiDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SiaRegionRestApiDemoApplication.class, args);

    }


    /**
     *  공통 모듈 Bean 객체 등록
     *
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

}
