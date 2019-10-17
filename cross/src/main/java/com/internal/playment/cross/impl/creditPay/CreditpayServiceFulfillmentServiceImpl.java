package com.internal.playment.cross.impl.creditPay;

import com.alibaba.dubbo.config.annotation.Service;
import com.internal.playment.api.cross.creditpay.CreditpayServiceFulfillmentService;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/16
 * Time: 下午6:03
 * Description:
 */
@Service(version = "${application.version}")
public class CreditpayServiceFulfillmentServiceImpl implements CreditpayServiceFulfillmentService {
    @Override
    public String test() {
        return "CreditpayServiceFulfillmentServiceImpl";
    }
}
