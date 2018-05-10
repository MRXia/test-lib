package com.mrxia.testlib.service;

import java.util.Collection;

import com.mrxia.testlib.beans.data.Subject;

/**
 * 通过Jsoup解析html的服务
 *
 * @author xiazijian
 */
public interface HtmlParseService {

    /**
     * 解析科目列表
     *
     * @param html html内容
     * @return 包含试卷信息的科目列表
     */
    Collection<Subject> parseSubjectList(String html);

}
