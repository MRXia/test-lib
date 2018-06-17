package com.mrxia.testlib.bean.request;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * 开始考试请求
 * @author xiazijian
 */
@Data
public class StartTestRequest {

    @NotNull
    private Integer subjectId;

    @NotNull
    private Integer pagerId;
}
