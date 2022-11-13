package com.yida.sqlsession.impl;

import com.yida.executor.Executor;
import com.yida.pojo.Configuration;
import com.yida.pojo.MappedStatement;
import com.yida.sqlsession.SqlSession;

import java.util.List;

/**
 * @Auther: yida
 * @Date: 2022/11/13 01:28
 * @Description:
 */
public class DefaultSqlSession implements SqlSession {
	private Configuration configuration;
	private Executor executor;
	
	public DefaultSqlSession(Configuration configuration, Executor executor) {
		this.configuration = configuration;
		this.executor = executor;
	}
	
	@Override
	public <E> List<E> selectList(String statementId, Object parameter) throws Exception {
		MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
		List<E> list = executor.query(configuration, mappedStatement, parameter);
		return list;
	}
	
	@Override
	public <E> E selectOne(String statementId, Object parameter) throws Exception {
		List<E> objects = this.selectList(statementId, parameter);
		if (objects.size() == 1) {
			return objects.get(0);
		} else if (objects.size() > 1) {
			throw new RuntimeException("返回结果过多");
		} else {
			return null;
		}
	}
}
