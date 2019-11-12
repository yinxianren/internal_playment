package com.internal.playment.pay.service.shortcut.impl;

import com.internal.playment.common.dto.*;
import com.internal.playment.common.enums.BusinessTypeEnum;
import com.internal.playment.common.enums.ParamTypeEnum;
import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.business.MerchantCardTable;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.business.RegisterInfoTable;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.common.tuple.Tuple5;
import com.internal.playment.pay.service.CommonServiceAbstract;
import com.internal.playment.pay.service.shortcut.NewBondCardService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/23
 * Time: 下午9:52
 * Description:
 */
@Service
public class NewBondCardServiceImpl extends CommonServiceAbstract implements NewBondCardService {


    @Override
    public void multipleOrder(String merOrderId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="multipleOrder";
        List<MerchantCardTable> merchantCardTableList =null;
        try {
            merchantCardTableList = dbCommonRPCComponent.apiMerchantCardService.getList(new MerchantCardTable()
                    .setMerOrderId(merOrderId)
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
            );
        }catch (Exception e){
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询订单是否重复，发生异常！异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isHasElement(merchantCardTableList,
                ResponseCodeEnum.RXH00009.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00009.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00009.getMsg()));

    }

//    @Override
//    public void checkSuccessOrder(MerchantBondCardApplyDTO mbcaDTO, InnerPrintLogObject ipo) throws NewPayException {
//        final String localPoint="checkSuccessOrder";
//        MerchantCardTable merchantCardTable = commonRPCComponent.apiMerchantCardService.getOne(new MerchantCardTable()
//                .setMerchantId(ipo.getMerId())
//                .setTerminalMerId(ipo.getTerMerId())
//                .setBankCardNum(mbcaDTO.getBankCardNum())
//                .setStatus(StatusEnum._0.getStatus())
//        );
//        isNotNull(merchantCardTable,
//                ResponseCodeEnum.RXH00028.getCode(),
//                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00028.getMsg(),localPoint),
//                format(" %s",ResponseCodeEnum.RXH00028.getMsg()));
//    }

//    @Override
//    public RegisterCollectTable getSuccessRegisterCollectInfo(MerchantBondCardApplyDTO mbcaDTO,InnerPrintLogObject ipo) throws NewPayException {
//        final String localPoint="getSuccessRegisterCollectInfo";
//        //成功进件的
//        List<RegisterCollectTable>  registerCollectTableList = commonRPCComponent.apiRegisterCollectService.getList(new RegisterCollectTable()
//                .setMerchantId(ipo.getMerId())
//                .setTerminalMerId(ipo.getTerMerId())
//                .setBussType(BusinessTypeEnum.b3.getBusiType())
//                .setStatus(StatusEnum._0.getStatus())
//        );
//
//        isHasNotElement(registerCollectTableList,
//                ResponseCodeEnum.RXH00030.getCode(),
//                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00030.getMsg(),localPoint),
//                format(" %s",ResponseCodeEnum.RXH00030.getMsg()));
//
//        //成功绑卡的
//        List<MerchantCardTable>  merchantCardTableList= commonRPCComponent.apiMerchantCardService.getList(new MerchantCardTable()
//                .setMerchantId(ipo.getMerId())
//                .setTerminalMerId(ipo.getTerMerId())
//                .setStatus(StatusEnum._0.getStatus())
//        );
//
//
//        LinkedList<RegisterCollectTable> linkedList = new LinkedList<>(registerCollectTableList);
//        registerCollectTableList.forEach(rc -> {
//            merchantCardTableList.forEach(mc -> {
//                if( rc.getOrganizationId().equalsIgnoreCase(mc.getOrganizationId())
//                        && rc.getProductId().equalsIgnoreCase( mc.getProductId()) )
//                    linkedList.remove(rc);
//            });
//        });
//
//        isHasNotElement(linkedList,
//                ResponseCodeEnum.RXH00031.getCode(),
//                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00031.getMsg(),localPoint),
//                format(" %s",ResponseCodeEnum.RXH00031.getMsg()));
//
//        //仅有有一条数据，则直接返回
//        if( linkedList.size() == 1 )
//            return linkedList.get(0);
//
//        //匹配银行卡号
//        for(RegisterCollectTable rc : linkedList){
//            if(rc.getBankCardNum() .equalsIgnoreCase( mbcaDTO.getBankCardNum() ) ){
//                return  rc;
//            }
//        }
//        //取最新时间
//        RegisterCollectTable rc = linkedList.stream()
//                .reduce( (_1,_2) -> _1.getCreateTime().compareTo(_2.getCreateTime()) > 0 ? _1 : _2  )
//                .orElse(null);
//
//        isNull(rc,
//                ResponseCodeEnum.RXH99999.getCode(),
//                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;错误根源：多个成功进件附属表根据时间大小选取时，得到结果null",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint),
//                format(" %s",ResponseCodeEnum.RXH99999.getMsg()));
//
//        return rc;
//    }

    @Override
    public MerchantCardTable saveCardInfoByB4(MerBondCardApplyDTO mbcaDTO, ChannelInfoTable channelInfoTable, RegisterCollectTable registerCollectTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="saveCardInfoByB4";
        MerchantCardTable  merchantCardTable = new MerchantCardTable()
                .setChannelId(channelInfoTable.getChannelId())
                .setId(System.currentTimeMillis())
                .setRegisterCollectPlatformOrderId(registerCollectTable.getPlatformOrderId())
                .setOrganizationId(channelInfoTable.getOrganizationId())
                .setProductId(channelInfoTable.getProductId())
                .setPlatformOrderId("RXH" + new Random(System.currentTimeMillis()).nextInt(1000000) + "-B4" + System.currentTimeMillis())
                .setMerchantId(mbcaDTO.getMerId())
                .setMerOrderId(mbcaDTO.getMerOrderId())
                .setTerminalMerId(mbcaDTO.getTerMerId())
                .setCardHolderName(mbcaDTO.getCardHolderName())
                .setIdentityType(Integer.valueOf(mbcaDTO.getIdentityType()))
                .setIdentityNum(mbcaDTO.getIdentityNum())
                .setBankCode(mbcaDTO.getBankCode())
                .setBankName(mbcaDTO.getBankName())
                .setBankAccountProp(Integer.valueOf(mbcaDTO.getBankAccountProp()))
                .setSubBankName(mbcaDTO.getSubBankName())
                .setBankCardType(Integer.valueOf(mbcaDTO.getBankCardType()))
                .setBankCardNum(mbcaDTO.getBankCardNum())
                .setBankCardPhone(mbcaDTO.getBankCardPhone())
                .setValidDate(mbcaDTO.getValidDate())
                .setSecurityCode(mbcaDTO.getSecurityCode())
                .setChannelRespResult(null).setCrossRespResult(null)
                .setBussType(BusinessTypeEnum.b4.getBusiType())
                .setStatus(StatusEnum._3.getStatus()).setCreateTime(new Date()).setUpdateTime(new Date());
        try {
            dbCommonRPCComponent.apiMerchantCardService.save(merchantCardTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：B4保存绑卡申请信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return merchantCardTable;
    }

    @Override
    public MerchantCardTable saveCardInfoByB5(MerchantCardTable merchantCardTable, MerReGetBondCodeDTO mrgbcDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="saveCardInfoByB5";
        merchantCardTable
                .setId(System.currentTimeMillis())
                .setPlatformOrderId("RXH" + new Random(System.currentTimeMillis()).nextInt(1000000) + "-B5" + System.currentTimeMillis())
                .setTerminalMerId(mrgbcDTO.getTerMerId())
                .setCardHolderName(mrgbcDTO.getCardHolderName())
                .setIdentityType(Integer.valueOf(mrgbcDTO.getIdentityType()))
                .setIdentityNum(mrgbcDTO.getIdentityNum())
                .setBankCode(mrgbcDTO.getBankCode())
                .setBankCardType(Integer.valueOf(mrgbcDTO.getBankCardType()))
                .setBankCardNum(mrgbcDTO.getBankCardNum())
                .setBankCardPhone(mrgbcDTO.getBankCardPhone())
                .setValidDate(mrgbcDTO.getValidDate())
                .setSecurityCode(mrgbcDTO.getSecurityCode())
                .setChannelRespResult(null).setCrossRespResult(null)
                .setBussType(BusinessTypeEnum.b5.getBusiType())
                .setStatus(StatusEnum._3.getStatus()).setCreateTime(new Date()).setUpdateTime(new Date());

        try {
            dbCommonRPCComponent.apiMerchantCardService.save(merchantCardTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：B5保存绑卡申请信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return merchantCardTable;
    }

    @Override
    public MerchantCardTable saveCardInfoByB6(MerchantCardTable merchantCardTable, MerConfirmBondCardDTO mcbcDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="saveCardInfoByB6";
        merchantCardTable
                .setId(System.currentTimeMillis())
                .setPlatformOrderId("RXH" + new Random(System.currentTimeMillis()).nextInt(1000000) + "-B5" + System.currentTimeMillis())
                .setTerminalMerId(mcbcDTO.getTerMerId())
                .setCardHolderName(mcbcDTO.getCardHolderName())
                .setIdentityType(Integer.valueOf(mcbcDTO.getIdentityType()))
                .setIdentityNum(mcbcDTO.getIdentityNum())
                .setBankCode(mcbcDTO.getBankCode())
                .setBankCardType(Integer.valueOf(mcbcDTO.getBankCardType()))
                .setBankCardNum(mcbcDTO.getBankCardNum())
                .setBankCardPhone(mcbcDTO.getBankCardPhone())
                .setValidDate(mcbcDTO.getValidDate())
                .setSecurityCode(mcbcDTO.getSecurityCode())
                .setChannelRespResult(null).setCrossRespResult(null)
                .setBussType(BusinessTypeEnum.b6.getBusiType())
                .setPayFee(new BigDecimal(mcbcDTO.getPayFee()))
                .setBackFee(new BigDecimal(mcbcDTO.getBackFee()))
                .setBackCardNum(mcbcDTO.getBankCardNum())
                .setBackBankCode(mcbcDTO.getBackBankCode())
                .setBackCardPhone(mcbcDTO.getBackCardPhone())
                .setStatus(StatusEnum._3.getStatus()).setCreateTime(new Date()).setUpdateTime(new Date());
        try {
            dbCommonRPCComponent.apiMerchantCardService.save(merchantCardTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：B6保存绑卡申请信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return merchantCardTable.setSmsCode(mcbcDTO.getSmsCode());
    }

    @Override
    public void updateByBondCardInfo(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, MerchantCardTable merchantCardTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="updateByBondCardInfo";
        merchantCardTable.setCrossRespResult(crossResponseMsg)
                .setChannelRespResult( null == crossResponseMsgDTO ? null  : crossResponseMsgDTO.getChannelResponseMsg() )
                .setStatus( null == crossResponseMsgDTO ? StatusEnum._1.getStatus() : crossResponseMsgDTO.getCrossStatusCode() );
        try {
            dbCommonRPCComponent.apiMerchantCardService.updateById(merchantCardTable);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：更新绑卡申请信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
    }

    @Override
    public RegisterCollectTable getRegisterInfoTableByPlatformOrderId(String platformOrderId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getRegisterInfoTableByPlatformOrderId";
        RegisterCollectTable registerCollectTable = null;
        try {
            registerCollectTable = dbCommonRPCComponent.apiRegisterCollectService.getOne(new RegisterCollectTable()
                    .setPlatformOrderId(platformOrderId)
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
                    .setBussType(BusinessTypeEnum.b3.getBusiType())
                    .setStatus(StatusEnum._0.getStatus())
            );
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：根据平台流水号获取进件成功的附属表,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(registerCollectTable,
                ResponseCodeEnum.RXH00032.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;"
                        ,ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00032.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00032.getMsg()));

        return registerCollectTable;
    }

    @Override
    public List<RegisterCollectTable> getRegCollectBySuccess(MerBondCardApplyDTO mbcaDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getRegCollectBySuccess";
        List<RegisterCollectTable>  list;
        try{
            list = dbCommonRPCComponent.apiRegisterCollectService.getList(new RegisterCollectTable()
                    .setTerminalMerId(mbcaDTO.getTerMerId())
                    .setMerchantId(mbcaDTO.getMerId())
                    .setBussType(BusinessTypeEnum.b3.getBusiType())
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：获取所有进件成功信息，发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isHasNotElement(list,
                ResponseCodeEnum.RXH00030.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00030.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00030.getMsg()));
        return list;
    }

    @Override
    public ChannelInfoTable getChannelInfoByRegCollect(List<RegisterCollectTable> registerCollectTableList, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getChannelInfoByRegCollect";
        Set<String> channelIdSet = registerCollectTableList.stream().map(RegisterCollectTable::getChannelId).collect(Collectors.toSet());
        List<ChannelInfoTable> channelInfoTableList;
        try{
            channelInfoTableList  = dbCommonRPCComponent.apiChannelInfoService.batchGetByChannelId(channelIdSet);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：获取进件成功的所有通道时，发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isHasNotElement(channelInfoTableList,
                ResponseCodeEnum.RXH00022.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;发错误根源：根据进件成功信息，获取通道信息时，没有找到对应的通道",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00022.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00022.getMsg()));

        ChannelInfoTable channelInfoTable =  channelInfoTableList.stream().reduce((_1,_2)-> _1.getChannelLevel().compareTo( _2.getChannelLevel()) > 0 ? _1 :_2 ).orElseGet(null);

        isNull(channelInfoTable,
                ResponseCodeEnum.RXH99999.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;发错误根源：获取高等级通道时，筛选过程中得到null值",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH99999.getMsg()));
        return channelInfoTable;
    }

    @Override
    public RegisterCollectTable filterRegCollectByChannelId(List<RegisterCollectTable> registerCollectTableList, String channelId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="filterRegCollectByChannelId";
        RegisterCollectTable registerCollectTable=  registerCollectTableList.stream()
                .filter(t -> t.getChannelId().equalsIgnoreCase(channelId)).findAny().orElse(null);
        isNull(registerCollectTable,
                ResponseCodeEnum.RXH99999.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;发错误根源：根据通道ID筛选进件附属表信息时，得到null对象",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH99999.getMsg()));
        return registerCollectTable;
    }

    @Override
    public List<MerchantCardTable> getMerCartInfoBySuccess(MerBondCardApplyDTO mbcaDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getMerCartInfoBySuccess";
        List<MerchantCardTable>  list=null;
        try{
            list = dbCommonRPCComponent.apiMerchantCardService.getList(new MerchantCardTable()
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
                    .setBankCardNum(mbcaDTO.getBankCardNum())
                    .setBussType(BusinessTypeEnum.b6.getBusiType())
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：获取成功绑卡信息时，发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return list;
    }

    @Override
    public List<RegisterCollectTable> filterRegCollectByBondCardSuccess(List<RegisterCollectTable> registerCollectTableList, List<MerchantCardTable> merchantCardTableList, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="filterRegCollectByBondCardSuccess";
        LinkedList<RegisterCollectTable>  linkedList = new LinkedList<>(registerCollectTableList);
        registerCollectTableList.forEach( rc ->{
            merchantCardTableList.forEach( mc->{
                if(        mc.getOrganizationId().equalsIgnoreCase(rc.getOrganizationId())
                        && mc.getBankCardNum().equalsIgnoreCase(rc.getBankCardNum())){
                    linkedList.remove(rc);
                }
            });
        });
        isHasNotElement(linkedList,
                ResponseCodeEnum.RXH00023.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;发错误根源：过滤掉已经成功绑卡的通道，已经无其他通道可以绑卡",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00023.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00023.getMsg()));
        return linkedList;
    }

    @Override
    public void updateByBondCardInfoByB6(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, MerchantCardTable merchantCardTable, MerchantCardTable merchantCardTable_old, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="updateByBondCardInfoByB6";
        merchantCardTable.setCrossRespResult(crossResponseMsg)
                .setChannelRespResult( null == crossResponseMsgDTO ? null  : crossResponseMsgDTO.getChannelResponseMsg() )
                .setStatus( null == crossResponseMsgDTO ? StatusEnum._1.getStatus() : crossResponseMsgDTO.getCrossStatusCode() );

        merchantCardTable_old.setStatus(StatusEnum._5.getStatus());

        List<MerchantCardTable> list = new ArrayList<>(2);
        list.add(merchantCardTable);
        list.add(merchantCardTable_old);
        try {
            dbCommonRPCComponent.apiMerchantCardService.bachUpdateById(list);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：更新绑卡申请信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }

    }


    @Override
    public RequestCrossMsgDTO getRequestCrossMsgDTO(Tuple2 tuple) {
        Tuple5<RegisterInfoTable,RegisterCollectTable,ChannelInfoTable, ChannelExtraInfoTable,MerchantCardTable> tuple5 = (Tuple5<RegisterInfoTable, RegisterCollectTable, ChannelInfoTable, ChannelExtraInfoTable, MerchantCardTable>) tuple;
        return new RequestCrossMsgDTO()
                .setMerchantCardTable(tuple5._5)
                .setChannelInfoTable(tuple5._3)
                .setChannelExtraInfoTable(tuple5._4)
                .setRegisterInfoTable(tuple5._1)
                .setRegisterCollectTable(tuple5._2);
    }

    @Override
    public Map<String, ParamRule> getParamMapByB4() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("merOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));// 商户订单号
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));// 持卡人姓名
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、	否	1
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码		否	32
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));//银行简码	如：中国农业银行： ABC，中国工商银行： ICBC	否	16
                put("bankName", new ParamRule(ParamTypeEnum.STRING.getType(), 4, 32));//银行名称
                put("bankAccountProp", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//账户属性
                put("subBankName", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//支行名称
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//卡号类型	1借记卡  2信用卡	否	1
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//银行卡号		否	32
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));//银行卡手机号		否	11
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("terMerName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//子商户名称	商户系统中商户的名称	否	32
                put("returnUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//签名字符串
                put("noticeUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//签名字符串
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }

    @Override
    public Map<String, ParamRule> getParamMapByB5() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("platformOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 平台流水号
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));// 持卡人姓名
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、	否	1
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码		否	32
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));//银行名称	如：中国农业银行： ABC，中国工商银行： ICBC	否	16
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//卡号类型	1借记卡  2信用卡	否	1
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//银行卡号		否	32
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));//银行卡手机号		否	11
                put("terminalMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("terminalMerName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//子商户名称	商户系统中商户的名称	否	32
                put("returnUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//签名字符串
                put("noticeUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//签名字符串
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }

    @Override
    public Map<String, ParamRule> getParamMapByB6() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("platformOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 平台流水号
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));// 持卡人姓名
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、	否	1
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码		否	32
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));//银行名称	如：中国农业银行： ABC，中国工商银行： ICBC	否	16
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//卡号类型	1借记卡  2信用卡	否	1
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//银行卡号		否	32
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));//银行卡手机号		否	11
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id	商户系统中商户的编码，要求唯一	否	64
                put("terMerName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//子商户名称	商户系统中商户的名称	否	32
                put("returnUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//返回地址
                put("noticeUrl", new ParamRule(ParamTypeEnum.URL.getType(), 16, 128));//通知地址
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
                put("smsCode", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 6));//短信验证码
                put("payFee", new ParamRule(ParamTypeEnum.AMOUNT.getType(), 3, 6));//代付手续费
                put("backFee", new ParamRule(ParamTypeEnum.AMOUNT.getType(), 3, 6));//代付手续费
                put("backCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//还款银行卡号
                put("backBankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));//还款银行编码
                put("backCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));//还款银行卡手机号
            }
        };
    }


}
