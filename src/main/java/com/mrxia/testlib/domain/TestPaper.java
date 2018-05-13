package com.mrxia.testlib.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.AbstractAuditable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrxia.common.domain.AbstractJsr310Auditable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 试题试卷类
 * @author xiazijian
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class TestPaper extends AbstractJsr310Auditable<Integer> {

    /**
     * 试卷名称
     */
    private String name;

    @ManyToOne
    @JoinColumn
    private Subject subject;

    @JsonProperty("tb_time")
    private Integer testTime;

    @OneToMany(mappedBy = "testPaper")
    private List<TestQuestion> questions;

    public void paperId(Integer id) {
        this.setId(id);
    }
}
