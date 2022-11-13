package com.yida.pojo;

import lombok.Data;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: yida
 * @Date: 2022/11/12 23:20
 * @Description: 存放核心配置文件解析的内容
 */
@Data
public class Configuration {
	/**
	 * 数据源对象
	 */
	private DataSource dataSource;
	
	private Map<String, MappedStatement> mappedStatementMap = new HashMap<>(16);
	
}
