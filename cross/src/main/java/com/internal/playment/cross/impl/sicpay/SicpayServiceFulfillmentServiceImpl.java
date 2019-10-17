package com.internal.playment.cross.impl.sicpay;

import com.alibaba.dubbo.config.annotation.Service;
import com.internal.playment.api.cross.sicpay.SicpayServiceFulfillmentService;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/16
 * Time: 下午6:04
 * Description:
 */
@Service(version = "${application.version}")
public class SicpayServiceFulfillmentServiceImpl implements SicpayServiceFulfillmentService {
    @Override
    public String test() {
        return "SicpayServiceFulfillmentServiceImpl";
    }
}
