package com.internal.playment.common.inner;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午2:44
 * Description:
 */

@Data
public class NewPayException extends  Exception{

    private String code;// 异常代码
    private String  innerPrintMsg;//内部打印信息
    private String responseMsg;//响应错误信息

    public NewPayException(String code, String innerPrintMsg, String responseMsg) {
        super(innerPrintMsg);
        this.code = code;
        this.innerPrintMsg = innerPrintMsg;
        this.responseMsg = responseMsg;
    }
}
