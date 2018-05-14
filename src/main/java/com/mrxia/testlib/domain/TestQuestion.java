package com.mrxia.testlib.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.mrxia.common.domain.AbstractJsr310Auditable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 考试试题
 *
 * @author xiazijian
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class TestQuestion extends AbstractJsr310Auditable<Integer> {

    @ManyToOne
    @JoinColumn
    private TestPaper testPaper;

    /**
     * 题目类型0：单选，1：多选
     */
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
