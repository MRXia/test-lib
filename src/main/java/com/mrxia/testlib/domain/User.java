package com.mrxia.testlib.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.AbstractPersistable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * 用户对象实体
 * @author xiazijian
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class User extends AbstractPersistable<Integer> {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
}

