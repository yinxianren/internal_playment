package com.internal.playment.api.entity.enums;


import lombok.Getter;

@Getter
public enum ResponseCodeEnum {

     SUCCESS("SUCCESS","SUCCESS")
    ,YKTC000000("YKTC-000000","请求参数为空")
    ,YKTC000001("YKTC-000001","缺少必要参数")
    ,YKTC000002("YKTC-000002","字段值长度异常")
    ,YKTC000003("YKTC-000003","字段值格式错误")

    ;
    private String code;
    private String msg;

    ResponseCodeEnum(String code,String msg){
        this.code = code;
        this.msg = msg;
    }
}
