package com.yida.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.yida.io.Resources;
import com.yida.pojo.Configuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Properties;

/**
 * @Auther: yida
 * @Date: 2022/11/12 23:43
 * @Description:
 */
@Data
public class XMLConfigBuilder {
	private Configuration configuration;
	
	public XMLConfigBuilder(Configuration configuration) {
		this.configuration = configuration;
	}
	
	public XMLConfigBuilder() {
		this.configuration = new Configuration();
	}
	
	public Configuration parse(InputStream inputStream) throws DocumentException, ClassNotFoundException {
		Document document = new SAXReader().read(inputStream);
		Element rootElement = document.getRootElement();
		
		// 获取数据源配置文件
		// <dataSource>
		//     <property name="driverClassName" value="com.mysql.jdbc.Driver"></property>
		//     <property name="url" value="jdbc:mysql:///yida_mybatis?useSSL=false&amp;characterEncoding=UTF-8&amp;serverTimezone=UTC"></property>
		//     <property name="username" value="root"></property>
		//     <property name="password" value="root"></property>
		// </dataSource>
		List<Element> list = rootElement.selectNodes("//property");
		
		Properties properties = new Properties();
		for (Element element : list) {
			String name = element.attributeValue("name");
			String value = element.attributeValue("value");
			
			properties.setProperty(name, value);
		}
		
		// 创建数据源对象
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setDriverClassName(properties.getProperty("driverClassName"));
		dataSource.setUrl(properties.getProperty("url"));
		dataSource.setUsername(properties.getProperty("username"));
		dataSource.setPassword(properties.getProperty("password"));
		
		// 将创建好的数据源连接对象封装进configuration中
		configuration.setDataSource(dataSource);
		
		// 解析映射配置文件
		// 1、获取配置文件路径
		// 2、解析
		// 3、封装mappedStatement
		//      <mappers>
		//         <mapper resource="mapper/UserMapper.xml"></mapper>
		//     </mappers>
		List<Element> mapperList = rootElement.selectNodes("//mapper");
		for (Element element : mapperList) {
			String mapperPath = element.attributeValue("resource");
			
			InputStream resourceAsStream = Resources.getResourceAsStream(mapperPath);
			XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configuration);
			
			xmlMapperBuilder.parse(resourceAsStream);
		}
		
		return configuration;
	}
	
}
