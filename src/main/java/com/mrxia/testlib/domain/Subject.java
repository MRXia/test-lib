package com.mrxia.testlib.domain;

import java.util.List;

import javax.persistence.Entity;
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
     * 包含试卷
     */
    @OneToMany(mappedBy = "subject")
    private List<TestPaper> testPapers;
}
