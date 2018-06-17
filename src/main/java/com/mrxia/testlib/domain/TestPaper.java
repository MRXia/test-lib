package com.mrxia.testlib.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mrxia.common.domain.AbstractJsr310Auditable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 试题试卷类
 * @author xiazijian
 */
@Data
@Entity
@EntityListeners({AuditingEntityListener.class})
public class TestPaper implements Persistable<Integer> {

    @Id
    private Integer id;

    /**
     * 试卷名称
     */
    private String name;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Subject subject;

    @JsonProperty("tb_time")
    private Integer testTime;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedDate
    private LocalDateTime updateTime;

    @JsonProperty("data")
    @OneToMany(mappedBy = "testPaper", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<TestQuestion> questions;

    @Override
    @Transient
    public boolean isNew() {
        return null == this.getId();
    }

    @Override
    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), this.getId());
    }
}
