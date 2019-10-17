package com.internal.playment.api.tools;


import lombok.Getter;

@Getter
public enum ResponseCodeEnum {

    YKTC000000("YKTC000000","请求参数为空");

    private String code;
    private String msg;

    ResponseCodeEnum(String code,String msg){
        this.code = code;
        this.msg = msg;
    }
}
