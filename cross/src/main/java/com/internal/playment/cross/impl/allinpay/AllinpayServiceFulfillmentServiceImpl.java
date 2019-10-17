package com.internal.playment.cross.impl.allinpay;

import com.alibaba.dubbo.config.annotation.Service;
import com.internal.playment.api.cross.allinpay.AllinpayServiceFulfillmentService;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/16
 * Time: 下午6:02
 * Description:
 */
@Service(version = "${application.version}")
public class AllinpayServiceFulfillmentServiceImpl implements AllinpayServiceFulfillmentService {

    @Override
    public String test() {
        return "AllinpayServiceFulfillmentServiceImpl";
    }
}
