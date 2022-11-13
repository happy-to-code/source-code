package com.yida.sqlsession.impl;

import com.yida.executor.Executor;
import com.yida.executor.impl.SimpleExecutor;
import com.yida.pojo.Configuration;
import com.yida.sqlsession.SqlSession;
import com.yida.sqlsession.SqlSessionFactory;

/**
 * @Auther: yida
 * @Date: 2022/11/13 00:31
 * @Description:
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
	private Configuration configuration;
	
	public DefaultSqlSessionFactory(Configuration configuration) {
		this.configuration = configuration;
	}
	
	@Override
	public SqlSession openSession() {
		Executor executor = new SimpleExecutor();
		DefaultSqlSession defaultSqlSession = new DefaultSqlSession(configuration, executor);
		
		return defaultSqlSession;
	}
}
