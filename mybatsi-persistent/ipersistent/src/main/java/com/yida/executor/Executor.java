package com.yida.executor;

import com.yida.pojo.Configuration;
import com.yida.pojo.MappedStatement;

import java.util.List;

/**
 * @Auther: yida
 * @Date: 2022/11/13 00:33
 * @Description:
 */
public interface Executor {
	<E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object param) throws Exception;
}
