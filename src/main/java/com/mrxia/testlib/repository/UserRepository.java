package com.mrxia.testlib.repository;

import org.springframework.data.repository.CrudRepository;

import com.mrxia.testlib.domain.User;

/**
 * 用户对象数据库持久类
 * @author xiazijian
 */
public interface UserRepository extends CrudRepository<User, Integer> {

    /**
     * 通过用户名查找用户对象
     * @param username 用户名
     * @return 用户对象
     */
    User findUserByUsername(String username);
}
