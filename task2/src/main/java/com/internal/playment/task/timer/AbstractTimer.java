package com.internal.playment.task.timer;

import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.inner.PayUtil;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.task.component.ActiveMqOrderProducerComponent;
import com.internal.playment.task.component.DbCommonRPCComponent;
import org.springframework.beans.factory.annotation.Autowired;

public abstract  class AbstractTimer implements NewPayAssert , PayUtil {

    @Autowired
    protected DbCommonRPCComponent dbCommonRPCComponent;
    @Autowired
    protected ActiveMqOrderProducerComponent activeMqOrderProducerComponent;

    protected static final Tuple2<String,String>  RXH_INNER_CODE_M01 = new Tuple2("M01","查询了，但没未找到任务对象");
}
