package com.internal.playment.inward.service.impl;

import com.internal.playment.common.enums.ResponseCodeEnum;
import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.InnerPrintLogObject;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.table.agent.AgentMerchantSettingTable;
import com.internal.playment.common.table.agent.AgentMerchantWalletTable;
import com.internal.playment.common.table.agent.AgentMerchantsDetailsTable;
import com.internal.playment.common.table.business.PayOrderInfoTable;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.common.table.channel.ChannelDetailsTable;
import com.internal.playment.common.table.channel.ChannelInfoTable;
import com.internal.playment.common.table.channel.ChannelWalletTable;
import com.internal.playment.common.table.merchant.MerchantInfoTable;
import com.internal.playment.common.table.merchant.MerchantRateTable;
import com.internal.playment.common.table.merchant.MerchantWalletTable;
import com.internal.playment.common.table.merchant.MerchantsDetailsTable;
import com.internal.playment.common.table.terminal.TerminalMerchantsDetailsTable;
import com.internal.playment.common.table.terminal.TerminalMerchantsWalletTable;
import com.internal.playment.common.tuple.Tuple2;
import com.internal.playment.inward.service.CommonServiceAbstract;
import com.internal.playment.inward.service.PayWalletService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/28
 * Time: 下午6:40
 * Description:
 */
@Service
public class PayWalletServiceImpl extends CommonServiceAbstract implements PayWalletService {


    @Override
    public MerchantInfoTable getMerInfo(InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getMerInfo(InnerPrintLogObject ipo)";
        MerchantInfoTable mit = null;
        try {
            mit = dbCommonRPCComponent.apiMerchantInfoService.getOne(new MerchantInfoTable().setMerchantId(ipo.getMerId()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询商户信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(mit,
                ResponseCodeEnum.RXH00017.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s;",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00017.getMsg(),localPoint),
                format(" %s",ResponseCodeEnum.RXH00017.getMsg()));
        return mit;
    }

    @Override
    public MerchantRateTable getMerRate(InnerPrintLogObject ipo, String ...args) throws NewPayException {
        final String localPoint="getMerRate(PayOrderInfoTable poi, InnerPrintLogObject ipo)";
        MerchantRateTable mrt = null;
        try{
            mrt = dbCommonRPCComponent.apiMerchantRateService.getOne(new MerchantRateTable()
                    .setProductId(args[0])
                    .setMerchantId(ipo.getMerId())
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询商户产品费率信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg())
            );
        }
        isNull(mrt,
                ResponseCodeEnum.RXH00041.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；产品类型：%s,代码所在位置：%s;",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00041.getMsg(),args[0],localPoint),
                format(" %s",ResponseCodeEnum.RXH00041.getMsg()));
        return mrt;
    }

    @Override
    public MerchantWalletTable getMerWallet(InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getMerWallet(InnerPrintLogObject ipo)";
        MerchantWalletTable mwt = null;
        try{
            mwt = dbCommonRPCComponent.apiMerchantWalletService.getOne(new MerchantWalletTable()
                    .setMerchantId(ipo.getMerId())
                    .setStatus(StatusEnum._0.getStatus())
            );
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询商户钱包信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg()) );
        }
        return  null == mwt ? new MerchantWalletTable() : mwt ;
    }
    @Override
    public ChannelWalletTable getChanWallet(String channelId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getChanWallet(String channelId, PayOrderInfoTable poi)";
        ChannelWalletTable cwt = null;
        try{
            cwt = dbCommonRPCComponent.apiChannelWalletService.getOne(new ChannelWalletTable().setChannelId(channelId));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询通道钱包信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg()) );
        }

        return null==cwt ? new ChannelWalletTable() : cwt ;
    }

    @Override
    public ChannelInfoTable getChannelInfo(String channelId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint=" getChannelInfo(String channelId, InnerPrintLogObject ipo)";
        ChannelInfoTable cit = null;
        try{
            cit = dbCommonRPCComponent.apiChannelInfoService.getOne(new ChannelInfoTable().setChannelId(channelId));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询通道信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg()) );
        }
        isNull(cit,
                ResponseCodeEnum.RXH00022.getCode(),
                format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,错误根源：通道id（%s）不存在，或者通道被禁用",
                        ipo.getBussType(),ipo.getMerId(),ipo.getTerMerId(),ResponseCodeEnum.RXH00022.getMsg(),localPoint,channelId),
                format(" %s",ResponseCodeEnum.RXH00022.getMsg()));
        return cit;
    }


    @Override
    public AgentMerchantSettingTable getAgentMerSet(String agentMerchantId, String  productId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getAgentMerSet(String agentMerchantId, InnerPrintLogObject ipo)";
        AgentMerchantSettingTable ams = null;
        try{
            ams = dbCommonRPCComponent.apiAgentMerchantSettingService.getOne(new AgentMerchantSettingTable()
                    .setAgentMerchantId(agentMerchantId)
                    .setProductId(productId)
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询代理设置信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg()) );
        }
        return ams;
    }

    @Override
    public AgentMerchantWalletTable getAgentMerWallet(String agentMerchantId, InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getAgentMerWallet(String agentMerchantId, InnerPrintLogObject ipo)";
        AgentMerchantWalletTable amw = null ;
        try{
            amw = dbCommonRPCComponent.apiAgentMerchantWalletService.getOne(new AgentMerchantWalletTable().setAgentMerchantId(agentMerchantId));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询代理钱包信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg()) );
        }
        return null == amw ? new AgentMerchantWalletTable() : amw ;
    }



    @Override
    public TerminalMerchantsWalletTable getTerMerWallet(InnerPrintLogObject ipo) throws NewPayException {
        final String localPoint="getTerMerWallet(InnerPrintLogObject ipo)";
        TerminalMerchantsWalletTable tmw = null;
        try{
            tmw = dbCommonRPCComponent.apiTerminalMerchantsWalletService.getOne(new TerminalMerchantsWalletTable()
                    .setMerchantId(ipo.getMerId())
                    .setTerminalMerId(ipo.getTerMerId())
                    .setStatus(StatusEnum._0.getStatus()));
        }catch (Exception e){
            e.printStackTrace();
            throw new NewPayException(
                    ResponseCodeEnum.RXH99999.getCode(),
                    format("%s-->商户号：%s；终端号：%s；错误信息: %s ；代码所在位置：%s,异常根源：查询终端商户钱包信息发生异常,异常信息：%s",
                            ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(), ResponseCodeEnum.RXH99999.getMsg(), localPoint,e.getMessage()),
                    format(" %s", ResponseCodeEnum.RXH99999.getMsg()) );
        }

        return null == tmw ? new TerminalMerchantsWalletTable() : tmw ;
    }

    @Override
    public Tuple2<MerchantWalletTable, MerchantsDetailsTable> updateMerWalletByPayOrder(MerchantWalletTable mwt, PayOrderInfoTable poi, MerchantRateTable mrt) {
        //订单金额
        BigDecimal amount = poi.getAmount();
        //总订单金额
        BigDecimal totalAmount = ( null == mwt.getTotalAmount() ?  amount :  mwt.getTotalAmount().add(amount) );
        BigDecimal rateFee = (null == mrt.getRateFee() ? new BigDecimal(0) : mrt.getRateFee().divide(new BigDecimal(100)) );
        BigDecimal singleFee = (null == mrt.getSingleFee() ? new BigDecimal(0) : mrt.getSingleFee() );
        //单笔总费用
        BigDecimal totalSingleFee = ( amount.multiply(rateFee).setScale(2, BigDecimal.ROUND_UP ) .add(singleFee)) ;
        //入账金额
        BigDecimal inAmount = amount.subtract(totalSingleFee);
        //总入账金额
        BigDecimal totalIncomeAmount = (null == mwt.getIncomeAmount() ? inAmount :  mwt.getIncomeAmount().add(inAmount) );
        //总手续费用
        BigDecimal totalFee = ( null == mwt.getTotalFee() ? totalSingleFee : mwt.getTotalFee().add(totalSingleFee) );
        //终端手续费
        BigDecimal payFee = poi.getPayFee().divide(new BigDecimal(100));
        BigDecimal terMerFee = amount.multiply(payFee).setScale(2, BigDecimal.ROUND_UP );
        //商户单笔手续费利润 = 终端手续费 - 单笔总费用;
        BigDecimal merSingleFeeProfit = terMerFee.subtract(totalSingleFee);
        //手续费利润总和
        BigDecimal feeProfit = ( null == mwt.getFeeProfit() ? merSingleFeeProfit :  mwt.getFeeProfit().add(merSingleFeeProfit) );
        //保证金尚未考虑
        //................
        //总可用余额
        BigDecimal totalBalance =( null == mwt.getTotalBalance() ? inAmount : mwt.getTotalBalance().add(inAmount) );
        //总不可用余额
        BigDecimal totalUnavailableAmount = ( null == mwt.getTotalUnavailableAmount() ? new BigDecimal(0) : mwt.getTotalUnavailableAmount() );
        //总可用余额
        BigDecimal totalAvailableAmount = ( null== mwt.getTotalAvailableAmount() ?  new BigDecimal(0) : mwt.getTotalAvailableAmount() );
        //判断结算周期
        List<String>  settleCycleList= Arrays.asList("d0","D0","t0","T0");
        String settleCycle = isBlank(mrt.getSettleCycle()) ? "T7"  :  mrt.getSettleCycle().trim() ;
        if( !settleCycleList.contains(settleCycle) ){
            totalUnavailableAmount = totalUnavailableAmount.add(inAmount);
        }else{
            totalAvailableAmount = totalAvailableAmount.add(inAmount);
        }
        //商户钱包
        mwt.setId(  null == mwt.getId() ? null : mwt.getId() )
                .setMerchantId( poi.getMerchantId())
                .setTotalAmount(totalAmount)//总订单金额
                .setIncomeAmount(totalIncomeAmount)  //总入账金额
                .setOutAmount(mwt.getOutAmount())//总出帐金额
                .setTotalFee(totalFee)//总手续费用
                .setFeeProfit(feeProfit) //手续费利润总和
                .setTotalMargin(mwt.getTotalMargin()) //保证金尚未考虑
                .setTotalBalance(totalBalance)
                .setTotalAvailableAmount( totalAvailableAmount )
                .setTotalUnavailableAmount(totalUnavailableAmount)
                .setTotalFreezeAmount(mwt.getTotalFreezeAmount())
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime( null == mwt.getCreateTime() ? new Date() : mwt.getCreateTime())
                .setUpdateTime(new Date());
        //创建商户钱包明细
        MerchantsDetailsTable mdt = new MerchantsDetailsTable()
                .setId(null)
                .setMerchantId(poi.getMerchantId())
                .setProductId(poi.getProductId())
                .setMerOrderId(poi.getMerOrderId())
                .setPlatformOrderId(poi.getPlatformOrderId())
                .setAmount(poi.getAmount())
                .setInAmount(inAmount)
                .setOutAmount(new BigDecimal(0))
                .setRateFee(poi.getPayFee())
                .setFee(totalSingleFee)
                .setFeeProfit(merSingleFeeProfit)
                .setTotalBalance(totalBalance)
                .setTimestamp(System.currentTimeMillis())
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return new Tuple2<>(mwt,mdt);
    }



    @Override
    public Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> updateTerMerWalletByPayOrder(TerminalMerchantsWalletTable tmw, PayOrderInfoTable poi, MerchantRateTable mrt){
        //订单金额
        BigDecimal amount = poi.getAmount();
        //订单总金额
        BigDecimal totalAmount = (null == tmw.getTotalAmount() ? amount : tmw.getTotalAmount().add(amount) );
        //手续费率
        BigDecimal payFee = poi.getPayFee().divide(new BigDecimal(100));
        //手续费
        BigDecimal terMerFee = amount.multiply(payFee).setScale(2, BigDecimal.ROUND_UP );
        //总手续费
        BigDecimal totalTerMerFee = ( null == tmw.getTotalFee() ?  terMerFee : tmw.getTotalFee().add(terMerFee));
        //入账金额
        BigDecimal inAmount = amount.subtract(terMerFee);
        //入账总金额
        BigDecimal TotalIncomeAmount = ( null == tmw.getIncomeAmount() ? inAmount :
                tmw.getIncomeAmount().add(inAmount) );
        //总余额
        BigDecimal totalBalance = (null == tmw.getTotalBalance() ? inAmount : tmw.getTotalBalance().add(inAmount) );
        //总不可用余额
        BigDecimal totalUnavailableAmount = ( null == tmw.getTotalUnavailableAmount() ? new BigDecimal(0) : tmw.getTotalUnavailableAmount() );
        //总可用余额
        BigDecimal totalAvailableAmount = ( null== tmw.getTotalAvailableAmount() ?  new BigDecimal(0) : tmw.getTotalAvailableAmount() );
        //判断结算周期
        List<String>  settleCycleList= Arrays.asList("d0","D0","t0","T0");
        String settleCycle = isBlank(mrt.getSettleCycle()) ? "T7"  :  mrt.getSettleCycle().trim() ;
        if( !settleCycleList.contains(settleCycle) ){
            totalUnavailableAmount = totalUnavailableAmount.add(inAmount);
        }else{
            totalAvailableAmount = totalAvailableAmount.add(inAmount);
        }

        //钱包
        tmw.setId( null == tmw.getId() ? null : tmw.getId())
                .setMerchantId(poi.getMerchantId())
                .setTerminalMerId(poi.getTerminalMerId())
                .setTotalAmount(totalAmount) //总订单金额
                .setIncomeAmount(TotalIncomeAmount)   //入账总金额
                .setOutAmount(tmw.getOutAmount())//总出帐金额
                .setTotalBalance(totalBalance)//总余额
                .setTotalAvailableAmount(totalAvailableAmount)
                .setTotalUnavailableAmount(totalUnavailableAmount)
                .setTotalFee(totalTerMerFee)
                .setTotalMargin(tmw.getTotalMargin())//保证金尚未考虑
                .setTotalFreezeAmount(tmw.getTotalFreezeAmount())
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime( null == tmw.getCreateTime() ? new Date() : tmw.getCreateTime() )
                .setUpdateTime(new Date());
        //钱包明细
        TerminalMerchantsDetailsTable tmd = new TerminalMerchantsDetailsTable()
                .setId(null)
                .setMerchantId(poi.getMerchantId())
                .setTerminalMerId(poi.getTerminalMerId())
                .setProductId(poi.getProductId())
                .setMerOrderId(poi.getMerOrderId())
                .setPlatformOrderId(poi.getPlatformOrderId())
                .setAmount(poi.getAmount())
                .setInAmount(inAmount)
                .setOutAmount(new BigDecimal(0))
                .setRateFee( poi.getPayFee())
                .setFee(terMerFee)
                .setTotalBalance(totalBalance)
                .setTimestamp(System.currentTimeMillis())
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return new Tuple2<>(tmw,tmd);
    }


    @Override
    public Tuple2<ChannelWalletTable, ChannelDetailsTable> updateChannelWalletByPayOrder(ChannelWalletTable cwt, ChannelInfoTable cit, PayOrderInfoTable poi, MerchantRateTable mrt) {
        //订单金额
        BigDecimal amount = poi.getAmount();
        //通道费率
        BigDecimal chanRateFee = cit.getChannelRateFee().divide(new BigDecimal(100));
        BigDecimal singleFee = cit.getChannelSingleFee();
        //通道费用
        BigDecimal chanFee = amount.multiply(chanRateFee).setScale(2,BigDecimal.ROUND_UP);
        chanFee = chanFee.add(singleFee);
        //入账金额
        BigDecimal inAmount = amount.subtract(chanFee);
        //总入帐金额
        BigDecimal totalInAmount = ( null == cwt.getIncomeAmount() ? inAmount : cwt.getIncomeAmount().add(inAmount) );
        //总订单金额
        BigDecimal totalAmount = ( null == cwt.getTotalAmount() ? amount : cwt.getTotalAmount().add(amount) );
        //总手续费
        BigDecimal totalFee = ( null == cwt.getTotalFee() ? chanFee : cwt.getTotalFee().add(chanFee) );
        //计算商户费用
        BigDecimal merRateFee = (null == mrt.getRateFee() ? new BigDecimal(0) : mrt.getRateFee().divide(new BigDecimal(100)) );
        BigDecimal merSingleFee = (null == mrt.getSingleFee() ? new BigDecimal(0) : mrt.getSingleFee() );
        BigDecimal merFee = ( amount.multiply(merRateFee).setScale(2, BigDecimal.ROUND_UP ) .add(merSingleFee)) ;
        //通道利润 = 商户的费用 - 通道费用
        BigDecimal chanProfit = merFee.subtract(chanFee);
        //总余额
        BigDecimal totalBalance = (null == cwt.getTotalBalance() ? inAmount : cwt.getTotalBalance().add(inAmount));
        //总不可用余额
        BigDecimal totalUnavailableAmount = ( null == cwt.getTotalUnavailableAmount() ? new BigDecimal(0) : cwt.getTotalUnavailableAmount() );
        //总可用余额
        BigDecimal totalAvailableAmount = ( null== cwt.getTotalAvailableAmount() ?  new BigDecimal(0) : cwt.getTotalAvailableAmount() );
        //判断结算周期
        List<String>  settleCycleList= Arrays.asList("d0","D0","t0","T0");
        String settleCycle = isBlank(cit.getSettleCycle()) ? "T7"  :  cit.getSettleCycle().trim() ;
        if( !settleCycleList.contains(settleCycle) ){
            totalUnavailableAmount = totalUnavailableAmount.add(inAmount);
        }else{
            totalAvailableAmount = totalAvailableAmount.add(inAmount);
        }

        //通道钱包
        cwt.setId( null == cwt.getId() ? null : cwt.getId())
                .setChannelId( null == cwt.getChannelId() ? poi.getChannelId() : cwt.getChannelId())
                .setOrganizationId( null == cwt.getOrganizationId() ? cit.getOrganizationId() : cwt.getOrganizationId())
                .setTotalAmount(totalAmount)
                .setIncomeAmount(totalInAmount)
                .setOutAmount(cwt.getOutAmount())
                .setTotalFee(totalFee)
                .setFeeProfit( null == cwt.getFeeProfit() ? chanProfit : cwt.getFeeProfit().add(chanProfit) )
                .setTotalBalance(totalBalance)
                .setTotalAvailableAmount(totalAvailableAmount)
                .setTotalUnavailableAmount(totalUnavailableAmount)
                .setTotalMargin(cwt.getTotalMargin())
                .setTotalFreezeAmount(cwt.getTotalFreezeAmount())
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime(  null == cwt.getCreateTime() ? new Date() : cwt.getCreateTime())
                .setUpdateTime(new Date());
        //通道钱包明细
        ChannelDetailsTable cdt = new ChannelDetailsTable()
                .setId(null)
                .setChannelId(poi.getChannelId())
                .setOrganizationId(cit.getOrganizationId())
                .setProductId(poi.getProductId())
                .setMerOrderId(poi.getMerOrderId())
                .setPlatformOrderId(poi.getPlatformOrderId())
                .setAmount(poi.getAmount())
                .setInAmount(inAmount)
                .setOutAmount(new BigDecimal(0))
                .setChRateFee(cit.getChannelRateFee())
                .setMerRateFee(mrt.getRateFee())
                .setMerFee(merFee)
                .setChFee(chanFee)
                .setChFeeProfit(chanProfit)
                .setTotalBalance(totalBalance)
                .setTimestamp(System.currentTimeMillis())
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return  new Tuple2<>(cwt,cdt);
    }


    @Override
    public Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> updateAgentMerWalletByPayOrder(AgentMerchantWalletTable amw, AgentMerchantSettingTable ams, PayOrderInfoTable poi) {
        //订单金额
        BigDecimal amount = poi.getAmount();
        //总订单金额
        BigDecimal totalAmount = ( null == amw.getTotalAmount() ? amount : amw.getTotalAmount().add(amount) );
//        BigDecimal payFee = poi.getPayFee().divide(new BigDecimal(100));
        //订单入账总金额
//        BigDecimal inAmount = amount.multiply(payFee).setScale(2,BigDecimal.ROUND_UP);
//        BigDecimal incomeAmount = ( null == amw.getIncomeAmount() ? inAmount : amw.getIncomeAmount().add(inAmount) );

        //代理费率
        ams =  null == ams ?  new AgentMerchantSettingTable() : ams ;
        BigDecimal rateFee = ( null == ams.getRateFee() ? new BigDecimal(0) :  ams.getRateFee().divide(new BigDecimal(100)) );
        BigDecimal singleFee = ams.getSingleFee();
        singleFee = null == singleFee ? new BigDecimal(0) : singleFee;
        //代理商手续费
        BigDecimal agentMerFee = amount.multiply(rateFee).setScale(2,BigDecimal.ROUND_UP);
        agentMerFee = agentMerFee.add(singleFee);
        //代理商总手续费入账
        BigDecimal totalFee = ( null == amw.getTotalFee() ? agentMerFee : amw.getTotalFee().add(agentMerFee) );
        //代理商总余额
        BigDecimal totalBalance = ( null == amw.getTotalBalance() ? agentMerFee : amw.getTotalBalance().add(agentMerFee) );
        //总不可用余额
        BigDecimal totalUnavailableAmount = ( null == amw.getTotalUnavailableAmount() ? new BigDecimal(0) : amw.getTotalUnavailableAmount() );
        //总可用余额
        BigDecimal totalAvailableAmount = ( null== amw.getTotalAvailableAmount() ?  new BigDecimal(0) : amw.getTotalAvailableAmount() );
        //判断结算周期
        List<String>  settleCycleList= Arrays.asList("d0","D0","t0","T0");
        String settleCycle = isBlank(ams.getSettleCycle()) ? "T7"  :  ams.getSettleCycle().trim() ;
        if( !settleCycleList.contains(settleCycle) ){
            totalUnavailableAmount = totalUnavailableAmount.add(agentMerFee);
        }else{
            totalAvailableAmount = totalAvailableAmount.add(agentMerFee);
        }

        amw.setId( null == amw.getId() ? null : amw.getId())
                .setAgentMerchantId( null ==  amw.getAgentMerchantId() ?  ams.getAgentMerchantId() : amw.getAgentMerchantId() )
                .setTotalAmount(totalAmount)
                .setIncomeAmount(totalFee)
                .setOutAmount(amw.getOutAmount())//手续费出帐金额
                .setTotalBalance(totalBalance) //代理商总余额
                .setTotalAvailableAmount(totalAvailableAmount)
                .setTotalUnavailableAmount(totalUnavailableAmount)
                .setTotalFee(amw.getTotalFee())
                .setTotalFreezeAmount(amw.getTotalFreezeAmount())
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime( null == amw.getCreateTime() ? new Date() : amw.getCreateTime() )
                .setUpdateTime(new Date());

        AgentMerchantsDetailsTable amd = new AgentMerchantsDetailsTable()
                .setId(null)
                .setAgentMerchantId(ams.getAgentMerchantId())
                .setMerOrderId(poi.getMerOrderId())
                .setPlatformOrderId(poi.getPlatformOrderId())
                .setProductId(poi.getProductId())
                .setAmount(amount)
                .setInAmount(agentMerFee)
                .setOutAmount(new BigDecimal(0))
                .setRateFee(ams.getRateFee())
                .setFee(agentMerFee)
                .setTotalBalance(totalBalance)
                .setTimestamp(System.currentTimeMillis())
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return new Tuple2<>(amw,amd);
    }

    @Override
    public Tuple2<MerchantWalletTable, MerchantsDetailsTable> updateMerWalletByTransOrder(MerchantWalletTable mwt, TransOrderInfoTable toit, MerchantRateTable mrt) throws Exception {
        //订单金额
        BigDecimal amount = toit.getAmount();

        //总余额
        BigDecimal totalBalance = mwt.getTotalBalance();
        if(amount.compareTo(totalBalance) == 1)
            throw  new Exception(format("快捷MQ队列--->代付业务钱包更新: 订单金额(%s)>商户钱包总余额(%s)",amount,totalBalance));
        //总可用余额
        BigDecimal totalAvailableAmount = mwt.getTotalAvailableAmount();
        if(amount.compareTo(totalAvailableAmount) == 1)
            throw  new Exception(format("快捷MQ队列--->代付业务钱包更新: 订单金额(%s)>商户钱包总可用余额(%s)",amount,totalAvailableAmount));
        //商户费率
        BigDecimal rateFee = mrt.getRateFee();
        rateFee = null == rateFee ? new BigDecimal(0 ) : rateFee.divide(new BigDecimal(100));
        BigDecimal singleFee = mrt.getSingleFee();
        singleFee = null == singleFee ? new BigDecimal(0) : singleFee;
        BigDecimal merFee = amount.multiply(rateFee).setScale(2,BigDecimal.ROUND_UP);
        //商户费用
        merFee = merFee.add(singleFee);
        //商户总费用
        BigDecimal totalMerFee = mwt.getTotalFee();
        totalMerFee = null == totalMerFee ? new BigDecimal(0) : totalMerFee;
        totalMerFee = totalMerFee.add(merFee);
        //终端商户费用
        BigDecimal terFee = toit.getBackFee();
        if(isNull(terFee))
            throw  new Exception("快捷MQ队列--->代付业务钱包更新: backFee 为空");
        //商户单笔利润
        BigDecimal feeProfit = terFee.subtract(merFee);
        //商户总利润
        BigDecimal totalFeeProfit = mwt.getFeeProfit();
        totalFeeProfit = null == totalFeeProfit ? new BigDecimal(0) : totalFeeProfit ;
        totalFeeProfit = totalFeeProfit.add(feeProfit);
        //单笔出账金额 = 订单金额-商户单笔利润
        BigDecimal singleOutAmount = amount.subtract(feeProfit);
        //总出帐金额
        BigDecimal outAmount = mwt.getOutAmount();
        outAmount = null == outAmount ? new BigDecimal(0) : outAmount ;
        outAmount = outAmount.add(singleOutAmount);
        //总不可用余额
        BigDecimal totalUnavailableAmount = mwt.getTotalUnavailableAmount();
        //冻结资金
        BigDecimal totalFreezeAmount = mwt.getTotalFreezeAmount();
        totalFreezeAmount = null == totalFreezeAmount ? new BigDecimal(0) : totalFreezeAmount;
        //判断结算周期
        List<String>  settleCycleList= Arrays.asList("d0","D0","t0","T0");
        String settleCycle = isBlank(mrt.getSettleCycle()) ? "T7"  :  mrt.getSettleCycle().trim() ;
        if( !settleCycleList.contains(settleCycle) ){
            totalFreezeAmount = totalFreezeAmount.add(singleOutAmount);
            totalAvailableAmount = totalAvailableAmount.subtract(singleOutAmount);
        }else{
            totalAvailableAmount = totalAvailableAmount.subtract(singleOutAmount);
            totalBalance = totalBalance.subtract(singleOutAmount);
        }
        mwt
                .setOutAmount(outAmount)                        .setTotalFee(totalMerFee)
                .setFeeProfit(totalFeeProfit)                   .setTotalMargin(mwt.getTotalMargin())
                .setTotalBalance(totalBalance)                  .setTotalAvailableAmount(totalAvailableAmount)
                .setTotalUnavailableAmount(totalUnavailableAmount)
                .setTotalFreezeAmount(totalFreezeAmount)        .setUpdateTime(new Date());
        MerchantsDetailsTable mdt = new MerchantsDetailsTable()
                .setId(null)
                .setMerchantId(toit.getMerchantId())              .setProductId(toit.getProductId())
                .setMerOrderId(toit.getMerOrderId())              .setPlatformOrderId(toit.getPlatformOrderId())
                .setAmount(toit.getAmount())                      .setInAmount(null)
                .setOutAmount(singleOutAmount)                    .setRateFee(mrt.getRateFee())
                .setFee(merFee)                                   .setFeeProfit(feeProfit)
                .setTotalBalance(totalBalance)
                .setTimestamp(System.currentTimeMillis())         .setStatus(StatusEnum._0.getStatus())
                .setCreateTime(new Date())                        .setUpdateTime(new Date());

        return new Tuple2(mwt,mdt);
    }

    @Override
    public Tuple2<TerminalMerchantsWalletTable, TerminalMerchantsDetailsTable> updateTerMerWalletByTransOrder(TerminalMerchantsWalletTable tmw, TransOrderInfoTable toit, MerchantRateTable mrt) throws Exception {
        //订单金额
        BigDecimal amount = toit.getAmount();
        //总余额
        BigDecimal totalBalance = tmw.getTotalBalance();
        if(amount.compareTo(totalBalance) == 1 )
            throw new Exception(format("快捷MQ队列--->代付业务钱包更新: 订单金额(%s)>商户钱包总余额(%s)",amount,totalBalance));

        //总可用余额
        BigDecimal totalAvailableAmount = tmw.getTotalAvailableAmount();
        if(amount.compareTo(totalAvailableAmount) == 1)
            throw  new Exception(format("快捷MQ队列--->代付业务钱包更新: 订单金额(%s)>商户钱包总可用余额(%s)",amount,totalAvailableAmount));

        //终端费用
        BigDecimal backFee = toit.getBackFee();
        //总的终端费用
        BigDecimal totalFee = tmw.getTotalFee();
        totalFee = null == totalFee ? backFee :  totalFee.add(backFee);
        //单笔出账金额
        BigDecimal singleOutAmount = amount.subtract(backFee);
        //总出账金额
        BigDecimal totalOutAmount = tmw.getOutAmount();
        totalOutAmount = null == totalOutAmount ? new BigDecimal(0) : totalOutAmount;
        totalOutAmount = totalOutAmount.add(singleOutAmount);
        //总不可用余额
        BigDecimal totalUnavailableAmount = tmw.getTotalUnavailableAmount();
        //冻结资金
        BigDecimal totalFreezeAmount = tmw.getTotalFreezeAmount();
        totalFreezeAmount = null == totalFreezeAmount ? new BigDecimal(0) : totalFreezeAmount;
        //判断结算周期
        List<String>  settleCycleList= Arrays.asList("d0","D0","t0","T0");
        String settleCycle = isBlank(mrt.getSettleCycle()) ? "T7"  :  mrt.getSettleCycle().trim() ;
        if( !settleCycleList.contains(settleCycle) ){
            totalFreezeAmount = totalFreezeAmount.add(amount);
            totalAvailableAmount = totalAvailableAmount.subtract(amount);
        }else{
            totalAvailableAmount = totalAvailableAmount.subtract(amount);
            totalBalance = totalBalance.subtract(amount);
        }

        tmw
                .setOutAmount(totalOutAmount)
                .setTotalBalance(totalBalance)
                .setTotalAvailableAmount(totalAvailableAmount)
                .setTotalUnavailableAmount(totalUnavailableAmount)
                .setTotalFee(totalFee)
                .setTotalMargin(tmw.getTotalMargin())
                .setTotalFreezeAmount(totalFreezeAmount)
                .setUpdateTime(new Date());

        TerminalMerchantsDetailsTable tmd = new TerminalMerchantsDetailsTable()
                .setId(null)
                .setMerchantId(toit.getMerchantId())
                .setTerminalMerId(toit.getTerminalMerId())
                .setProductId(toit.getProductId())
                .setMerOrderId(toit.getMerOrderId())
                .setPlatformOrderId(toit.getPlatformOrderId())
                .setAmount(amount)
                .setInAmount(null)
                .setOutAmount(singleOutAmount)
                .setRateFee(null)
                .setFee(backFee)
                .setTotalBalance(totalBalance)
                .setTimestamp(System.currentTimeMillis())
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());

        return new Tuple2(tmw,tmd);
    }


    @Override
    public Tuple2<ChannelWalletTable, ChannelDetailsTable> updateChannelWalletByTransOrder(ChannelWalletTable cwt, ChannelInfoTable cit, TransOrderInfoTable toit, MerchantRateTable mrt) throws Exception {
        //订单金额
        BigDecimal amount = toit.getAmount();
        //总可用余额
        BigDecimal totalBalance = cwt.getTotalBalance();
        if(amount.compareTo(totalBalance) == 1 )
            throw new Exception(format("快捷MQ队列--->代付业务钱包更新: 订单金额(%s)>商户钱包总余额(%s)",amount,totalBalance));
        //总可用余额
        BigDecimal totalAvailableAmount = cwt.getTotalAvailableAmount();
        if(amount.compareTo(totalAvailableAmount) == 1)
            throw  new Exception(format("快捷MQ队列--->代付业务钱包更新: 订单金额(%s)>商户钱包总可用余额(%s)",amount,totalAvailableAmount));
        //通道费用
        BigDecimal chRateFee = cit.getChannelRateFee();
        chRateFee = null == chRateFee ? new BigDecimal(0) : chRateFee;
        chRateFee = chRateFee.divide(new BigDecimal(100));
        BigDecimal chSingleFee = cit.getChannelSingleFee();
        BigDecimal chFee = amount.multiply(chRateFee).setScale(2,BigDecimal.ROUND_UP);
        BigDecimal totalSingleFee =chFee.add(chSingleFee);
        //商户费率
        BigDecimal merRateFee = mrt.getRateFee();
        merRateFee = null == merRateFee ? new BigDecimal(0) : merRateFee;
        merRateFee = merRateFee.divide(new BigDecimal(100));
        BigDecimal merSingleFee = mrt.getSingleFee();
        BigDecimal merFee = amount.multiply(merRateFee).setScale(2,BigDecimal.ROUND_UP);
        BigDecimal merTotalSingleFee = merFee.add(merSingleFee);
        //通道单笔利润
        BigDecimal singleProfit = merTotalSingleFee.subtract(totalSingleFee);
        //通道总利润
        BigDecimal feeProfit = cwt.getFeeProfit();
        feeProfit = null == feeProfit ? singleProfit : feeProfit.add(singleProfit);
        //总费用
        BigDecimal totalFee = cwt.getTotalFee();
        totalFee = totalFee == null ? totalSingleFee : totalFee.add(totalSingleFee);
        //子商户出账金额
        BigDecimal terOutAmount = amount.subtract(toit.getBackFee());
        //单笔输出金额 = 子商户出账金额 - 通道单笔所有费用
        BigDecimal singleOutAmount = terOutAmount.subtract(totalSingleFee);
        //总输出金额
        BigDecimal totalOutAmount = cwt.getOutAmount();
        totalOutAmount = null == totalOutAmount ? singleOutAmount : totalOutAmount.add(singleOutAmount);
        //总不可用余额
        BigDecimal totalUnavailableAmount = cwt.getTotalUnavailableAmount();
        //冻结资金
        BigDecimal totalFreezeAmount = cwt.getTotalFreezeAmount();
        totalFreezeAmount = null == totalFreezeAmount ? new BigDecimal(0) : totalFreezeAmount;
        //判断结算周期
        List<String>  settleCycleList= Arrays.asList("d0","D0","t0","T0");
        String settleCycle = isBlank(cit.getSettleCycle()) ? "T7"  :  cit.getSettleCycle().trim() ;
        if( !settleCycleList.contains(settleCycle) ){
            totalFreezeAmount = totalFreezeAmount.add(singleOutAmount);
            totalAvailableAmount = totalAvailableAmount.subtract(singleOutAmount);
        }else{
            totalAvailableAmount = totalAvailableAmount.subtract(singleOutAmount);
            totalBalance = totalBalance.subtract(singleOutAmount);
        }
        cwt
                .setTotalAmount(cwt.getTotalAmount())
                .setOutAmount(totalOutAmount)
                .setTotalFee(totalFee)
                .setFeeProfit(feeProfit)
                .setTotalBalance(totalBalance)
                .setTotalAvailableAmount(totalAvailableAmount)
                .setTotalUnavailableAmount(totalUnavailableAmount)
                .setTotalFreezeAmount(totalFreezeAmount)
                .setUpdateTime(new Date());
        ChannelDetailsTable cdt = new ChannelDetailsTable()
                .setChRateFee(cit.getChannelRateFee())
                .setChFee(totalSingleFee)
                .setChFeeProfit(singleProfit)
                .setMerRateFee(mrt.getRateFee())
                .setMerFee(merTotalSingleFee)
                .setId(null)
                .setChannelId(toit.getChannelId())
                .setOrganizationId(cit.getOrganizationId())
                .setProductId(toit.getProductId())
                .setMerOrderId(toit.getMerOrderId())
                .setPlatformOrderId(toit.getPlatformOrderId())
                .setAmount(amount)
                .setInAmount(null)
                .setOutAmount(singleOutAmount)
                .setTotalBalance(totalBalance)
                .setTimestamp(System.currentTimeMillis())
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());
        return new Tuple2(cwt,cdt);
    }

    @Override
    public Tuple2<AgentMerchantWalletTable, AgentMerchantsDetailsTable> updateAgentMerWalletByTransOrder(AgentMerchantWalletTable amw, AgentMerchantSettingTable ams, TransOrderInfoTable toit) {

        //订单金额
        BigDecimal amount = toit.getAmount();
        //代理商费用
        BigDecimal agentRateFee = ams.getRateFee();
        agentRateFee = null == agentRateFee ? new BigDecimal(0) :  agentRateFee;
        agentRateFee = agentRateFee.divide(new BigDecimal(100));
        BigDecimal agentFee = amount.multiply(agentRateFee).setScale(2,BigDecimal.ROUND_UP);
        BigDecimal agentSingleFee = ams.getSingleFee();
        BigDecimal totalSingleFee = agentFee.add(agentSingleFee);
        //总入账金额
        BigDecimal totalInAmount = amw.getIncomeAmount();
        totalInAmount = null == totalInAmount ? totalSingleFee : totalInAmount.add(totalSingleFee);
        //总可用余额
        BigDecimal totalBalance = amw.getTotalBalance();
        totalBalance = totalBalance.add(totalSingleFee);
        //总可用余额
        BigDecimal totalAvailableAmount = amw.getTotalAvailableAmount();
        //总不可用余额
        BigDecimal totalUnavailableAmount = amw.getTotalUnavailableAmount();

        //判断结算周期
        List<String>  settleCycleList= Arrays.asList("d0","D0","t0","T0");
        String settleCycle = isBlank(ams.getSettleCycle()) ? "T7"  :  ams.getSettleCycle().trim() ;
        if( !settleCycleList.contains(settleCycle) ){
            totalUnavailableAmount = totalUnavailableAmount.add(totalSingleFee);
        }else{
            totalAvailableAmount = totalAvailableAmount.add(totalSingleFee);
        }
        amw
                .setTotalAmount(amw.getTotalAmount())
                .setIncomeAmount(totalInAmount)
                .setOutAmount(amw.getOutAmount())
                .setTotalBalance(totalBalance)
                .setTotalAvailableAmount(totalAvailableAmount)
                .setTotalUnavailableAmount(totalUnavailableAmount)
                .setTotalFee(amw.getTotalFee())
                .setTotalFreezeAmount(amw.getTotalFreezeAmount())
                .setUpdateTime(new Date());

        AgentMerchantsDetailsTable amdt = new AgentMerchantsDetailsTable()
                .setId(null)
                .setAgentMerchantId(toit.getMerchantId())
                .setMerOrderId(toit.getMerOrderId())
                .setPlatformOrderId(toit.getPlatformOrderId())
                .setProductId(toit.getProductId())
                .setAmount(amount)
                .setInAmount(totalSingleFee)
                .setOutAmount(null)
                .setRateFee( ams.getRateFee())
                .setFee(totalSingleFee)
                .setTotalBalance(totalBalance)
                .setTimestamp(System.currentTimeMillis())
                .setStatus(StatusEnum._0.getStatus())
                .setCreateTime(new Date())
                .setUpdateTime(new Date());

        return new Tuple2(amw,amdt);
    }

    @Override
    public void checkPayOrderOperability(PayOrderInfoTable poi, InnerPrintLogObject ipo) throws Exception {
        PayOrderInfoTable payOrderInfoTable =  dbCommonRPCComponent.apiPayOrderInfoService.getOne(
                new PayOrderInfoTable()
                        .setMerchantId(poi.getMerchantId())
                        .setTerminalMerId(poi.getTerminalMerId())
                        .setMerOrderId(poi.getMerOrderId())
                        .setPlatformOrderId(poi.getPlatformOrderId())
        );
        if(isNull(payOrderInfoTable))
            throw  new Exception(format("该订单在数据库中不存在：[%s]",poi.toString()));
        Set<Integer> set = new HashSet(Arrays.asList(StatusEnum._7.getStatus(),StatusEnum._8.getStatus()));
        if( !set.contains(payOrderInfoTable.getStatus()) )
            throw  new Exception(format("\n====================================================================\n" +
                            "该订单不在钱包处理范围:\n" +
                            "队列中的订单:[%s]\n" +
                            "数据库的订单:[%s]\n" +
                            "====================================================================\n",
                    poi.toString(),payOrderInfoTable.toString()));
    }

    @Override
    public void checkTransOrderOperability(TransOrderInfoTable toit, InnerPrintLogObject ipo) throws Exception {
        TransOrderInfoTable transOrderInfoTable = dbCommonRPCComponent.apiTransOrderInfoService.getOne(
                new TransOrderInfoTable()
                        .setMerchantId(toit.getMerchantId())
                        .setTerminalMerId(toit.getTerminalMerId())
                        .setMerOrderId(toit.getMerOrderId())
                        .setPlatformIncome(toit.getPlatformIncome())
        );
        if(isNull(transOrderInfoTable))
            throw  new Exception(format("该订单在数据库中不存在：[%s]",toit.toString()));

        Set<Integer> set = new HashSet(Arrays.asList(StatusEnum._7.getStatus(),StatusEnum._8.getStatus()));
        if( !set.contains(transOrderInfoTable.getStatus()) )
            throw  new Exception(format("\n====================================================================\n" +
                            "该订单不在钱包处理范围:\n" +
                            "队列中的订单:[%s]\n" +
                            "数据库的订单:[%s]\n" +
                            "====================================================================\n",
                    toit.toString(),transOrderInfoTable.toString()));
    }

    @Override
    public List<PayOrderInfoTable> getPayOrderInfo(TransOrderInfoTable toit, InnerPrintLogObject ipo) throws Exception {
        String orgMerOrderIds = toit.getOrgMerOrderId().trim();
        List<String> orgMerOrderIdList = Arrays.asList(orgMerOrderIds.split("\\|"));
        Set<String> orgMerOrderIdSet = orgMerOrderIdList.stream().map(t->t.trim()).collect(Collectors.toSet());
        List<PayOrderInfoTable> payOrderInfoTableList = dbCommonRPCComponent.apiPayOrderInfoService.getList(new PayOrderInfoTable()
                .setMerOrderIdCollect(orgMerOrderIdSet)
                .setMerchantId(toit.getMerchantId())
                .setTerminalMerId(toit.getTerminalMerId())
                .setStatus(StatusEnum._9.getStatus()));
        if(isHasNotElement(payOrderInfoTableList))
            throw  new Exception( format("%s-->商户号：%s；终端号：%s；收单订单号：%s,错误信息：根据原始订单号提供信息，查不到收单信息",
                    ipo.getBussType(), ipo.getMerId(), ipo.getTerMerId(),orgMerOrderIds));
        if( payOrderInfoTableList.size() == 1) {
            BigDecimal tAmount = toit.getAmount();
            BigDecimal pInAmount = payOrderInfoTableList.get(0).getInAmount();
            if(tAmount.compareTo(pInAmount) == -1)
                payOrderInfoTableList.get(0).setStatus(StatusEnum._11.getStatus());
            return payOrderInfoTableList;
        }
        payOrderInfoTableList.forEach(p->p.setStatus(StatusEnum._10.getStatus()));
        return payOrderInfoTableList;
    }
}
