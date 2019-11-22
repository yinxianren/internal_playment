package com.internal.playment.pay.service.shortcut.impl;

import com.internal.playment.common.dto.CrossResponseMsgDTO;
import com.internal.playment.common.dto.MerBankCardBindDTO;
import com.internal.playment.common.dto.MerBasicInfoRegDTO;
import com.internal.playment.common.dto.RequestCrossMsgDTO;
import com.internal.playment.common.enums.*;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.inner.ParamRule;
import com.internal.playment.common.table.business.RegisterCollectTable;
import com.internal.playment.common.table.business.RegisterInfoTable;
import com.internal.playment.common.table.channel.ChannelExtraInfoTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.merchant.MerchantRateTable;
import com.internal.playment.common.table.system.MerchantSettingTable;
import com.internal.playment.common.table.system.ProductGroupTypeTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.common.tuple.Tuple3;
import com.internal.playment.pay.service.CommonServiceAbstract;
import com.internal.playment.pay.service.shortcut.NewIntoPiecesOfInformationService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/18
 * Time: 下午3:49
 * Description:
 */
@Service
public class NewIntoPiecesOfInformationServiceImp extends CommonServiceAbstract implements NewIntoPiecesOfInformationService {

    private final static Object b1 = new Object();
    private final static Object b = new Object();
    private final static Object b2 = new Object();
    private final static Object b3 = new Object();
    private static AtomicInteger count = new AtomicInteger(0);
    private static AtomicInteger count2 = new AtomicInteger(0);
    @Override
    public List<RegisterCollectTable> getRegisterCollectOnSuccess(InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint = "getRegisterCollectOnSuccess(InnerPrintLogObject ipo)";
        List<RegisterCollectTable> list;
        try{
            list = dbCommonRPCComponent.apiRegisterCollectService.getList( new RegisterCollectTable()
                    .setBussType(BusinessTypeEnum.b3.getBusiType())
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置B1：%s,异常根源：查询是否有进件成功信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return  list;
    }


    @Override
    public LinkedList<ChannelInfoTable> filtrationChannelInfoBySuccessRegisterCollect(Set<ChannelInfoTable> channelInfoTableSet, List<RegisterCollectTable> registerCollectTableList, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="filtrationChannelInfoBySuccessRegisterCollect";
        LinkedList<ChannelInfoTable>  channelInfoTableLinkedList = new LinkedList<>(channelInfoTableSet);
        channelInfoTableSet.forEach(channel->{
            registerCollectTableList.forEach(regCollect->{
                if(channel.getOrganizationId().equalsIgnoreCase(regCollect.getOrganizationId()))
                    channelInfoTableLinkedList.remove(channel);
            });
        });
        isHasNotElement(channelInfoTableLinkedList,//无更多可用通道
                ResponseCodeEnum.RXH00023.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00020.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00023.getMsg()));;
        return channelInfoTableLinkedList;
    }

    @Override
    public ChannelInfoTable filtrationChannelInfoByLevel(LinkedList<ChannelInfoTable> channelInfoTablesList, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="filtrationChannelInfoByLevel";
        ChannelInfoTable channelInfoTable = channelInfoTablesList
                .stream()
                .reduce((t1,t2)-> t1.getChannelLevel() > t2.getChannelLevel() ? t1 : t2 )
                .orElse(null);
        isNull(channelInfoTable,
                ResponseCodeEnum.RXH99999.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置(取到空通道信息)：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH99999.getMsg()));

        return channelInfoTable;
    }

    @Override
    public ChannelExtraInfoTable getAddCusChannelExtraInfo(ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getAddCusChannelExtraInfo";
        ChannelExtraInfoTable channelExtraInfoTable = null;
        try {
            channelExtraInfoTable = dbCommonRPCComponent.apiChannelExtraInfoService.getOne(
                    new ChannelExtraInfoTable()
                            .setOrganizationId(channelInfoTable.getOrganizationId())
                            .setBussType(BussTypeEnum.ADDCUS.getBussType())
            );
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置B1：%s,异常根源：查询是进件附属通道发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(channelExtraInfoTable,
                ResponseCodeEnum.RXH00024.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00024.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00024.getMsg()));
        return channelExtraInfoTable;
    }

    @Override
    public Tuple2<RegisterInfoTable,RegisterCollectTable> saveByRegister(MerBasicInfoRegDTO mbirDTO, ChannelInfoTable channelInfoTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="saveByRegister";
        try {

            List<MerchantRateTable> merchantRateTableList = dbCommonRPCComponent.apiMerchantRateService.getList(new MerchantRateTable()
                    .setMerchantId(ipo.getMerId())
                    .setStatus(StatusEnum._0.getStatus()));

            isHasNotElement(merchantRateTableList,
                    ResponseCodeEnum.RXH00054.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00054.getMsg(),localPoint),
                    format(" %s",ResponseCodeEnum.RXH00054.getMsg()));

            RegisterInfoTable registerInfoTable = dbCommonRPCComponent.apiRegisterInfoService.getOne(
                    new RegisterInfoTable()
                            .setMerchantId(mbirDTO.getMerId())
                            .setTerminalMerId(mbirDTO.getTerMerId())
                            .setUserName(mbirDTO.getCardHolderName())
                            .setIdentityType(new Integer(mbirDTO.getIdentityType()))
                            .setIdentityNum(mbirDTO.getIdentityNum()));

            if (null == registerInfoTable) {
                synchronized (b) {
                    StringBuilder sb = new StringBuilder()
                            .append("RXH")
                            .append(count2.incrementAndGet())
                            .append("*")
                            .append(java.util.UUID.randomUUID().toString().replaceAll("-", ""))
                            .append(System.currentTimeMillis());
                    registerInfoTable = new RegisterInfoTable()
                            .setPlatformOrderId(sb.toString())
                            .setCreateTime(new Date());
                }
            }
            registerInfoTable.setMerchantId(mbirDTO.getMerId())
                    .setTerminalMerId(mbirDTO.getTerMerId())                    .setTerminalMerName(mbirDTO.getTerMerName())
                    .setUserName(mbirDTO.getCardHolderName())                   .setUserShortName(mbirDTO.getTerMerShortName())
                    .setIdentityType(new Integer(mbirDTO.getIdentityType()))    .setIdentityNum(mbirDTO.getIdentityNum())
                    .setPhone(mbirDTO.getPhone())                               .setMerchantType(mbirDTO.getMerType())
                    .setProvince(mbirDTO.getProvince())                         .setCity(mbirDTO.getCity())
                    .setAddress(mbirDTO.getAddress())                           .setStatus(StatusEnum._0.getStatus())
                    .setUpdateTime(new Date());

            RegisterCollectTable registerCollectTable = new RegisterCollectTable()
                    .setProductGroupId(mbirDTO.getProductGroupType())           .setRitId(registerInfoTable.getId())
                    .setOrganizationId(channelInfoTable.getOrganizationId())    .setMerchantId(mbirDTO.getMerId())
                    .setTerminalMerId(mbirDTO.getTerMerId())                    .setMerOrderId(mbirDTO.getMerOrderId())
                    .setCategory(mbirDTO.getCategory())                         .setMiMerCertPic1(mbirDTO.getMiMerCertPic1())
                    .setMiMerCertPic2(mbirDTO.getMiMerCertPic2())               .setBankCode(mbirDTO.getBankCode())
                    .setBankCardType(new Integer(mbirDTO.getBankCardType()))    .setCardHolderName(mbirDTO.getCardHolderName())
                    .setBankCardNum(mbirDTO.getBankCardNum())                   .setBankCardPhone(mbirDTO.getBankCardPhone())
                    .setPayFee(new BigDecimal(mbirDTO.getPayFee()))             .setBackFee(new BigDecimal(mbirDTO.getBackFee()))
                    .setChannelRespResult(null)                                 .setCrossRespResult(null)
                    .setStatus(StatusEnum._3.getStatus())                       .setMerchantRateTableCollect(merchantRateTableList)
                    .setCreateTime(new Date())                                  .setBussType(BusinessTypeEnum.b1.getBusiType()) //基础信息登记
                    .setUpdateTime(new Date());
            synchronized (b1) {
                StringBuilder sb = new StringBuilder()
                        .append("B1RXH")
                        .append(count.incrementAndGet())
                        .append("*")
                        .append(java.util.UUID.randomUUID().toString().replaceAll("-", ""))
                        .append(System.currentTimeMillis());
                registerCollectTable.setPlatformOrderId(sb.toString());
            }
            //保持或更新
            dbCommonRPCComponent.apiRegisterInfoService.saveOrUpdate(registerInfoTable);
            dbCommonRPCComponent.apiRegisterCollectService.save(registerCollectTable);

            return new Tuple2(registerInfoTable,registerCollectTable);
        }catch (Exception e){

            if(e instanceof NewPayException){
                NewPayException npe = (NewPayException) e;
                throw  npe;
            }else {
                e.printStackTrace();
                throw new NewPayException(
                        ResponseCodeEnum.RXH99999.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息: %s ；B1代码所在位置：%s,异常根源：保存进件信息发生异常,异常信息:%s",
                                ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint, e.getMessage()),
                        format(" %s", ResponseCodeEnum.RXH99999.getMsg())
                );
            }
        }
    }

    public RequestCrossMsgDTO getRequestCrossMsgDTO(Tuple2 tuple) {
        Tuple3<ChannelExtraInfoTable, RegisterInfoTable,RegisterCollectTable> tuple3 = (Tuple3<ChannelExtraInfoTable,  RegisterInfoTable,RegisterCollectTable>) tuple;
        return new RequestCrossMsgDTO()
                .setChannelExtraInfoTable(tuple3._1)
                .setRegisterInfoTable(tuple3._2)
                .setRegisterCollectTable(tuple3._3);
    }

    @Override
    public RegisterCollectTable updateByRegisterCollectTable(CrossResponseMsgDTO crossResponseMsgDTO, String crossResponseMsg, RegisterCollectTable registerCollectTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="updateByRegisterCollectTable";
        registerCollectTable
                .setStatus( null== crossResponseMsgDTO ?  StatusEnum._1.getStatus() : Integer.valueOf(crossResponseMsgDTO.getCrossStatusCode()) )
                .setCrossRespResult(crossResponseMsg)
                .setUpdateTime(new Date())
                .setChannelRespResult( null== crossResponseMsgDTO ?  null : crossResponseMsgDTO.getChannelResponseMsg() );
        try {
            dbCommonRPCComponent.apiRegisterCollectService.updateByWhereCondition(new RegisterCollectTable()
                    .setStatus(registerCollectTable.getStatus() )
                    .setCrossRespResult(crossResponseMsg)
                    .setUpdateTime(registerCollectTable.getUpdateTime())
                    .setPlatformOrderId(registerCollectTable.getPlatformOrderId())
                    .setChannelRespResult( registerCollectTable.getChannelRespResult() ));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：更新进件信息发生异常,异常信息：%s",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint,e.getMessage()),
                    format(" %s",ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        return registerCollectTable;
    }

    @Override
    public List<ChannelInfoTable> getChannelInfoByMerSetting(List<MerchantSettingTable> list, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getChannelInfoByMerSetting";
        Set<String>  channelIdSet = list.stream().map(MerchantSettingTable::getChannelId).collect(Collectors.toSet());
        List<ChannelInfoTable>   channelInfoTableList=null;
        try {
            channelInfoTableList = dbCommonRPCComponent.apiChannelInfoService.batchGetByChannelId(channelIdSet);
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；B1代码所在位置：%s,异常根源：批量获取商户配置表中的通道信息发生异常,异常信息：%s",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH99999.getMsg(),localPoint,e.getMessage()),
                    format(" %s",ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isHasNotElement(channelInfoTableList,
                ResponseCodeEnum.RXH00020.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00020.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00020.getMsg()));
        return channelInfoTableList;
    }

    @Override
    public List<ProductGroupTypeTable> getProductGroupTypeInfo(String productGroupType, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getProductGroupTypeInfo";
        List<ProductGroupTypeTable> productGroupTypeTableList = null;
        try{
            productGroupTypeTableList = dbCommonRPCComponent.ApiProductGroupTypeService.getList(new ProductGroupTypeTable()
                    .setProductGroupId(productGroupType)
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置B2：%s,异常根源：查询产品组信息发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isHasNotElement(productGroupTypeTableList,
                ResponseCodeEnum.RXH00063.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00063.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00063.getMsg()));
        return productGroupTypeTableList;
    }

    @Override
    public Set<ChannelInfoTable> filtrationChannelInfo(List<ProductGroupTypeTable> productGroupTypeTableList, List<ChannelInfoTable> channelInfoTableList, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="filtrationChannelInfo";
        Set<ChannelInfoTable> set = new HashSet<>(channelInfoTableList.size());
        productGroupTypeTableList.forEach(pgt->{
            channelInfoTableList.forEach(cit->{
                if(pgt.getOrganizationId().equalsIgnoreCase(cit.getOrganizationId()))
                    set.add(cit);
            });
        });
        isHasNotElement(set,
                ResponseCodeEnum.RXH00064.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00064.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00064.getMsg()));
        return set;
    }


    @Override
    public RegisterCollectTable getRegisterCollectTable(String platformOrderId,String busiType, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getRegisterCollectTable";
        RegisterCollectTable rct = null;
        try {
            rct = dbCommonRPCComponent.apiRegisterCollectService.getOne(new RegisterCollectTable()
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
                    .setPlatformOrderId(platformOrderId)
                    .setBussType(busiType)
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置B2：%s,异常根源：查询平台订单是否存在发生异常,异常信息：%s", ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(rct,
                ResponseCodeEnum.RXH00025.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00025.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00025.getMsg()));
        return rct;
    }

    @Override
    public void checkRepetitionOperation(RegisterCollectTable rct,String busiType,InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="saveOnRegisterInfo";
        RegisterCollectTable  rct2= null;
        try {
            rct2 = dbCommonRPCComponent.apiRegisterCollectService.getOne(new RegisterCollectTable()
                            .setMerchantId(ipo.getMerId())
                            .setTerminalMerId(ipo.getTerMerId())
                            .setBussType(busiType)
                            .setStatus(StatusEnum._0.getStatus())
//                    .setChannelId(rct.getChannelId())
                            .setBankCardNum(rct.getBankCardNum())
                            .setBankCardPhone(rct.getBankCardPhone())
            );
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询平台订单是否有重复操作发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }

        isNotNull(rct2,
                ResponseCodeEnum.RXH00045.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00045.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00045.getMsg()));

    }

    @Override
    public synchronized Tuple2<RegisterInfoTable,RegisterCollectTable> saveOnRegisterInfo(RegisterCollectTable registerCollectTable, MerBankCardBindDTO mbcbDTO, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="saveOnRegisterInfo";
        try {

            List<MerchantRateTable>   merchantRateTableList = dbCommonRPCComponent.apiMerchantRateService.getList(new MerchantRateTable()
                    .setMerchantId(ipo.getMerId())
                    .setStatus(StatusEnum._0.getStatus()));
            isHasNotElement(merchantRateTableList,
                    ResponseCodeEnum.RXH00054.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00054.getMsg(),localPoint),
                    format(" %s",ResponseCodeEnum.RXH00054.getMsg()));

            RegisterInfoTable  registerInfoTable = dbCommonRPCComponent.apiRegisterInfoService.getOne(new RegisterInfoTable()
                    .setId(registerCollectTable.getRitId()));

            isNull(registerInfoTable,
                    ResponseCodeEnum.RXH00027.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：根据进件副本，无法找到主表",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00027.getMsg(),localPoint),
                    format(" %s",ResponseCodeEnum.RXH00027.getMsg()));

            registerInfoTable
                    .setIdentityType(Integer.valueOf(mbcbDTO.getIdentityType()))
                    .setIdentityNum(mbcbDTO.getIdentityNum())
                    .setProvince(mbcbDTO.getProvince())
                    .setCity(mbcbDTO.getCity())
                    .setUpdateTime(new Date());
            registerCollectTable
                    .setId(null)
                    .setStatus(StatusEnum._3.getStatus())
                    .setBankAccountProp(Integer.valueOf(mbcbDTO.getBankAccountProp()))
                    .setBankCode(mbcbDTO.getBankCode())
                    .setBankCardType(Integer.valueOf(mbcbDTO.getBankCardType()))
                    .setCardHolderName(mbcbDTO.getCardHolderName())
                    .setBankCardNum(mbcbDTO.getBankCardNum())
                    .setBankCardPhone(mbcbDTO.getBankCardPhone())
                    .setBussType(BusinessTypeEnum.b2.getBusiType())
                    .setMerchantRateTableCollect(merchantRateTableList)
                    .setCreateTime(new Date())
                    .setUpdateTime(new Date())
                    .setCrossRespResult(null)
                    .setChannelRespResult(null);
            synchronized (b2) {
                StringBuilder sb = new StringBuilder()
                        .append("B2RXH")
                        .append(count.incrementAndGet())
                        .append("*")
                        .append(java.util.UUID.randomUUID().toString().replaceAll("-", ""))
                        .append(System.currentTimeMillis());
                registerCollectTable.setPlatformOrderId(sb.toString());
            }


            dbCommonRPCComponent.apiRegisterInfoService.saveOrUpdate(registerInfoTable);
            dbCommonRPCComponent.apiRegisterCollectService.save(registerCollectTable);

            return new Tuple2<>(registerInfoTable,registerCollectTable);
        }catch (Exception e){
            if(e instanceof NewPayException){
                NewPayException npe = (NewPayException) e;
                throw  npe;
            }else {
                e.printStackTrace();
                throw new NewPayException(
                        ResponseCodeEnum.RXH99999.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置B2：%s,异常信息：%s",
                                ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint, e.getMessage()),
                        format(" %s", ResponseCodeEnum.RXH99999.getMsg())
                );
            }
        }
    }



    @Override
    public RegisterCollectTable saveRegisterCollectTableByB3(RegisterCollectTable registerCollectTable, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="saveRegisterCollectTableByB3";
        synchronized (b3) {
            StringBuilder sb = new StringBuilder()
                    .append("B3RXH")
                    .append(count.incrementAndGet())
                    .append("*")
                    .append(java.util.UUID.randomUUID().toString().replaceAll("-", ""))
                    .append(System.currentTimeMillis());
            registerCollectTable.setPlatformOrderId(sb.toString());
        }
        registerCollectTable
                .setBussType(BusinessTypeEnum.b3.getBusiType())
                .setCrossRespResult(null)
                .setChannelRespResult(null)
                .setUpdateTime(new Date())
                .setCreateTime(new Date())
                .setStatus(StatusEnum._3.getStatus());
        List<MerchantRateTable> merchantRateTableList = null;

        try{

            merchantRateTableList = dbCommonRPCComponent.apiMerchantRateService.getList(new MerchantRateTable()
                    .setMerchantId(ipo.getMerId())
                    .setStatus(StatusEnum._0.getStatus()));

            isHasNotElement(merchantRateTableList,
                    ResponseCodeEnum.RXH00054.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s",
                            ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00054.getMsg(),localPoint),
                    format(" %s",ResponseCodeEnum.RXH00054.getMsg()));

            dbCommonRPCComponent.apiRegisterCollectService.save(registerCollectTable);
        }catch (Exception e){
            if(e instanceof NewPayException){
                NewPayException npe = (NewPayException) e;
                throw  npe;
            }else{
                e.printStackTrace();
                throw new NewPayException(
                        ResponseCodeEnum.RXH99999.getCode(),
                        format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置B3：%s,异常根源：保存进件附属信息,异常信息：%s",
                                ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                        format(" %s", ResponseCodeEnum.RXH99999.getMsg())
                );
            }
        }

        return registerCollectTable.setMerchantRateTableCollect(merchantRateTableList);
    }



    @Override
    public Map<String, ParamRule> getParamMapByB1() {
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//固定为MD5
                put("productGroupType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 64));//产品组合类型
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("merOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 32));// 商户订单号
                put("merType", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 2));//商户类型 商户类型	00公司商户，01个体商户
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id
                put("terMerName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//终端客户名称
                put("terMerShortName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//  商户简称
                put("category", new ParamRule(ParamTypeEnum.STRING.getType(), 3,16));// 经营项目
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//  证件类型 证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码
                put("phone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));// 手机号
                put("province", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 16));// 省份
                put("city", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 16));// 城市
                put("address", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 128));// 详细地址
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));// 银行简称	如：中国农业银行： ABC，中国工商银行： ICBC
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//  卡号类型	1借记卡  2信用卡	否	1
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//银行卡号
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//银行卡持卡人
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11,11));// 银行卡手机号
                put("payFee", new ParamRule(ParamTypeEnum.STRING.getType(), 1,8));//扣款手续费	用户扣款费率，单位： %，如2.8	否	8
                put("backFee", new ParamRule(ParamTypeEnum.STRING.getType(), 1,8));//代付手续费	用户还款费率,单位：元/笔,保留两位小数	否	8
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 256));//签名字符串
            }
        };
    }

    public boolean multipleOrder(String merOrderId,InnerPrintLogObject ipo) throws NewPayException{
        final String localPoint="multipleOrder";
        List<RegisterCollectTable> list = null;
        try {
            list = dbCommonRPCComponent.apiRegisterCollectService.getList(new RegisterCollectTable()
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
                    .setMerOrderId(merOrderId)
            );
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置B3：%s,异常根源：查询订单是否重发生异常，异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isHasElement(list,
                ResponseCodeEnum.RXH00009.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s 代码所在位置：%s",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00009.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00009.getMsg()));
        return false;
    }

    @Override
    public Map<String, ParamRule> getParamMapByB2(){

        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
//                put("merOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));// 商户订单号
                put("platformOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 平台流水号
                put("merchantType", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 2));//商户类型
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id
                put("bankAccountProp", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//账户属性	0：个人账户，1：对公账户
                put("identityType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));//  证件类型 证件类型	1身份证、2护照、3港澳回乡证、4台胞证、5军官证、	否	1
                put("identityNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//证件号码
                put("bankCode", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));// 银行简称	如：中国农业银行： ABC，中国工商银行： ICBC	否	16
                put("bankCardPhone", new ParamRule(ParamTypeEnum.PHONE.getType(), 11, 11));//银行卡手机号
                put("bankCardType", new ParamRule(ParamTypeEnum.STRING.getType(), 1, 1));// 卡号类型	1借记卡  2信用卡	否	1
                put("cardHolderName", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 32));//银行卡持卡人
                put("bankCardNum", new ParamRule(ParamTypeEnum.STRING.getType(), 12, 32));//银行卡号
                put("province", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));// 省份
                put("city", new ParamRule(ParamTypeEnum.STRING.getType(), 2, 16));// 城市
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }

    @Override
    public  Map<String, ParamRule> getParamMapByB3(){
        return new HashMap<String, ParamRule>() {
            {
                put("charset", new ParamRule(ParamTypeEnum.STRING.getType(), 5, 5));//参数字符集编码 固定UTF-8
                put("signType", new ParamRule(ParamTypeEnum.STRING.getType(), 3, 3));//签名类型	固定为MD5
                put("platformOrderId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));// 平台流水号
                put("merId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 32));//商户号
                put("terMerId", new ParamRule(ParamTypeEnum.STRING.getType(), 6, 64));//子商户id
                put("signMsg", new ParamRule(ParamTypeEnum.STRING.getType(), 16, 256));//签名字符串
            }
        };
    }
}
