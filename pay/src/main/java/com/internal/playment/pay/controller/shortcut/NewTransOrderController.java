package com.internal.playment.pay.controller.shortcut;

import com.alibaba.dubbo.common.json.JSON;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.MerTransOrderApplyDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.BusinessTypeEnum;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.business.*;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.internal.playment.common.table.system.SystemOrderTrackTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.common.tuple.Tuple5;
import com.internal.playment.pay.channel.CommonChannelHandlePortComponent;
import com.internal.playment.pay.component.Md5Component;
import com.internal.playment.pay.config.SpringContextUtil;
import com.internal.playment.pay.controller.NewAbstractCommonController;
import com.internal.playment.pay.service.shortcut.NewTransOrderService;
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

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/11/5
 * Time: 下午2:19
 * Description:
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/shortcut")
public class NewTransOrderController extends NewAbstractCommonController {

    private final NewTransOrderService newTransOrderService;
    private final Md5Component md5Component;

    /**
     *
     * @param request
     * @param param
     * @return
     *
     *   未完善部分：
     *          1.判断商户钱包是否有足够的余额进行代付
     *          2.商户锁
     *          3.子商户锁
     *
     */
    @PostMapping(value = "/payment", produces = "text/html;charset=UTF-8")
    public String payment(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【快捷付款】";
        String errorMsg,errorCode,printErrorMsg,respResult=null;
        SystemOrderTrackTable sotTable = null;
        MerTransOrderApplyDTO merTransOrderApplyDTO=null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        TransOrderInfoTable transOrderInfoTable=null;
        try{
            //0.解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            merTransOrderApplyDTO = JSON.parse(sotTable.getRequestMsg(), MerTransOrderApplyDTO.class);
            sotTable.setMerId(merTransOrderApplyDTO.getMerId()).setMerOrderId(merTransOrderApplyDTO.getMerOrderId()).setReturnUrl(merTransOrderApplyDTO.getReturnUrl()).setNoticeUrl(merTransOrderApplyDTO.getNoticeUrl());
            //创建日志打印对象
            ipo = new InnerPrintLogObject(merTransOrderApplyDTO.getMerId(), merTransOrderApplyDTO.getTerMerId(),bussType);
            //获取商户信息
            merInfoTable = newTransOrderService.getOneMerInfo(ipo);
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newTransOrderService.getParamMapByB11();
            //参数校验
            this.verify(paramRuleMap, merTransOrderApplyDTO,ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //查看是否重复订单
            newTransOrderService.multipleOrder(merTransOrderApplyDTO.getMerOrderId(),ipo);
            //验证产品类型
            newTransOrderService.checkProductTypeByB11(merTransOrderApplyDTO,ipo);
            //-----------------------------------------开始加锁-->子商户锁-->保证收单订单多次获取
            //查询支付订单是否存在
            List<PayOrderInfoTable> payOrderInfoTableList = newTransOrderService.getPayOrderInfoByList(merTransOrderApplyDTO,ipo);
            //验证订单金额
            BigDecimal totalAmount = newTransOrderService.verifyOrderAmount(payOrderInfoTableList,merTransOrderApplyDTO,ipo);
            //多通道多代付,目前系统不开发这类功能，-->订单分类，根据不同通道机构Id进行划分
            // Map<OrganizationInfoTable,  List<PayOrderInfoTable> >  organizationOrderList = newTransOrderService.groupPayOrderByOrganization(payOrderInfoTableList,ipo);
            //查看订单是否是同一个通道
            Tuple2<OrganizationInfoTable, ChannelInfoTable> tuple2 =  newTransOrderService.verifyOrderChannelTab(payOrderInfoTableList,merTransOrderApplyDTO,ipo);
            //获取进件副表信息
            RegisterCollectTable registerCollectTable = newTransOrderService.getRegCollectInfo(payOrderInfoTableList.get(0).getRegPlatformOrderId(), BusinessTypeEnum.b3.getBusiType(),ipo);
            //获取进件主表信息
            RegisterInfoTable registerInfoTable = newTransOrderService.getRegisterInfoTable(registerCollectTable.getRitId(),ipo);
            //获取绑卡信息
            MerchantCardTable merchantCardTable = newTransOrderService.getMerchantCardInfoByPlatformOrderId(payOrderInfoTableList.get(0).getCardPlatformOrderId(),BusinessTypeEnum.b6.getBusiType(),ipo);
            //保存代付订单
            transOrderInfoTable = newTransOrderService.saveOrder(merTransOrderApplyDTO,tuple2._2,merInfoTable,ipo);
            Class  clz=Class.forName(tuple2._1.getApplicationClassObj().trim());
            //生成通道处理对象
            CommonChannelHandlePortComponent commonChannelHandlePortComponent = (CommonChannelHandlePortComponent) SpringContextUtil.getBean(clz);
            //封装请求cross必要参数   TransOrderInfoTable,ChannelInfoTable,RegisterCollectTable,RegisterInfoTable,MerchantCardTable
            requestCrossMsgDTO = newTransOrderService.getRequestCrossMsgDTO(new Tuple5(transOrderInfoTable,tuple2._2,registerCollectTable,registerInfoTable,merchantCardTable));
            requestCrossMsgDTO.setIP(sotTable.getIp());
            //调用业务申请
            crossResponseMsgDTO = commonChannelHandlePortComponent.payment(requestCrossMsgDTO,ipo);
            String crossResponseMsg = null == crossResponseMsgDTO ? null : crossResponseMsgDTO.toString();
            //更新订单信息
            transOrderInfoTable = newTransOrderService.updateOrder(transOrderInfoTable,crossResponseMsg,crossResponseMsgDTO,ipo);
            //状态为成功是才执行以下操作
            if(crossResponseMsgDTO.getCrossStatusCode().equals(StatusEnum._0.getStatus())){
                /**
                 * 事务处理
                 */
                //修改收单订单状体为 -->代付处理中,移除不必要的数据
                payOrderInfoTableList =  newTransOrderService.updatePayOrderStatus(payOrderInfoTableList);
                //执行事务
                newTransOrderService.batchUpdateTransOderCorrelationInfo(transOrderInfoTable,payOrderInfoTableList,ipo);
            }
            //通道差异化处理
            commonChannelHandlePortComponent.channelDifferBusinessHandleByTransOrder(requestCrossMsgDTO,crossResponseMsgDTO);
            //crossResponseMsgDTO 状态码非成功则抛出异常
            newTransOrderService.isSuccess(crossResponseMsgDTO,ipo);
            //封装放回结果  // merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg,channelTab
            respResult = newTransOrderService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,merTransOrderApplyDTO.getMerOrderId(),transOrderInfoTable.getPlatformOrderId(),merTransOrderApplyDTO.getAmount(),null,null);
            sotTable.setPlatformPrintLog(StatusEnum.remark(crossResponseMsgDTO.getCrossStatusCode())).setTradeCode( crossResponseMsgDTO.getCrossStatusCode() );
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
            respResult = newTransOrderService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,
                    null != merTransOrderApplyDTO ? merTransOrderApplyDTO.getMerOrderId() : null, null != transOrderInfoTable ? transOrderInfoTable.getPlatformOrderId(): null,null,errorCode,errorMsg);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newTransOrderService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }
}
