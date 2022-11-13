package com.yida.sqlsession;

import java.util.List;

/**
 * @Auther: yida
 * @Date: 2022/11/12 23:32
 * @Description:
 */
public interface SqlSession {
	
	<E> List<E> selectList(String statementId, Object parameter) throws Exception;
	
	<E> E selectOne(String statementId, Object parameter) throws Exception;
	
	/**
	 * 生成代理对象
	 */
	<T> T getMapper(Class<?> mapperClass);
}
