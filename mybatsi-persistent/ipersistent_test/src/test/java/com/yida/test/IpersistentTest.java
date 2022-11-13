package com.yida.test;

import com.yida.io.Resources;
import com.yida.pojo.User;
import com.yida.sqlsession.SqlSession;
import com.yida.sqlsession.SqlSessionFactory;
import com.yida.sqlsession.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * @Auther: yida
 * @Date: 2022/11/13 01:41
 * @Description:
 */
public class IpersistentTest {
	@Test
	public void test() throws Exception {
		// 1.根据配置文件的路径，加载成字节输入流，存到内存中
		InputStream resourceAsStream = Resources.getResourceAsStream("sqlMapConfig.xml");
		// 2.解析了配置文件，封装了Configuration对象  2.创建sqlSessionFactory工厂对象
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
		// 3.生产sqlSession 创建了执行器对象
		SqlSession sqlSession = sqlSessionFactory.openSession();
		
		User u = new User();
		u.setId(1);
		u.setUsername("xm");
		// 4.调用sqlSession方法
		User user = sqlSession.selectOne("User.selectOne", u);
		System.out.println("user = " + user);
		
		System.out.println("--------------");
		List<User> list = sqlSession.selectList("User.selectList", null);
		for (User user1 : list) {
			System.out.println(user1);
		}
	}
	
}
