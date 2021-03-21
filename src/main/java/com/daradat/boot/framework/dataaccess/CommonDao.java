package com.daradat.boot.framework.dataaccess;

import java.util.List;

public interface CommonDao {
    <T> T select(String var1);
    <T> T select(String var1, Object var2);
    <E> List<E> selectList(String var1);
    <E> List<E> selectList(String var1, Object var2);
}
