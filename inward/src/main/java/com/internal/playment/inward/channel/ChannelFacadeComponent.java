package com.internal.playment.inward.channel;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.NewPayAssert;
import com.internal.playment.common.inner.PayUtil;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.internal.playment.inward.component.DbCommonRPCComponent;
import com.internal.playment.inward.config.SpringContextUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ChannelFacadeComponent implements NewPayAssert, PayUtil {

    private final DbCommonRPCComponent dbCommonRPCComponent;

    /**
     *
     * @param payOrderInfoTable
     */
    public void ChannelRoutingByPayOrder(PayOrderInfoTable payOrderInfoTable){
        try{
            if(isNull(payOrderInfoTable))
                throw new Exception("收单异步查询，发现订单对象为空，无法处理！");

            ChannelInfoTable channelInfoTable = dbCommonRPCComponent.apiChannelInfoService.getOne(new ChannelInfoTable()
                    .setChannelId(payOrderInfoTable.getChannelId())
                    .setStatus(StatusEnum._0.getStatus()));
            if(isNull(channelInfoTable))
                throw new Exception(format("收单异步查询，发现通道ID为%s,该对象在数据库中未能找到，无法处理！",payOrderInfoTable.getChannelId()));
            OrganizationInfoTable organizationInfoTable = dbCommonRPCComponent.apiOrganizationInfoService.getOne(
                    new OrganizationInfoTable()
                            .setOrganizationId( channelInfoTable.getOrganizationId() ));
            if(isNull(organizationInfoTable))
                throw new Exception(format("收单异步查询，发现组织机构ID为%s,该对象在数据库中未能找到，无法处理！",channelInfoTable.getOrganizationId()));
            String appClassObj = organizationInfoTable.getApplicationClassObj();
            String beanName = appClassObj.substring(appClassObj.lastIndexOf(".")+1,appClassObj.length());
            ChannelHandlePortComponent channelHandlePortComponent =  SpringContextUtil.getBean(toLowerCaseFirstOne(beanName));
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
            ChannelInfoTable channelInfoTable = dbCommonRPCComponent.apiChannelInfoService.getOne(new ChannelInfoTable()
                    .setChannelId(transOrderInfoTable.getChannelId())
                    .setStatus(StatusEnum._0.getStatus()));
            if(isNull(channelInfoTable))
                throw new Exception(format("代付异步查询，发现通道ID为%s,该对象在数据库中未能找到，无法处理！",transOrderInfoTable.getChannelId()));

            OrganizationInfoTable organizationInfoTable = dbCommonRPCComponent.apiOrganizationInfoService.getOne(
                    new OrganizationInfoTable()
                            .setOrganizationId( channelInfoTable.getOrganizationId() ));
            if(isNull(organizationInfoTable))
                throw new Exception(format("代付异步查询，发现组织机构ID为%s,该对象在数据库中未能找到，无法处理！",channelInfoTable.getOrganizationId()));
            String appClassObj = organizationInfoTable.getApplicationClassObj();
            String beanName = appClassObj.substring( appClassObj.lastIndexOf(".")+1,appClassObj.length() );
            ChannelHandlePortComponent channelHandlePortComponent = (ChannelHandlePortComponent) SpringContextUtil.getBean(toLowerCaseFirstOne(beanName));
            channelHandlePortComponent.asyncTransOderQuery(transOrderInfoTable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
