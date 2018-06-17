package com.mrxia.testlib.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    private TestPaper testPaper;

    /**
     * 题目类型0：单选，1：多选
     */
    @JsonProperty("tb_type")
    private Integer type;

    /**
     * 题目
     */
    @JsonProperty("tb_timu")
    private String title;

    @JsonProperty("tb_q1")
    private String answerA;

    @JsonProperty("tb_q2")
    private String answerB;

    @JsonProperty("tb_q3")
    private String answerC;

    @JsonProperty("tb_q4")
    private String answerD;

    /**
     * 正确答案
     */
    @JsonProperty("tb_v1")
    private String rightAnswer;

    /**
     * 得分
     */
    @JsonProperty("tb_fenshu")
    private Float score;

    /**
     * 扣分
     */
    @JsonProperty("tb_koufen")
    private Integer points;

    /**
     * 提示
     */
    @JsonProperty("tb_tishiinf")
    private String tip;

}
