package com.internal.playment.pay.controller.shortcut;


import com.alibaba.dubbo.common.json.JSON;
import com.internal.playment.common.dto.BusinessUnBondCardDTO;
import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.MerTransOrderApplyDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.BusinessTypeEnum;
import com.internal.playment.common.enums.BussTypeEnum;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.internal.playment.common.table.system.SystemOrderTrackTable;
import com.internal.playment.common.tuple.Tuple3;
import com.internal.playment.pay.channel.CommonChannelHandlePortComponent;
import com.internal.playment.pay.component.Md5Component;
import com.internal.playment.pay.config.SpringContextUtil;
import com.internal.playment.pay.controller.NewAbstractCommonController;
import com.internal.playment.pay.service.shortcut.NewUnBondCardService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/shortcut")
public class NewUnBondCardController extends NewAbstractCommonController {

    private final NewUnBondCardService newUnBondCardService;
    private final Md5Component md5Component;

    /**
     *解绑消费银行卡
     * @return
     */
    @PostMapping(value = "/unBondCard", produces = "text/html;charset=UTF-8")
    public String unBondCard(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【解绑消费银行卡】";
        String errorMsg,errorCode,printErrorMsg,respResult=null;
        BusinessUnBondCardDTO businessUnBondCardDTO;
        SystemOrderTrackTable sotTable = null;
        InnerPrintLogObject ipo = null ;
        MerchantInfoTable merInfoTable = null;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        try{
            //0.解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            businessUnBondCardDTO = JSON.parse(sotTable.getRequestMsg(), BusinessUnBondCardDTO.class);
            sotTable.setMerId(businessUnBondCardDTO.getMerId());
            //创建日志打印对象
            ipo = new InnerPrintLogObject(businessUnBondCardDTO.getMerId(), businessUnBondCardDTO.getTerMerId(),bussType);
            //获取商户信息
            merInfoTable = newUnBondCardService.getOneMerInfo(ipo);
            //获取必要参数
            Map<String, ParamRule> paramRuleMap = newUnBondCardService.getParamMapByUnBondCard();
            //参数校验
            this.verify(paramRuleMap, businessUnBondCardDTO,ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //根据通道标识，获取机构信息,主要识别通道标识是否存在
            OrganizationInfoTable oit = newUnBondCardService.getOrganizationInfoByUnBondCard(businessUnBondCardDTO.getChannelTab(),ipo);
            //获取绑卡信息
            MerchantCardTable  mct =  newUnBondCardService.getMerchantCardInfo(new MerchantCardTable()
                    .setTerminalMerId(businessUnBondCardDTO.getTerMerId())
                    .setMerchantId(businessUnBondCardDTO.getMerId())
                    .setBackCardNum(businessUnBondCardDTO.getBankCardNum())
                    .setOrganizationId(businessUnBondCardDTO.getChannelTab())
                    .setBussType(BusinessTypeEnum.b6.getBusiType())
                    .setStatus(StatusEnum._0.getStatus()),ipo);
            //获取进件附属信息
            RegisterCollectTable rct = newUnBondCardService.getRegCollectInfo(new RegisterCollectTable()
                    .setOrganizationId(mct.getOrganizationId())
                    .setTerminalMerId(businessUnBondCardDTO.getTerMerId())
                    .setMerchantId(businessUnBondCardDTO.getMerId())
                    .setBussType(BusinessTypeEnum.b3.getBusiType())
                    .setStatus(StatusEnum._0.getStatus())
                    ,ipo);
            //获取绑卡附属通道
            ChannelExtraInfoTable cei = newUnBondCardService.getChannelExtraInfoByOrgId(mct.getOrganizationId(),BussTypeEnum.BONDCARD.getBussType(),ipo);
            //生成通道处理对象
            Class  clz=Class.forName(oit.getApplicationClassObj().trim());
            CommonChannelHandlePortComponent commonChannelHandlePortComponent = (CommonChannelHandlePortComponent) SpringContextUtil.getBean(clz);
            //封装cross必要参数
            RequestCrossMsgDTO requestCrossMsgDTO = newUnBondCardService.getRequestCrossMsgDTO(new Tuple3(mct,rct,cei));
            //调用业务申请
            crossResponseMsgDTO = commonChannelHandlePortComponent.unBondCard(requestCrossMsgDTO,ipo);
            if(crossResponseMsgDTO.getCrossStatusCode() == StatusEnum._0.getStatus()){
                //更新绑卡信息
                newUnBondCardService.update(mct
                        .setCrossRespResult(com.alibaba.fastjson.JSON.toJSONString(crossResponseMsgDTO))
                        .setChannelRespResult(crossResponseMsgDTO.getChannelResponseMsg())
                        .setStatus(StatusEnum._6.getStatus())
                        .setUpdateTime(new Date()),ipo);
            }
            //// merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg,channelTab
            respResult = newUnBondCardService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,null,null,null,null,null);
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
            respResult = newUnBondCardService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO, null,null,null,errorCode,errorMsg);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newUnBondCardService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }

}
