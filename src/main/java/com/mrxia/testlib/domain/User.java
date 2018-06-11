package com.mrxia.testlib.domain;

import javax.persistence.Entity;
import javax.persistence.Transient;

import com.mrxia.common.domain.AbstractJsr310Auditable;

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
public class User extends AbstractJsr310Auditable<Integer> {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 账户所有的科目类型
     */
    private Integer subjectType;

    @Transient
    private String zhengdaSessionId;
}

