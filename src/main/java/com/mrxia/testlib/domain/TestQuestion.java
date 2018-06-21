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
    private Integer type;

    /**
     * 题目
     */
    @Column(columnDefinition = "text")
    private String title;

    @Column(name = "answer_a")
    private String answerA;

    @Column(name = "answer_b")
    private String answerB;

    @Column(name = "answer_c")
    private String answerC;

    @Column(name = "answer_d")
    private String answerD;

    /**
     * 正确答案
     */
    private String rightAnswer;

    /**
     * 得分
     */
    private Float score;

    /**
     * 扣分
     */
    private Float points;

    /**
     * 提示
     */
    @Column(length = 2048)
    private String tip;

    /*
     通过不同名的set方法反序列化远程json数据
    */

    @JsonProperty("tb_type")
    public void setTbType(Integer type) {
        this.type = type;
    }

    @JsonProperty("tb_timu")
    public void setTbTitle(String title) {
        this.title = title;
    }

    @JsonProperty("tb_q1")
    public void setTbQ1(String answerA) {
        this.answerA = answerA;
    }

    @JsonProperty("tb_q2")
    public void setTbQ2(String answerB) {
        this.answerB = answerB;
    }

    @JsonProperty("tb_q3")
    public void setTbQ3(String answerC) {
        this.answerC = answerC;
    }

    @JsonProperty("tb_q4")
    public void setTbQ4(String answerD) {
        this.answerD = answerD;
    }

    @JsonProperty("tb_v1")
    public void setTbv1(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    @JsonProperty("tb_fenshu")
    public void setTbFenshu(Float score) {
        this.score = score;
    }

    @JsonProperty("tb_koufen")
    public void setTbKoufen(Float points) {
        this.points = points;
    }

    @JsonProperty("tb_tishiinf")
    public void setTbTishiinf(String tip) {
        this.tip = tip;
    }
}
