package com.chg.hackdays.chappie.db.config;

import liquibase.integration.spring.SpringLiquibase;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {
    @Bean
    public ModelMapper controllerModelMapper(){
        return new ModelMapper();
    }

//    @Bean
//    public SpringLiquibase liquibase() {
//        SpringLiquibase liquibase = new SpringLiquibase();
//        liquibase.setChangeLog("classpath:liquibase/changelog.xml");
//        liquibase.setDataSource(dataSource());
//        return liquibase;
//    }
}
