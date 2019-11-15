package com.internal.playment.inward.channel;

import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.internal.playment.inward.component.DbCommonRPCComponent;
import com.internal.playment.inward.config.SpringContextUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ChannelFacadeComponent implements NewPayAssert  {

    private final DbCommonRPCComponent dbCommonRPCComponent;

    /**
     *
     * @param payOrderInfoTable
     */
    public void ChannelRoutingByPayOrder(PayOrderInfoTable payOrderInfoTable){
        try{
            if(isNull(payOrderInfoTable))
                throw new Exception("收单异步查询，发现订单对象为空，无法处理！");

            OrganizationInfoTable organizationInfoTable = dbCommonRPCComponent.apiOrganizationInfoService.getOne(
                    new OrganizationInfoTable()
                            .setOrganizationId( payOrderInfoTable.getOrganizationId() ));

            String appClassObj = organizationInfoTable.getApplicationClassObj();
            String beanName = appClassObj.substring(appClassObj.lastIndexOf(".")+1,appClassObj.length());
            ChannelHandlePortComponent channelHandlePortComponent = SpringContextUtil.getBean(beanName);
            channelHandlePortComponent.asyncPayOderQuery(payOrderInfoTable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *
     * @param transOrderInfoTable
     */
    public  void ChannelRoutingByTransOrder(TransOrderInfoTable transOrderInfoTable){
        try{
            if(isNull(transOrderInfoTable))
                throw new Exception("代付异步查询，发现订单对象为空，无法处理！");
            OrganizationInfoTable organizationInfoTable = dbCommonRPCComponent.apiOrganizationInfoService.getOne(
                    new OrganizationInfoTable()
                            .setOrganizationId( transOrderInfoTable.getOrganizationId() ));
            String appClassObj = organizationInfoTable.getApplicationClassObj();
            String beanName = appClassObj.substring( appClassObj.lastIndexOf(".")+1,appClassObj.length() );
            ChannelHandlePortComponent channelHandlePortComponent = SpringContextUtil.getBean(beanName);
            channelHandlePortComponent.asyncTransOderQuery(transOrderInfoTable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
