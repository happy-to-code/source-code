package com.yida.pojo;

import lombok.Data;

/**
 * @Auther: yida
 * @Date: 2022/11/12 23:24
 * @Description: 存放解析配置文件的内容
 *
 * <select id="selectOne" resultType="com.yida.pojo.User" parameterType="com.yida.pojo.User">
 * select * from user where id = #{id} and username = #{username}
 * </select>
 */
@Data
public class MappedStatement {
	/**
	 * 唯一标识   namespace.id
	 */
	private String statementId;
	/**
	 * 返回结果类型
	 */
	private String resultType;
	/**
	 * 参数类型
	 */
	private String parameterType;
	/**
	 * 要执行的sql
	 */
	private String sql;
	/**
	 * mapper代理    (select、update、delete……)
	 */
	private String sqlcommandType;
	
	
}
