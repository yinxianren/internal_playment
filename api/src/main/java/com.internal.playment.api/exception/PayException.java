package com.internal.playment.api.exception;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/17
 * Time: 上午10:40
 * Description:
 */
@Data
public class PayException extends Exception {
    private String code;// 异常代码
    private String  innerPrintMsg;//内部打印信息
    private String responseMsg;//响应错误信息

    public PayException(String code, String innerPrintMsg, String responseMsg) {
        super(innerPrintMsg);
        this.code = code;
        this.innerPrintMsg = innerPrintMsg;
        this.responseMsg = responseMsg;
    }
}
