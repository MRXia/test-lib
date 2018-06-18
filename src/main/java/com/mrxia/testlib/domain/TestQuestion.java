package com.mrxia.testlib.domain;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne(cascade = CascadeType.MERGE)
    @JsonIgnore
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
    @Column(columnDefinition = "text")
    private String title;

    @JsonProperty("tb_q1")
    @Column(name = "answer_a")
    private String answerA;

    @JsonProperty("tb_q2")
    @Column(name = "answer_b")
    private String answerB;

    @JsonProperty("tb_q3")
    @Column(name = "answer_c")
    private String answerC;

    @JsonProperty("tb_q4")
    @Column(name = "answer_d")
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
    private Float points;

    /**
     * 提示
     */
    @JsonProperty("tb_tishiinf")
    @Column(length = 2048)
    private String tip;
}
