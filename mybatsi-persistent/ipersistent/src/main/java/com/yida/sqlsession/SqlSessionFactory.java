package com.yida.sqlsession;

public interface SqlSessionFactory {
	/**
	 * 创建sqlSession:封装与数据库交互的方法
	 *
	 * @return
	 */
	SqlSession openSession();
}
