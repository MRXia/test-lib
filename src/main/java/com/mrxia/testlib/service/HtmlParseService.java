package com.mrxia.testlib.service;

import java.util.List;

import com.mrxia.testlib.domain.Subject;

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
    List<Subject> parseSubjectList(String html);

    /**
     * 从html中解析tickId，以获取试卷信息
     *
     * @param html html内容
     * @return 解析出的tickId
     */
    Integer parseTickId(String html);
}
