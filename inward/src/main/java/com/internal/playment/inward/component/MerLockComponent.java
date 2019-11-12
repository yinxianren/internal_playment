package com.internal.playment.inward.component;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.inner.PayUtil;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class MerLockComponent implements NewPayAssert, PayUtil {

    private static ConcurrentHashMap<String,Object> concurrentHashMap =new ConcurrentHashMap<>();

    @Autowired
    private DbCommonRPCComponent dbCommonRPCComponent;

    synchronized public  Object getLock(String merId) throws Exception {
        if(isBlank(merId)) throw new Exception("参数merId为NULL");
        MerchantInfoTable merchantInfoTable = dbCommonRPCComponent.apiMerchantInfoService.getOne(
                new MerchantInfoTable()
                        .setMerchantId(merId)
                        .setStatus(StatusEnum._0.getStatus())
        );
        if(isNull(merchantInfoTable)) throw new Exception(format("商户号：%s,"));

        return null;
    }


}
