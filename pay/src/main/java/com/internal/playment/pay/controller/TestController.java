package com.internal.playment.pay.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.internal.playment.api.cross.ServiceFulfillmentService;
import com.internal.playment.api.cross.allinpay.AllinpayServiceFulfillmentService;
import com.internal.playment.api.cross.creditpay.CreditpayServiceFulfillmentService;
import com.internal.playment.api.cross.sicpay.SicpayServiceFulfillmentService;
import com.internal.playment.api.db.assist.AssistSystemLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/16
 * Time: 下午6:07
 * Description:
 */
@RestController
@RequestMapping("/pay")
public class TestController {

    @Reference(version = "${application.version}")
    private  AllinpayServiceFulfillmentService allinpayServiceFulfillmentService;

    @Reference(version = "${application.version}")
    private CreditpayServiceFulfillmentService creditpayServiceFulfillmentService;

    @Reference(version = "${application.version}")
    private SicpayServiceFulfillmentService sicpayServiceFulfillmentService;

    @Reference(version = "${application.version}")
    private AssistSystemLogService assistSystemLogService;

    @GetMapping("/test1")
    public String test1(){
        return allinpayServiceFulfillmentService.test();
    }

    @GetMapping("/test2")
    public String test2(){
        return creditpayServiceFulfillmentService.test();
    }

    @GetMapping("/test3")
    public String test3(){
        return sicpayServiceFulfillmentService.test();
    }


    @GetMapping("/test5")
    public String test5(){
        return sicpayServiceFulfillmentService.test()+assistSystemLogService.test();
    }

}
