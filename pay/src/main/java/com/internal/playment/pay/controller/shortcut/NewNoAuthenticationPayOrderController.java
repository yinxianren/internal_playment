package com.internal.playment.pay.controller.shortcut;

import com.alibaba.fastjson.JSON;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.MerNoAuthPayOrderApplyDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.BusinessTypeEnum;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.channel.ChannelHistoryTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.merchant.MerchantQuotaRiskTable;
import com.internal.playment.common.table.system.MerchantSettingTable;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.internal.playment.common.table.system.RiskQuotaTable;
import com.internal.playment.common.table.system.SystemOrderTrackTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.common.tuple.Tuple4;
import com.internal.playment.pay.channel.AllinPayChannelHandlePortComponent;
import com.internal.playment.pay.channel.CommonChannelHandlePortComponent;
import com.internal.playment.pay.component.Md5Component;
import com.internal.playment.pay.config.SpringContextUtil;
import com.internal.playment.pay.controller.NewAbstractCommonController;
import com.internal.playment.pay.service.shortcut.NewPayOrderService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *  快捷免验证支付
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/5
 * Time: 下午2:25
 * Description:
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/shortcut")
public class NewNoAuthenticationPayOrderController  extends NewAbstractCommonController {

    private  final Md5Component md5Component;
    private  final NewPayOrderService newPayOrderService;
    private  final AllinPayChannelHandlePortComponent allinPayChannelHandlePortComponent;
    /**
     *  快捷免验证支付
     * @param request
     * @param param
     * @return
     */
    @PostMapping(value = "/exemptCodePay", produces = "text/html;charset=UTF-8")
    public String exemptCodePay(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【快捷免验证支付】";
        String errorMsg,errorCode,printErrorMsg,respResult=null;
        SystemOrderTrackTable sotTable = null;
        MerNoAuthPayOrderApplyDTO merNoAuthPayOrderApplyDTO = null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        PayOrderInfoTable payOrderInfoTable = null;
        try{
            //0.解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            merNoAuthPayOrderApplyDTO = JSON.parseObject(sotTable.getRequestMsg(), MerNoAuthPayOrderApplyDTO.class);
            sotTable.setMerId(merNoAuthPayOrderApplyDTO.getMerId())
                    .setAmount( new BigDecimal(merNoAuthPayOrderApplyDTO.getAmount()) )
                    .setMerOrderId(merNoAuthPayOrderApplyDTO.getMerOrderId())
                    .setReturnUrl(merNoAuthPayOrderApplyDTO.getReturnUrl())
                    .setNoticeUrl(merNoAuthPayOrderApplyDTO.getNoticeUrl());
            //创建日志打印对象
            ipo = new InnerPrintLogObject(merNoAuthPayOrderApplyDTO.getMerId(), merNoAuthPayOrderApplyDTO.getTerMerId(),bussType);
            //获取商户信息
            merInfoTable = newPayOrderService.getOneMerInfo(ipo);
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newPayOrderService.getParamMapByB10();
            //参数校验
            this.verify(paramRuleMap, merNoAuthPayOrderApplyDTO,ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //查看是否重复订单
            newPayOrderService.multipleOrder(merNoAuthPayOrderApplyDTO.getMerOrderId(),ipo);
            //判断产品类型
            newPayOrderService.checkProductTypeByB10(merNoAuthPayOrderApplyDTO,ipo);
            //1.执行平台风控
            //获取商户风控表
            MerchantQuotaRiskTable merchantQuotaRiskTable = newPayOrderService.getMerchantQuotaRiskByMerId(merInfoTable.getMerchantId(),ipo);
            //执行单笔风控
            newPayOrderService.checkSingleAmountRisk(merNoAuthPayOrderApplyDTO.getAmount(),merchantQuotaRiskTable,ipo);
            //获取风控交易量统计数据
            Tuple2<RiskQuotaTable,RiskQuotaTable> merRiskQuota = newPayOrderService.getRiskQuotaInfoByMer(merInfoTable,ipo);
            //执行风控控制
            newPayOrderService.executePlatformRisk(merNoAuthPayOrderApplyDTO.getAmount(),merchantQuotaRiskTable,merRiskQuota,ipo);
            //2.查询通道使用记录  MerchantId  TerminalMerId ProductId
            ChannelHistoryTable channelHistoryTable = newPayOrderService.getChannelHistoryInfo(ipo,merNoAuthPayOrderApplyDTO.getMerId(),merNoAuthPayOrderApplyDTO.getTerMerId(),merNoAuthPayOrderApplyDTO.getProductType());
            //通道信息
            ChannelInfoTable channelInfoTable;
            //进件信息
            RegisterCollectTable registerCollectTable;
            //绑卡信息
            MerchantCardTable merchantCardTable;
            //获取该通道历史统计交易量
            Tuple2<RiskQuotaTable,RiskQuotaTable> channelRiskQuota = null;
            //没有通道使用记录
            if(isNull(channelHistoryTable)){
                //根据商户配置信息,并且进行二次过滤，主要根据本次交易的产品类型
                List<MerchantSettingTable> merchantSettingTableList = newPayOrderService.getMerchantSetting(ipo);
                merchantSettingTableList = newPayOrderService
                        .filterMerchantSettingTableByProductType(merchantSettingTableList,merNoAuthPayOrderApplyDTO.getProductType(),ipo);
                //获取成功进件记录
                List<RegisterCollectTable> registerCollectTableList = newPayOrderService.getSuccessRegisterInfo(new RegisterCollectTable()
                        .setMerchantId(merNoAuthPayOrderApplyDTO.getMerId())
                        .setTerminalMerId(merNoAuthPayOrderApplyDTO.getTerMerId())
                        .setBussType(BusinessTypeEnum.b3.getBusiType())
                        .setStatus(StatusEnum._0.getStatus()),ipo);
                //根据配置通道信息过滤可用的进件信息
                registerCollectTableList = newPayOrderService
                        .filterRegCollectByMerSet(registerCollectTableList,merchantSettingTableList,ipo);
                //根据进件信息获取邦卡记录
                List<MerchantCardTable> merchantCardTableList = newPayOrderService
                        .getSuccessMerchantCardInfo(registerCollectTableList, ipo);
                //根据收单的信息过滤出绑卡信息
                merchantCardTableList= newPayOrderService.filterMerCardByPaymentMsg(
                        merchantCardTableList,
                        ipo,
                        merNoAuthPayOrderApplyDTO.getBankCardNum(),
                        merNoAuthPayOrderApplyDTO.getBankCardPhone());
                //根据进件信息和绑卡信息过滤进件信息,  merchantCardTableList,registerCollectTableList 有共同的组织机构
                registerCollectTableList  = newPayOrderService.filterRegCollectInfoByMerCard(registerCollectTableList,merchantCardTableList,ipo);
                //获取可行的通道
                List<ChannelInfoTable> channelInfoTableList = newPayOrderService.getAllUsableChannelList(registerCollectTableList,ipo,merNoAuthPayOrderApplyDTO.getProductType(), BusinessTypeEnum.PAY.getBusiType());
                //获取最终可用的通道 //merPayOrderApplyDTO.getAmount(),
                channelInfoTable = newPayOrderService.getFeasibleChannel(channelInfoTableList,ipo,merNoAuthPayOrderApplyDTO.getAmount());
                //确定进件信息
                registerCollectTable = newPayOrderService.finallyFilterRegCollect(channelInfoTable,registerCollectTableList,ipo);
                //确定绑卡信息
                merchantCardTable = newPayOrderService.finallyFilterMerCard(merchantCardTableList,ipo,merNoAuthPayOrderApplyDTO.getBankCardNum() ,merNoAuthPayOrderApplyDTO.getBankCardPhone());
            }else{
                //获取通道信息
                channelInfoTable = newPayOrderService.getChannelInfoByChannelHistory(channelHistoryTable,ipo);
                //根据商户配置信息
                List<MerchantSettingTable> merchantSettingTableList = newPayOrderService.getMerchantSetting(ipo);
                merchantSettingTableList = newPayOrderService
                        .filterMerchantSettingTableByProductType(merchantSettingTableList,merNoAuthPayOrderApplyDTO.getProductType(),ipo);
                //判断商户是否该通道,如果该channelInfoTable没在merchantSettingTableList列表中，则制空
                channelInfoTable = newPayOrderService.judgeThisChannelUsable(channelInfoTable,merchantSettingTableList);
                //备份一个通道信息
                ChannelInfoTable channelInfoTable_back = channelInfoTable;
                //执行通道风控
                if(!isNull(channelInfoTable)){
                    //获取该通道历史统计交易量
                    channelRiskQuota = newPayOrderService.getRiskQuotaInfoByChannel(channelInfoTable,ipo);
                    //执行通道风控
                    channelInfoTable = newPayOrderService.executeChannelRisk(channelInfoTable,channelRiskQuota,ipo,merNoAuthPayOrderApplyDTO.getAmount());
                }
                //如果该通道从商户配置中删除，则该通道为null,需要重新获取一次
                if(isNull(channelInfoTable)){
                    //获取成功进件记录
                    //获取成功进件记录
                    List<RegisterCollectTable> registerCollectTableList = newPayOrderService.getSuccessRegisterInfo(new RegisterCollectTable()
                            .setMerchantId(merNoAuthPayOrderApplyDTO.getMerId())
                            .setTerminalMerId(merNoAuthPayOrderApplyDTO.getTerMerId())
                            .setBussType(BusinessTypeEnum.b3.getBusiType())
                            .setStatus(StatusEnum._0.getStatus()),ipo);
                    //根据配置通道信息过滤可用的进件信息
                    registerCollectTableList = newPayOrderService.filterRegCollectByMerSet(registerCollectTableList,merchantSettingTableList,ipo);
                    //根据进件信息获取邦卡记录
                    List<MerchantCardTable> merchantCardTableList = newPayOrderService.getSuccessMerchantCardInfo(registerCollectTableList, ipo);
                    //根据收单的信息过滤出绑卡信息
                    merchantCardTableList= newPayOrderService.filterMerCardByPaymentMsg(merchantCardTableList,ipo,merNoAuthPayOrderApplyDTO.getBankCardNum(),merNoAuthPayOrderApplyDTO.getBankCardPhone());
                    //根据进件信息和绑卡信息过滤进件信息,  merchantCardTableList,registerCollectTableList 有共同的组织机构
                    registerCollectTableList  = newPayOrderService.filterRegCollectInfoByMerCard(registerCollectTableList,merchantCardTableList,ipo);
                    //获取可行的通道
                    List<ChannelInfoTable> channelInfoTableList = newPayOrderService.getAllUsableChannelList(registerCollectTableList,ipo,merNoAuthPayOrderApplyDTO.getProductType(),BusinessTypeEnum.PAY.getBusiType());
                    //去除前面备份的通道
                    channelInfoTableList = newPayOrderService.subtractUnableChanInfo(channelInfoTableList,channelInfoTable_back,ipo);
                    //获取最终可用的通道
                    channelInfoTable = newPayOrderService.getFeasibleChannel(channelInfoTableList,ipo,merNoAuthPayOrderApplyDTO.getAmount());
                    //确定进件信息
                    registerCollectTable = newPayOrderService.finallyFilterRegCollect(channelInfoTable,registerCollectTableList,ipo);
                    //确定绑卡信息
                    merchantCardTable = newPayOrderService.finallyFilterMerCard(merchantCardTableList,ipo,merNoAuthPayOrderApplyDTO.getBankCardNum() ,merNoAuthPayOrderApplyDTO.getBankCardPhone());
                }else {
                    //获取进件信息
                    registerCollectTable = newPayOrderService.getSuccessRegInfoByChanInfo(channelInfoTable, ipo);
                    //获取绑卡信息
                    merchantCardTable = newPayOrderService.getMerCardByChanAndReg(channelInfoTable, registerCollectTable,ipo,merNoAuthPayOrderApplyDTO.getBankCardNum(),merNoAuthPayOrderApplyDTO.getBankCardPhone());
                }
            }
            //3.保存订单信息
            payOrderInfoTable = newPayOrderService.savePayOrderByNoAuth(merInfoTable, merNoAuthPayOrderApplyDTO,channelInfoTable,registerCollectTable,merchantCardTable,ipo);
            sotTable.setPlatformOrderId(payOrderInfoTable.getPlatformOrderId());
            //获取组织机构信息
            OrganizationInfoTable organizationInfoTable = newPayOrderService.getOrganizationInfo(channelInfoTable.getOrganizationId(),ipo);
            Class  clz=Class.forName(organizationInfoTable.getApplicationClassObj().trim());
            //生成通道处理对象
            CommonChannelHandlePortComponent commonChannelHandlePortComponent = (CommonChannelHandlePortComponent) SpringContextUtil.getBean(clz);
            //封装请求cross必要参数
            requestCrossMsgDTO = newPayOrderService.getRequestCrossMsgDTO(new Tuple4(channelInfoTable,payOrderInfoTable,registerCollectTable,merchantCardTable));
            requestCrossMsgDTO.setIP(sotTable.getIp());
            //调用业务申请
            crossResponseMsgDTO = allinPayChannelHandlePortComponent.exemptCodePay(requestCrossMsgDTO,ipo);
            String crossResponseMsg = null == crossResponseMsgDTO ? null : crossResponseMsgDTO.toString();
            //更新订单信息
            payOrderInfoTable = newPayOrderService.updateByPayOrderInfoByB9After(crossResponseMsgDTO,crossResponseMsg,payOrderInfoTable,ipo);
            //状态为成功是才执行以下操作
            if(crossResponseMsgDTO.getCrossStatusCode().equals(StatusEnum._0.getStatus())){
                /**
                 * 事务处理
                 */
                //更新通道历史使用记录 8_channel_history_table
                ChannelHistoryTable  cht = newPayOrderService.updateByChannelHistoryInfo(channelHistoryTable,payOrderInfoTable);
                if(isNull(channelRiskQuota))
                    channelRiskQuota = newPayOrderService.getRiskQuotaInfoByChannel(channelInfoTable,ipo);
                //更新 商户和通道使用汇总情况 8_risk_quota_table
                Set<RiskQuotaTable> rqtSet = newPayOrderService.updateByRiskQuotaInfo(payOrderInfoTable,merRiskQuota,channelRiskQuota);
                //执行事务更新操作
                newPayOrderService.batchUpdatePayOderCorrelationInfo(payOrderInfoTable,cht,rqtSet,ipo);
            }
            //通道差异化处理
            commonChannelHandlePortComponent.channelDifferBusinessHandleByPayOrder(requestCrossMsgDTO,crossResponseMsgDTO);
            //crossResponseMsgDTO 状态码非成功则抛出异常
            newPayOrderService.isSuccess(crossResponseMsgDTO,ipo);
            //封装放回结果  // merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg,channelTab
            respResult = newPayOrderService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,merNoAuthPayOrderApplyDTO.getMerOrderId(),payOrderInfoTable.getPlatformOrderId(),merNoAuthPayOrderApplyDTO.getAmount(),null,null,channelInfoTable.getChannelId());
            sotTable.setPlatformPrintLog(StatusEnum.remark(crossResponseMsgDTO.getCrossStatusCode()))
                    .setTradeCode( crossResponseMsgDTO.getCrossStatusCode() );
        }catch (Exception e){
            if(e instanceof NewPayException){
                NewPayException npe = (NewPayException) e;
                errorMsg = npe.getResponseMsg();
                printErrorMsg = npe.getInnerPrintMsg();
                errorCode = npe.getCode();
            }else{
                e.printStackTrace();
                errorMsg = ResponseCodeEnum.RXH99999.getMsg();
                printErrorMsg = isBlank(e.getMessage()) ? "" : (e.getMessage().length()>=512 ? e.getMessage().substring(0,526) : e.getMessage());
                errorCode = ResponseCodeEnum.RXH99999.getCode();
            }
            // merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg
            respResult = newPayOrderService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,
                    null != merNoAuthPayOrderApplyDTO ? merNoAuthPayOrderApplyDTO.getMerOrderId() : null, null != payOrderInfoTable ? payOrderInfoTable.getPlatformOrderId(): null,null,errorCode,errorMsg,null);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newPayOrderService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }




}
