package com.mrxia.testlib.service;

import java.util.Collection;

import com.mrxia.testlib.domain.Subject;
import com.mrxia.testlib.domain.TestPaper;

/**
 * 远程请求抓取服务
 *
 * @author xiazijian
 */
public interface RemoteService {

    /**
     * 用户登录
     *
     * @param userName 用户名
     * @param password 密码
     * @return 正大系统的sessionId
     */
    String login(String userName, String password);

    /**
     * 通过sessionId获取当前用户的可选科目
     *
     * @param sessionId 正大系统会话id
     * @return 可选科目集合
     */
    Collection<Subject> getSubjectList(String sessionId);

    /**
     * 通过试卷id返回对应的试卷信息
     *
     * @param paperId 试卷id
     * @return 包含所有考题和试卷信息的数据
     */
    TestPaper selectTestPaper(Integer paperId);
}
