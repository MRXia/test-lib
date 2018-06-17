package com.mrxia.testlib.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import com.mrxia.common.domain.AbstractJsr310Auditable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 试题科目类
 * @author xiazijian
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Subject extends AbstractJsr310Auditable<Integer> {

    /**
     * 科目名称
     */
    private String name;

    /**
     * 科目类型
     */
    private Integer type;

    /**
     * 包含试卷
     */
    @OneToMany(mappedBy = "subject", cascade = CascadeType.PERSIST)
    private List<TestPaper> testPapers;
}
