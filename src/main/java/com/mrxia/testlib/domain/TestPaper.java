package com.mrxia.testlib.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 试题试卷类
 * @author xiazijian
 */
@Data
public class TestPaper {

    /**
     * 试卷id
     */
    private Integer id;

    /**
     * 试卷名称
     */
    private String name;

    @JsonProperty("tb_time")
    private Integer testTime;

    private List<TestQuestion> questions;
}
