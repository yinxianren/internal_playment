package com.internal.playment.common.enums;

import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 上午11:01
 * Description:
 */
@Getter
public enum  StatusEnum {
    _0(0,"成功"),//对应数据库：开启，启用
    _1(1,"失败"),//对应数据库：禁用，未启用
    _2(2,"未处理"),
    _3(3,"处理中"),
    _4(4,"等待短信确认"),
    _5(5,"无效，已使用过/过期/超时"),
    _6(6,"银行卡已经解绑"),
    //以下所有状态，原订单都是成功的
    _7(7,"等待队列处理中"),//内部使用，不对外暴露该状态
    _8(8,"队列处理异常"),//内部使用，不对外暴露该状态
    _9(9,"代付处理中"),
    _10(10,"全额代付"),//订单状态是成功，且已经全额代付
    _11(11,"部分代付"),//订单状态是成功，且部分代付代付
    _12(12,"收单对冲"),
    _13(13,"代付对冲")
    ;

    private int status;
    private String remark;

    StatusEnum(int status,String remark){
        this.status = status ;
        this.remark = remark;
    }

    public static String  remark(int status){
        for(StatusEnum statusEnum : StatusEnum.values()){
            if(statusEnum.getStatus() == status){
                return  statusEnum.getRemark();
            }
        }
        return "该状态码不存在";
    }
}
