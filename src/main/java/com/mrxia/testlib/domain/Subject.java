package com.mrxia.testlib.domain;

import java.util.List;

import javax.persistence.Entity;

import org.springframework.data.jpa.domain.AbstractAuditable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 试题科目类
 * @author xiazijian
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Subject extends AbstractAuditable<User, Integer> {

    /**
     * 科目名称
     */
    private String name;

    /**
     * 包含试卷
     */
    private List<TestPaper> testPapers;
}
