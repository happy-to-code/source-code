package com.yida.executor.impl;

import com.yida.config.BoundSql;
import com.yida.executor.Executor;
import com.yida.pojo.Configuration;
import com.yida.pojo.MappedStatement;
import com.yida.utils.GenericTokenParser;
import com.yida.utils.ParameterMapping;
import com.yida.utils.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: yida
 * @Date: 2022/11/13 00:36
 * @Description:
 */
public class SimpleExecutor implements Executor {
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;
	
	@Override
	public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object param) throws Exception {
		//	加载驱动  获取数据库连接
		connection = configuration.getDataSource().getConnection();
		
		// 获取preparedStatement预编译对象
		// 获取要执行的sql
		// select * from user where id = #{id} and username = #{username}
		// 替换：  select * from user where id = ? and username = ?
		// 解析替换的过程中：#{id}里面的值保存下来
		String sql = mappedStatement.getSql();
		BoundSql boundSql = getBoundSql(sql);
		String finalSql = boundSql.getFinalSql(); // 此时参数已经换成了 ？
		// 根据sql获取预编译对象
		preparedStatement = connection.prepareStatement(finalSql);
		
		// 设置参数
		// parameterType="com.yida.pojo.User">
		String parameterType = mappedStatement.getParameterType();
		if (parameterType != null) {
			Class<?> parameterTypeClass = Class.forName(parameterType);
			
			List<ParameterMapping> parameterMappingList = boundSql.getParameterMappingList(); // #{id},#{username} --> [id,username]
			for (int i = 0; i < parameterMappingList.size(); i++) {
				ParameterMapping parameterMapping = parameterMappingList.get(i);
				
				// id  ||  username
				String parameterName = parameterMapping.getContent();
				//	根据字节码文件   通过反射获取字段值
				Field field = parameterTypeClass.getDeclaredField(parameterName);
				field.setAccessible(true);
				
				// 获取参数值
				Object value = field.get(param);
				preparedStatement.setObject(i + 1, value);
			}
		}
		
		// 执行sql   发起查询
		resultSet = preparedStatement.executeQuery();
		
		// 处理返回的结果集
		List<E> list = new ArrayList<>(10);
		while (resultSet.next()) {
			// 元数据信息   ==>   字段名   字段值
			ResultSetMetaData metaData = resultSet.getMetaData();
			
			// resultType="com.yida.pojo.User"
			String resultType = mappedStatement.getResultType();
			Class<?> resultTypeClass = Class.forName(resultType);
			Object o = resultTypeClass.newInstance();
			
			for (int i = 1; i <= metaData.getColumnCount(); i++) {
				String columnName = metaData.getColumnName(i);
				Object value = resultSet.getObject(columnName);
				
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultTypeClass);
				Method writeMethod = propertyDescriptor.getWriteMethod();
				writeMethod.invoke(o, value);
			}
			list.add((E) o);
		}
		
		return list;
	}
	
	private BoundSql getBoundSql(String sql) {
		ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
		GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
		
		// #{}占位符替换成？ 2.解析替换的过程中 将#{}里面的值保存下来 ParameterMapping
		String finalSql = genericTokenParser.parse(sql);
		
		// #{}里面的值的一个集合 id username
		List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
		return new BoundSql(finalSql, parameterMappings);
	}
}
