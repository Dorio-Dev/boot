package com.daradat.boot.framework.dataaccess.config;

import com.daradat.boot.framework.dataaccess.CommonDao;
import com.daradat.boot.framework.dataaccess.mybatis.MybatisCommonDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisCommonDaoConfig {
    @Bean
    public CommonDao commonDao(){
        return new MybatisCommonDao();
    }
}
