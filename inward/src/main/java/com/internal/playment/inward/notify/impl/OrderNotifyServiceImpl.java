package com.internal.playment.inward.notify.impl;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.HttpClientUtils;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.system.AsyncNotifyTable;
import com.internal.playment.inward.notify.NotifyCommonServiceAbstract;
import com.internal.playment.inward.notify.OrderNotifyService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderNotifyServiceImpl extends NotifyCommonServiceAbstract implements OrderNotifyService {

    @Override
    public void orderNotify(AsyncNotifyTable asyncNotifyTable) {
        try {
            //获取用户信息
            MerchantInfoTable merchantInfoTable = dbCommonRPCComponent.apiMerchantInfoService.getOne(
                    new MerchantInfoTable()
                            .setMerchantId(asyncNotifyTable.getMerchantId()));
            if (isNull(merchantInfoTable))
                new Exception(format("【收单异步主动通联查询】-->主动通知下游商户：%s 时，获取商户信息为空", asyncNotifyTable.getMerchantId()));
            //封装异步通知对象
            Map<String, Object> map = packageAsyncNotifyMapObj(asyncNotifyTable, merchantInfoTable, StatusEnum._0.getRemark());
            //发起异步通知
            String content = HttpClientUtils.doPost(HttpClientUtils.getHttpsClient(), asyncNotifyTable.getNotifyUrl().trim(), map);
            //判断 如果成功则结束通知
            if( !isBlank(content) && content.trim().equalsIgnoreCase("SUCCESS")){
                asyncNotifyTable.setStatus(StatusEnum._0.getStatus());//结束通知
                dbCommonRPCComponent.apiAsyncNotifyService.updateByKey(asyncNotifyTable);
                return;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
