package com.internal.playment.pay.controller.shortcut;

import com.alibaba.dubbo.common.json.JSON;
import com.internal.playment.common.dto.*;
import com.internal.playment.common.enums.BusinessTypeEnum;
import com.internal.playment.common.enums.BussTypeEnum;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.business.RegisterInfoTable;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.system.MerchantSettingTable;
import com.internal.playment.common.table.system.OrganizationInfoTable;
import com.internal.playment.common.table.system.ProductSettingTable;
import com.internal.playment.common.table.system.SystemOrderTrackTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.common.tuple.Tuple4;
import com.internal.playment.pay.channel.CommonChannelHandlePortComponent;
import com.internal.playment.pay.component.Md5Component;
import com.internal.playment.pay.config.SpringContextUtil;
import com.internal.playment.pay.controller.NewAbstractCommonController;
import com.internal.playment.pay.service.shortcut.NewIntoPiecesOfInformationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 *  进件
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午2:40
 * Description:
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/shortcut")
public class NewIntoPiecesOfInformationController extends NewAbstractCommonController {

    private final NewIntoPiecesOfInformationService newIntoPiecesOfInformationService;
    private final Md5Component md5Component;

    /**
     *  基本信息登记
     * @param request
     * @param param
     * @return
     *
     *
     */
    @PostMapping(value = "/addCusInfo" ,produces = "text/html;charset=UTF-8")
    public String intoPiecesOfInformation(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【基本信息登记】";
        String errorMsg,errorCode,printErrorMsg,respResult=null;
        SystemOrderTrackTable sotTable = null;
        MerBasicInfoRegDTO mbirDTO=null;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO requestCrossMsgDTO;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        String crossResponseMsg = null;
        Tuple2<RegisterInfoTable, RegisterCollectTable> tuple = null;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            mbirDTO = JSON.parse(sotTable.getRequestMsg(), MerBasicInfoRegDTO.class);
            sotTable.setMerId(mbirDTO.getMerId()).setMerOrderId(mbirDTO.getMerOrderId());
            //创建日志打印对象
            ipo = new InnerPrintLogObject(mbirDTO.getMerId(),mbirDTO.getTerMerId(),bussType);
            //获取商户信息
            merInfoTable = newIntoPiecesOfInformationService.getOneMerInfo(ipo);
            //获取必要参数
            Map<String, ParamRule> paramRuleMap =newIntoPiecesOfInformationService.getParamMapByB1();
            //参数校验
            this.verify(paramRuleMap,mbirDTO,ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //查看是否重复订单
            newIntoPiecesOfInformationService.multipleOrder(mbirDTO.getMerOrderId(),ipo);
            // 获取商户配置
            List<MerchantSettingTable>  merchantSettingTableList=newIntoPiecesOfInformationService.getMerchantSetting(ipo);
            //获取配置的所有通道
            List<ChannelInfoTable>  channelInfoTableList = newIntoPiecesOfInformationService.getChannelInfoByMerSetting(merchantSettingTableList,ipo);
            //根据产品类型进行过滤
            Tuple2<List<ProductSettingTable>,Set<ChannelInfoTable>> tuple2=newIntoPiecesOfInformationService.filtrationChannelInfoByProductType(channelInfoTableList,mbirDTO.getProductType(),ipo);
            //获取商户成功进件的信息
            List<RegisterCollectTable>  registerCollectTableList = newIntoPiecesOfInformationService.getRegisterCollectOnSuccess(ipo);
            //过滤已经成功进件的通道
            LinkedList<ChannelInfoTable> channelInfoTablesList=newIntoPiecesOfInformationService.filtrationChannelInfoBySuccessRegisterCollect(tuple2._2,registerCollectTableList,ipo);
            //获取星级最高的通道，如果相同，取最后一个
            ChannelInfoTable channelInfoTable = newIntoPiecesOfInformationService.filtrationChannelInfoByLevel(channelInfoTablesList,ipo);
            //获取进件附属通道
            ChannelExtraInfoTable extraInfoTable = newIntoPiecesOfInformationService.getAddCusChannelExtraInfo(channelInfoTable,ipo);
            //获取组织机构信息
            OrganizationInfoTable organizationInfoTable = newIntoPiecesOfInformationService.getOrganizationInfo(channelInfoTable.getOrganizationId(),ipo);
            Class  clz=Class.forName(organizationInfoTable.getApplicationClassObj().trim());
            //保存进件信息
            tuple = newIntoPiecesOfInformationService.saveByRegister(mbirDTO,channelInfoTable,ipo);
            sotTable.setPlatformOrderId(tuple._2.getPlatformOrderId());
            //封装请求cross必要参数
            requestCrossMsgDTO = newIntoPiecesOfInformationService.getRequestCrossMsgDTO(new Tuple4(channelInfoTable,extraInfoTable,tuple._1,tuple._2));
            requestCrossMsgDTO.setIP(sotTable.getIp());
            //生成通道处理对象
            CommonChannelHandlePortComponent commonChannelHandlePortComponent = (CommonChannelHandlePortComponent) SpringContextUtil.getBean(clz);
            //调用业务申请
            crossResponseMsgDTO = commonChannelHandlePortComponent.addCusInfo(requestCrossMsgDTO,ipo);
            crossResponseMsg =  null == crossResponseMsgDTO ? null : crossResponseMsgDTO.toString();
            //更新进件信息
            newIntoPiecesOfInformationService.updateByRegisterCollectTable(crossResponseMsgDTO,crossResponseMsg,tuple._2,ipo);
            //crossResponseMsgDTO 状态码非成功则抛出异常
            newIntoPiecesOfInformationService.isSuccess(crossResponseMsgDTO,ipo);
            //封装放回结果  // merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg
            respResult = newIntoPiecesOfInformationService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,mbirDTO.getMerOrderId(),sotTable.getPlatformOrderId(),null,null,null);
            sotTable.setPlatformPrintLog( crossResponseMsg )
                    .setTradeCode( null == crossResponseMsgDTO ? StatusEnum._1.getStatus(): crossResponseMsgDTO.getCrossStatusCode() );
        }catch (Exception e){
            if(e instanceof NewPayException){
                NewPayException npe = (NewPayException) e;
                errorMsg = npe.getResponseMsg();
                printErrorMsg = npe.getInnerPrintMsg();
                errorCode = npe.getCode();
                log.info(printErrorMsg);
            }else{
                e.printStackTrace();
                errorMsg = ResponseCodeEnum.RXH99999.getMsg();
                printErrorMsg = isBlank(e.getMessage()) ? e.getClass().getName() : (e.getMessage().length()>=512 ? e.getMessage().substring(0,526) : e.getMessage());
                errorCode = ResponseCodeEnum.RXH99999.getCode();
            }
            if(!isNull(tuple))
                newIntoPiecesOfInformationService.updateByRegisterCollectTable(crossResponseMsgDTO,crossResponseMsg ,tuple._2,ipo);
            // merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg
            respResult = newIntoPiecesOfInformationService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,
                    null != mbirDTO ? mbirDTO.getMerOrderId() : null, null != tuple ? tuple._2.getPlatformOrderId(): null,null,errorCode,errorMsg);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newIntoPiecesOfInformationService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }

    /**
     * 银行卡登记接口
     * @param request
     * @param param
     * @return
     */
    @PostMapping(value="/bankCardBind", produces = "text/html;charset=UTF-8")
    public String bankCardBinding(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【银行卡登记接口】";
        String errorMsg,errorCode,printErrorMsg,respResult=null;
        SystemOrderTrackTable sotTable = null;
        MerBankCardBindDTO mbcbDTO;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO  requestCrossMsgDTO;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        RegisterCollectTable registerCollectTable = null;
        Tuple2<RegisterInfoTable,RegisterCollectTable>  tuple2 = null;
        String crossResponseMsg = null;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            mbcbDTO = JSON.parse(sotTable.getRequestMsg(),MerBankCardBindDTO.class);
            sotTable.setMerId(mbcbDTO.getMerId());
            //创建日志打印对象
            ipo = new InnerPrintLogObject(mbcbDTO.getMerId(),mbcbDTO.getTerMerId(),bussType);
            //获取商户信息
            merInfoTable = newIntoPiecesOfInformationService.getOneMerInfo(ipo);
            //获取必要参数
            Map<String, ParamRule> paramRuleMap =newIntoPiecesOfInformationService.getParamMapByB2();
            //判断平台订单号是否存在
            registerCollectTable = newIntoPiecesOfInformationService.getRegisterCollectTable(mbcbDTO.getPlatformOrderId(), BusinessTypeEnum.b1.getBusiType(),ipo);
            sotTable.setMerOrderId(registerCollectTable.getMerOrderId());
            //验证是否重复操作
            newIntoPiecesOfInformationService.checkRepetitionOperation(registerCollectTable,BusinessTypeEnum.b2.getBusiType(),ipo);
            //参数校验
            this.verify(paramRuleMap,mbcbDTO,ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //更新RegisterCollectTable并保存
            tuple2 = newIntoPiecesOfInformationService.saveOnRegisterInfo(registerCollectTable,mbcbDTO,ipo);
            sotTable.setPlatformOrderId(tuple2._2.getPlatformOrderId());
            //获取通道信息
            ChannelInfoTable channelInfoTable = newIntoPiecesOfInformationService.getChannelInfoByChannelId(tuple2._2.getChannelId(),ipo);
            //获取附属通道信息
            ChannelExtraInfoTable channelExtraInfoTable =  newIntoPiecesOfInformationService.getChannelExtraInfoByOrgId(channelInfoTable.getOrganizationId(), BussTypeEnum.ADDCUS.getBussType(),ipo);
            //获取组织机构信息
            OrganizationInfoTable organizationInfoTable = newIntoPiecesOfInformationService.getOrganizationInfo(channelInfoTable.getOrganizationId(),ipo);
            Class  clz=Class.forName(organizationInfoTable.getApplicationClassObj().trim());
            //封装请求cross必要参数
            requestCrossMsgDTO = newIntoPiecesOfInformationService.getRequestCrossMsgDTO(new Tuple4(channelInfoTable,channelExtraInfoTable,tuple2._1,tuple2._2));
            requestCrossMsgDTO.setIP(sotTable.getIp());
            //生成通道处理对象
            CommonChannelHandlePortComponent commonChannelHandlePortComponent = (CommonChannelHandlePortComponent) SpringContextUtil.getBean(clz);
            //调用业务申请
            crossResponseMsgDTO = commonChannelHandlePortComponent.bankCardBind(requestCrossMsgDTO,ipo);
            crossResponseMsg =  null == crossResponseMsgDTO ? null : crossResponseMsgDTO.toString();
            //更新进件信息
            newIntoPiecesOfInformationService.updateByRegisterCollectTable(crossResponseMsgDTO,crossResponseMsg,tuple2._2,ipo);
            //crossResponseMsgDTO 状态码非成功则抛出异常
            newIntoPiecesOfInformationService.isSuccess(crossResponseMsgDTO,ipo);
            //封装放回结果  // merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg
            respResult = newIntoPiecesOfInformationService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,registerCollectTable.getMerOrderId(),sotTable.getPlatformOrderId(),null,null,null);
            sotTable.setPlatformPrintLog(  null == crossResponseMsgDTO ? crossResponseMsg : StatusEnum.remark(crossResponseMsgDTO.getCrossStatusCode()))
                    .setTradeCode( null == crossResponseMsgDTO ? StatusEnum._1.getStatus(): crossResponseMsgDTO.getCrossStatusCode() );
        }catch (Exception e){
            if(e instanceof NewPayException){
                NewPayException npe = (NewPayException) e;
                errorMsg = npe.getResponseMsg();
                printErrorMsg = npe.getInnerPrintMsg();
                errorCode = npe.getCode();
                log.info(printErrorMsg);
            }else{
                e.printStackTrace();
                errorMsg = ResponseCodeEnum.RXH99999.getMsg();
                printErrorMsg = isBlank(e.getMessage()) ? e.getClass().getName() : (e.getMessage().length()>=512 ? e.getMessage().substring(0,526) : e.getMessage());
                errorCode = ResponseCodeEnum.RXH99999.getCode();
            }
            if(!isNull(tuple2) && tuple2._2.getBussType().equalsIgnoreCase(BusinessTypeEnum.b2.getBusiType()))
                newIntoPiecesOfInformationService.updateByRegisterCollectTable(crossResponseMsgDTO,crossResponseMsg,tuple2._2,ipo);
            // merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg
            respResult = newIntoPiecesOfInformationService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,
                    null != registerCollectTable ? registerCollectTable.getMerOrderId() : null, null != registerCollectTable ? registerCollectTable.getPlatformOrderId(): null,null,errorCode,errorMsg);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newIntoPiecesOfInformationService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }


    /**
     *  业务开通接口
     * @param request
     * @param param
     * @return
     */
    @PostMapping(value="/serviceFulfillment", produces = "text/html;charset=UTF-8")
    public String  serviceFulfillment(HttpServletRequest request, @RequestBody(required = false) String param){
        final String bussType = "【业务开通接口】";
        String errorMsg,errorCode,printErrorMsg,respResult=null;
        SystemOrderTrackTable sotTable = null;
        MerServiceFulfillDTO msDTO;
        MerchantInfoTable merInfoTable = null;
        RequestCrossMsgDTO  requestCrossMsgDTO;
        CrossResponseMsgDTO crossResponseMsgDTO = null;
        InnerPrintLogObject ipo = null ;
        RegisterCollectTable registerCollectTable =null;
        String crossResponseMsg = null;
        try{
            //解析 以及 获取SystemOrderTrackTable对象
            sotTable = this.getSystemOrderTrackTable(request,param,bussType);
            //类型转换
            msDTO = JSON.parse(sotTable.getRequestMsg(),MerServiceFulfillDTO.class);
            sotTable.setMerId(msDTO.getMerId());
            //创建日志打印对象
            ipo = new InnerPrintLogObject(msDTO.getMerId(),msDTO.getTerMerId(),bussType);
            //获取商户信息
            merInfoTable = newIntoPiecesOfInformationService.getOneMerInfo(ipo);
            //获取必要参数
            Map<String, ParamRule> paramRuleMap =newIntoPiecesOfInformationService.getParamMapByB3();
            //判断订单是否存在
            registerCollectTable = newIntoPiecesOfInformationService.getRegisterCollectTable(msDTO.getPlatformOrderId(), BusinessTypeEnum.b2.getBusiType(),ipo);
            sotTable.setMerOrderId(registerCollectTable.getMerOrderId());
            //验证是否重复操作
            newIntoPiecesOfInformationService.checkRepetitionOperation(registerCollectTable,BusinessTypeEnum.b3.getBusiType(),ipo);
            //参数校验
            this.verify(paramRuleMap,msDTO,ipo);
            //验证签名
            md5Component.checkMd5(sotTable.getRequestMsg(),merInfoTable.getSecretKey(),ipo);
            //更新进件附属表信息
            registerCollectTable = newIntoPiecesOfInformationService.saveRegisterCollectTableByB3(registerCollectTable,ipo);
            sotTable.setPlatformOrderId(registerCollectTable.getPlatformOrderId());
            //获取进件主表信息
            RegisterInfoTable registerInfoTable = newIntoPiecesOfInformationService.getRegisterInfoTable(registerCollectTable.getRitId(),ipo);
            //获取通道信息
            ChannelInfoTable channelInfoTable = newIntoPiecesOfInformationService.getChannelInfoByChannelId(registerCollectTable.getChannelId(),ipo);
            //获取附属通道信息
            ChannelExtraInfoTable channelExtraInfoTable =  newIntoPiecesOfInformationService.getChannelExtraInfoByOrgId(channelInfoTable.getOrganizationId(), BussTypeEnum.ADDCUS.getBussType(),ipo);
            //获取组织机构信息
            OrganizationInfoTable organizationInfoTable = newIntoPiecesOfInformationService.getOrganizationInfo(channelInfoTable.getOrganizationId(),ipo);
            Class  clz=Class.forName(organizationInfoTable.getApplicationClassObj().trim());
            //封装请求cross必要参数
            requestCrossMsgDTO = newIntoPiecesOfInformationService.getRequestCrossMsgDTO(new Tuple4(channelInfoTable,channelExtraInfoTable,registerInfoTable,registerCollectTable));
            requestCrossMsgDTO.setIP(sotTable.getIp());
            //生成通道处理对象
            CommonChannelHandlePortComponent commonChannelHandlePortComponent = (CommonChannelHandlePortComponent) SpringContextUtil.getBean(clz);
            //调用业务申请
            crossResponseMsgDTO = commonChannelHandlePortComponent.serviceFulfillment(requestCrossMsgDTO,ipo);
            crossResponseMsg =  null == crossResponseMsgDTO ? null : crossResponseMsgDTO.toString();
            //更新进件信息
            newIntoPiecesOfInformationService.updateByRegisterCollectTable(crossResponseMsgDTO,crossResponseMsg,registerCollectTable,ipo);
            //crossResponseMsgDTO 状态码非成功则抛出异常
            newIntoPiecesOfInformationService.isSuccess(crossResponseMsgDTO,ipo);
            //封装放回结果  // merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg
            respResult = newIntoPiecesOfInformationService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,registerCollectTable.getMerOrderId(),sotTable.getPlatformOrderId(),null,null,null);
            sotTable.setPlatformPrintLog(  null == crossResponseMsgDTO ? crossResponseMsg : StatusEnum.remark(crossResponseMsgDTO.getCrossStatusCode()))
                    .setTradeCode( null == crossResponseMsgDTO ? StatusEnum._1.getStatus(): crossResponseMsgDTO.getCrossStatusCode() );
        }catch (Exception e){
            if(e instanceof NewPayException){
                NewPayException npe = (NewPayException) e;
                errorMsg = npe.getResponseMsg();
                printErrorMsg = npe.getInnerPrintMsg();
                errorCode = npe.getCode();
                log.info(printErrorMsg);
            }else{
                e.printStackTrace();
                errorMsg = ResponseCodeEnum.RXH99999.getMsg();
                printErrorMsg = isBlank(e.getMessage()) ? e.getClass().getName() : (e.getMessage().length()>=512 ? e.getMessage().substring(0,526) : e.getMessage());
                errorCode = ResponseCodeEnum.RXH99999.getCode();
            }
            if(!isNull(registerCollectTable) && registerCollectTable.getBussType().equalsIgnoreCase(BusinessTypeEnum.b3.getBusiType()))
                newIntoPiecesOfInformationService.updateByRegisterCollectTable(crossResponseMsgDTO,crossResponseMsg,registerCollectTable,ipo);
            // merInfoTable, ipo, crossResponseMsgDTO,merOrderId,platformOrderId,amount,errorCode,errorMsg
            respResult = newIntoPiecesOfInformationService.responseMsg(merInfoTable,ipo,crossResponseMsgDTO,
                    null != registerCollectTable ? registerCollectTable.getMerOrderId() : null, null != registerCollectTable ? registerCollectTable.getPlatformOrderId(): null,null,errorCode,errorMsg);
            sotTable.setPlatformPrintLog(printErrorMsg).setTradeCode( StatusEnum._1.getStatus());
        }finally {
            sotTable.setResponseResult(respResult).setCreateTime(new Date());
            newIntoPiecesOfInformationService.saveSysLog(sotTable);
            return null == respResult ? "系统内部错误！" : respResult;
        }
    }



}
