package com.yida.sqlsession;

import com.yida.config.XMLConfigBuilder;
import com.yida.pojo.Configuration;
import com.yida.sqlsession.impl.DefaultSqlSessionFactory;
import org.dom4j.DocumentException;

import java.io.InputStream;

/**
 * @Auther: yida
 * @Date: 2022/11/12 23:30
 * @Description:解析配置文件，封装Configuration；创建sqlsessionFactory工厂对象
 */
public class SqlSessionFactoryBuilder {
	public SqlSessionFactory build(InputStream inputStream) throws DocumentException, ClassNotFoundException {
		// 1.解析配置文件，封装容器对象 XMLConfigBuilder:专门解析核心配置文件的解析类
		XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
		Configuration configuration = xmlConfigBuilder.parse(inputStream);
		
		// 2.创建SqlSessionFactory工厂对象
		return new DefaultSqlSessionFactory(configuration);
	}
}
