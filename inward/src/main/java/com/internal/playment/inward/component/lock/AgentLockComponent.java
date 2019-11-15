//package com.internal.playment.inward.component;
//
//import com.internal.playment.common.enums.StatusEnum;
//import com.internal.playment.common.inner.NewPayAssert;
//import com.internal.playment.common.inner.PayUtil;
//import com.internal.playment.common.table.agent.AgentMerchantInfoTable;
//import com.internal.playment.common.table.merchant.MerchantInfoTable;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class AgentLockComponent implements NewPayAssert, PayUtil {
//
//    private static ConcurrentHashMap<String,Object> concurrentHashMap =new ConcurrentHashMap<>();
//
//    @Autowired
//    private DbCommonRPCComponent dbCommonRPCComponent;
//
//    synchronized public  Object getLock(String agentId) throws Exception {
//        if(isBlank(agentId)) throw new Exception("参数merId为NULL！");
//
//        AgentMerchantInfoTable agentMerchantInfoTable =dbCommonRPCComponent.apiAgentMerchantInfoService.getOne(
//                new AgentMerchantInfoTable()
//                        .setAgentMerchantId(agentId)
//                        .setStatus(StatusEnum._0.getStatus())
//        );
//
//        if(isNull(agentMerchantInfoTable)) throw new Exception(format("代理商号：%s,此时商号不存在！",agentId));
//        Object object = concurrentHashMap.get(agentMerchantInfoTable.getAgentMerchantId());
//        if(isNull(object)){
//            object = new Object();
//            concurrentHashMap.put(agentMerchantInfoTable.getAgentMerchantId(),object);
//        }
//        return object;
//    }
//
//
//}
