package com.mrxia.testlib.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mrxia.testlib.domain.Subject;
import com.mrxia.testlib.domain.TestPaper;
import com.mrxia.testlib.domain.User;

/**
 * 题库操作相关服务
 *
 * @author xiazijian
 */
public interface TestLibService {

    /**
     * <h2>用户登录操作</h2>
     * <p>
     * 首先检查数据库中是否包含该用户<br/>
     * 如果不存在，则通过用户名密码登录正大题库 <br/>
     * 若登录成功，则记录该用户信息<br/>
     * </p>
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回登录成功的用户对象
     */
    User login(String username, String password);


    /**
     * <h2>返回可选的科目列表</h2>
     * 先通过数据库查找当前用户的科目列表，如果不存在，则查找正大题库并记录信息
     * @param user 登录的用户信息
     * @return 科目列表
     */
    List<Subject> listSubject(User user);
}
