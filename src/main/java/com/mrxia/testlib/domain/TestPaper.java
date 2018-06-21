package com.mrxia.testlib.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

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
    @JsonIgnore
    private Subject subject;

    private Integer testTime;

    @CreatedDate
    private LocalDateTime createTime;

    @LastModifiedDate
    private LocalDateTime updateTime;


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

    /*
     通过不同名的set方法反序列化远程json数据
    */

    @JsonProperty("tb_time")
    public void setTbTime(Integer testTime) {
        this.testTime = testTime;
    }

    @JsonProperty("data")
    public void setData(List<TestQuestion> questions) {
        this.questions = questions;
    }
}
