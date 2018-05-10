package com.mrxia.testlib.beans.data;

import java.util.List;

import lombok.Data;

/**
 * 试题科目类
 * @author xiazijian
 */
@Data
public class Subject {

    /**
     * 科目名称
     */
    private String name;

    /**
     * 包含试卷
     */
    private List<TestPaper> testPapers;
}
