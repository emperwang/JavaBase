package com.wk.SqlSession;

public interface Excutor {
    <T> T query(String statement,Object parameter);
}
