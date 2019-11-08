package com.internal.playment.cross.impl.yacolpay;

import com.alibaba.dubbo.config.annotation.Service;
import com.internal.playment.api.cross.yacolpay.ApiYaColPayOtherBusinessCrossComponent;
import lombok.extern.slf4j.Slf4j;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/7
 * Time: 上午11:37
 * Description:
 */
@Slf4j
@Service(version = "${application.version}", group = "yaColPay"  , timeout = 30000 )
public class ApiYaColPayOtherBusinessCrossComponentImpl implements ApiYaColPayOtherBusinessCrossComponent {
}
