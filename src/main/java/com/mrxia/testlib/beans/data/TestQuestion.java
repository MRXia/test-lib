package com.mrxia.testlib.beans.data;

import lombok.Data;

/**
 * 考试试题
 *
 * @author xiazijian
 */
@Data
public class TestQuestion {

    private Integer id;

    private Integer type;

    /**
     * 题目
     */
    private String title;

    private String answerA;

    private String answerB;

    private String answerC;

    private String answerD;

    /**
     * 正确答案
     */
    private String rightAnswer;

    /**
     * 得分
     */
    private Integer score;

    /**
     * 扣分
     */
    private Integer points;

    /**
     * 提示
     */
    private String tip;
}
