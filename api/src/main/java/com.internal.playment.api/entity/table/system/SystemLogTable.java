package com.internal.playment.api.entity.table.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/16
 * Time: 下午7:21
 * Description:
 */

@Data
@TableName("sys_log")
public class SystemLogTable {
    private Long id;
    private Integer type;
    private String operator;
    private Date startTime;
    private Long spendTime;
    private String requestIp;
    private String requestUri;
    private String methodName;
    private String methodDescription;
    private String message;
}
