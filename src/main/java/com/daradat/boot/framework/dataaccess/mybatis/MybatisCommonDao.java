package com.daradat.boot.framework.dataaccess.mybatis;

import com.daradat.boot.framework.dataaccess.CommonDao;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MybatisCommonDao implements CommonDao, InitializingBean {
    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    private String a = "sqlSessionFactory";
    protected Map<String, SqlSession> localSqlSessionMap = new HashMap();

    @Override
    public <T> T select(String var1) {
        return sqlSessionTemplate.selectOne(var1);
    }

    @Override
    public <T> T select(String var1, Object var2) {
        return sqlSessionTemplate.selectOne(var1, var2);
    }

    @Override
    public <E> List<E> selectList(String var1) {
        return sqlSessionTemplate.selectList(var1);
    }

    @Override
    public <E> List<E> selectList(String var1, Object var2) {
        return sqlSessionTemplate.selectList(var1, var2);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
