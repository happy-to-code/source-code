package com.yida.sqlsession.impl;

import com.yida.executor.Executor;
import com.yida.pojo.Configuration;
import com.yida.pojo.MappedStatement;
import com.yida.sqlsession.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.*;
import java.util.Collection;
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
	
	@Override
	public <T> T getMapper(Class<?> mapperClass) {
		// 使用JDK的动态代理生成基于接口的代理对象
		Object proxy = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
			/**
			 *
			 * @param o         代理对象的引用
			 * @param method    被调用的方法的字节码对象
			 * @param objects   调用的方法的参数
			 * @return
			 * @throws Throwable
			 */
			@Override
			public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
				//  具体逻辑：执行底层的JDBC
				//  通过调用sqlSession里面的方法来完成方法的调用
				String methodName = method.getName();
				// com.yida.dao.IUserDao
				String className = method.getDeclaringClass().getName();
				String statementId = className + "." + methodName;
				
				MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
				String sqlcommandType = mappedStatement.getSqlcommandType();
				switch (sqlcommandType) {
					case "select":
						Type genericReturnType = method.getGenericReturnType();
						//	调selectOne还是调selectAll呢？
						if (genericReturnType instanceof ParameterizedType) {
							if (objects != null) {
								return selectList(statementId, objects[0]);
							}
							return selectList(statementId, null);
						}
						/*Class<?> returnType = method.getReturnType();
						boolean assignableFrom = Collection.class.isAssignableFrom(returnType);
						if(assignableFrom){
							if(mappedStatement.getParameterType() !=null) {
								return   selectList(statementId, objects[0]);
							}
							return selectList(statementId, null);
						}*/
						
						return selectOne(statementId, objects[0]);
					case "update":
						//	执行更新方法调用  TODO
						break;
					case "delete":
						//	执行删除方法调用  TODO
						break;
					case "insert":
						//	执行更新方法调用  TODO
						break;
				}
				return null;
			}
		});
		
		
		return (T) proxy;
	}
}
