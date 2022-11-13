package com.yida.dao;

import com.yida.pojo.User;

import java.util.List;

public interface IUserDao {
	List<User> findAll() throws Exception;
	
	User findByCondition(User user) throws Exception;
}
