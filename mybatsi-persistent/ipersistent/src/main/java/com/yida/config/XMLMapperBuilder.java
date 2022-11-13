package com.yida.config;

import com.yida.pojo.Configuration;
import com.yida.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

/**
 * @Auther: yida
 * @Date: 2022/11/13 00:04
 * @Description:
 */
public class XMLMapperBuilder {
	private Configuration configuration;
	
	public XMLMapperBuilder(Configuration configuration) {
		this.configuration = configuration;
	}
	
	public void parse(InputStream inputStream) throws DocumentException, ClassNotFoundException {
		Document document = new SAXReader().read(inputStream);
		Element rootElement = document.getRootElement();
		
		//<select id="selectOne" resultType="com.yida.pojo.User" parameterType="com.yida.pojo.User">
		//   select * from user where id = #{id} and username = #{username}
		//</select>
		String namespace = rootElement.attributeValue("namespace");
		List<Element> select = rootElement.selectNodes("select");
		for (Element element : select) {
			String id = element.attributeValue("id");
			String parameterType = element.attributeValue("parameterType");
			String resultType = element.attributeValue("resultType");
			
			// Class<?> parameterTypeClassType = getClassType(parameterType);
			// Class<?> resultTypeClassType = getClassType(resultType);
			
			//	statementId
			String key = namespace + "." + id;
			String sql = element.getTextTrim();
			
			//	封装mappedStatement
			MappedStatement mappedStatement = new MappedStatement();
			mappedStatement.setStatementId(key);
			mappedStatement.setResultType(resultType);
			mappedStatement.setParameterType(parameterType);
			mappedStatement.setSql(sql);
			mappedStatement.setSqlcommandType("select");
			
			// 将封装好的mappedStatement封装到configuration中的map集合中
			configuration.getMappedStatementMap().put(key, mappedStatement);
		}
		
		
	}
	
	private Class<?> getClassType(String parameter) throws ClassNotFoundException {
		return Class.forName(parameter);
	}
}
