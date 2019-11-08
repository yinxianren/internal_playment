package com.internal.playment.inward.scheduler;//package com.rxh.anew.scheduler;
//
//import com.alibaba.fastjson.JSONObject;
//import com.rxh.service.square.ChannelWalletService;
//import com.rxh.service.square.MerchantSquareRateService;
//import com.rxh.service.square.MerchantWalletService;
//import com.rxh.service.square.TerminalMerchantsWalletService;
//import com.rxh.service.trading.PayOrderService;
//import com.rxh.square.pojo.*;
//import com.rxh.utils.HttpClientUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
///**
// * 描述： 定时查询消费订单并跟新结算金额状态
// *
// * @author phoenix
// * @date 2019/09/06
// */
//@Component
//public class WalletAvailableBalanceUpdateTask {
//    private Logger logger = LoggerFactory.getLogger(WalletAvailableBalanceUpdateTask.class);
//    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//
//    @Resource
//    private PayOrderService payOrderService;
//    @Resource
//    private MerchantSquareRateService merchantSquareRateService;
//    @Resource
//    private MerchantWalletService merchantWalletService;
//    @Resource
//    private  ChannelWalletService channelWalletService;
//    @Resource
//    private  TerminalMerchantsWalletService terminalMerchantsWalletService;
//
//    @Scheduled(cron = "0 0 5 * * ?")
//    public void settlement() {
//        //调用判断工作日接口
//        logger.info("开始结算定时任务");
//            List<PayOrder> list = payOrderService.selectByOrderStatusAndSettleStatus("0", "0");//获取结算状态是未结算,交易状态是成功的订单
//            logger.info("待更新的订单: "+list.size()+"条");
//            for (PayOrder payOrder : list) {
//                String merId = payOrder.getMerId();
//                MerchantRate merchantRate = merchantSquareRateService.getMerchantRateByIdAndPayType(merId,payOrder.getPayType());
//                String settlecycle = merchantRate.getSettlecycle();//获取对应商户的结算周期
//                if("d".equals(settlecycle.substring(0,1))||"D".equals(settlecycle.substring(0,1))){
//                    if (Integer.parseInt(dateFormat.format(payOrder.getTradeTime())) + Integer.parseInt(settlecycle.substring(1)) <= Integer.parseInt(dateFormat.format(new Date()))) {
//                        String terminalMerId = payOrder.getTerminalMerId();
//                        String channelId = payOrder.getChannelId();
//                        String payId = payOrder.getPayId();
//                        BigDecimal amount = payOrder.getAmount();
//                        BigDecimal terminalFee = payOrder.getTerminalFee();
//                        BigDecimal fee = payOrder.getFee();
//                        BigDecimal channelFee = payOrder.getChannelFee();
//                        updateTerminalMerchantsWallet(merId,terminalMerId,amount,terminalFee);
//                        updateMerchantWallet(merId,amount,fee);
//                        updateChannelWallet(channelId,amount, channelFee);
//                        int settleStatus = 1;
//                        PayOrder payOrderObject=new PayOrder();
//                        payOrderObject.setPayId(payId);
//                        payOrderObject.setSettleStatus(settleStatus);
//                        payOrderService.updateByPrimaryKeySelective(payOrderObject);
//                    }
//                }else if("t".equals(settlecycle.substring(0,1))||"T".equals(settlecycle.substring(0,1))){
//                    String date = dateFormat.format(new Date());
//                    String content = HttpClientUtils.doPostJson(HttpClientUtils.getHttpsClient(), "http://api.goseek.cn/Tools/holiday?date=" + date, "");
//                    JSONObject parm = JSONObject.parseObject(content);
//                    String a = parm.getString("data");
//                    if ("0".equals(a)){
//                        if (Integer.parseInt(dateFormat.format(payOrder.getTradeTime())) + Integer.parseInt(settlecycle.substring(1)) <= Integer.parseInt(dateFormat.format(new Date()))) {
//                            String terminalMerId = payOrder.getTerminalMerId();
//                            String channelId = payOrder.getChannelId();
//                            String payId = payOrder.getPayId();
//                            BigDecimal amount = payOrder.getAmount();
//                            BigDecimal terminalFee = payOrder.getTerminalFee();
//                            BigDecimal fee = payOrder.getFee();
//                            BigDecimal channelFee = payOrder.getChannelFee();
//                            updateTerminalMerchantsWallet(merId,terminalMerId,amount,terminalFee);
//                            updateMerchantWallet(merId,amount,fee);
//                            updateChannelWallet(channelId,amount, channelFee);
//                            int settleStatus = 1;
//                            PayOrder payOrderObject=new PayOrder();
//                            payOrderObject.setPayId(payId);
//                            payOrderObject.setSettleStatus(settleStatus);
//                            payOrderService.updateByPrimaryKeySelective(payOrderObject);
//                        }
//                    }
//                }
//            }
//    }
//
//    //更新子商户钱包
//    public void updateTerminalMerchantsWallet(String merId,String terminalMerId,BigDecimal amount,BigDecimal terminalFee){
//        TerminalMerchantsWallet param =terminalMerchantsWalletService.search(merId,terminalMerId);
//        amount=amount.subtract(terminalFee);
//        BigDecimal totalUnavailableAmount = (param.getTotalUnavailableAmount() == null ? new BigDecimal("0"):param.getTotalUnavailableAmount())
//                .subtract(amount);
//        BigDecimal totalAvailableAmount = (param.getTotalAvailableAmount() == null ? new BigDecimal("0"):param.getTotalAvailableAmount())
//                .add(amount);
//        TerminalMerchantsWallet terminalMerchantsWallet = new TerminalMerchantsWallet();
//        terminalMerchantsWallet.setId(param.getId());
//        terminalMerchantsWallet.setTotalUnavailableAmount(totalUnavailableAmount);
//        terminalMerchantsWallet.setTotalAvailableAmount(totalAvailableAmount);
//        terminalMerchantsWallet.setUpdateTime(new Date());
//        terminalMerchantsWalletService.updateByPrimaryKeySelective(terminalMerchantsWallet);
//
//    }
//
//
//
//    //更新商户钱包
//    public void updateMerchantWallet(String merId,BigDecimal amount,BigDecimal fee) {
//        MerchantWallet param = merchantWalletService.searchById(merId);
//        amount=amount.subtract(fee);
//        BigDecimal totalUnavailableAmount = (param.getTotalUnavailableAmount() == null ? new BigDecimal("0"):param.getTotalUnavailableAmount())
//                .subtract(amount);
//        BigDecimal totalAvailableAmount = (param.getTotalAvailableAmount() == null ? new BigDecimal("0"):param.getTotalAvailableAmount())
//                .add(amount);
//        MerchantWallet merchantWallet =new MerchantWallet();
//        merchantWallet.setMerId(merId);
//        merchantWallet.setTotalUnavailableAmount(totalUnavailableAmount);
//        merchantWallet.setTotalAvailableAmount(totalAvailableAmount);
//        merchantWallet.setUpdateTime(new Date());
//        merchantWalletService.updateByPrimaryKeySelective(merchantWallet);
//    }
//
// /* //更新代理商钱包
//  public void updateAgentWallet(String agentMerchantId, BigDecimal amount, BigDecimal agentFee) {
//      AgentWallet param = agentWalletService.selectByPrimaryKey(agentMerchantId);
//      amount=amount.subtract(agentFee);
//      BigDecimal totalUnavailableAmount = (param.getTotalUnavailableAmount() == null ? new BigDecimal("0"):param.getTotalUnavailableAmount())
//              .subtract(amount);
//      BigDecimal totalAvailableAmount = (param.getTotalAvailableAmount() == null ? new BigDecimal("0"):param.getTotalAvailableAmount())
//              .add(amount);
//      AgentWallet agentWalle = new AgentWallet();
//      agentWalle.setAgentMerchantId(agentMerchantId);
//      agentWalle.setTotalUnavailableAmount(totalUnavailableAmount);
//      agentWalle.setTotalAvailableAmount(totalAvailableAmount);
//      agentWalletService.updateByPrimaryKeySelective(agentWalle);
//  }*/
//    //更新平台钱包
//    public void updateChannelWallet(String channelId, BigDecimal amount,  BigDecimal channelFee) {
//        ChannelWallet param = channelWalletService.getChannelWallet(channelId);
//        amount=amount.subtract(channelFee);
//        BigDecimal totalUnavailableAmount = (param.getTotalUnavailableAmount() == null ? new BigDecimal("0"):param.getTotalUnavailableAmount())
//                .subtract(amount);
//        BigDecimal totalAvailableAmount = (param.getTotalAvailableAmount() == null ? new BigDecimal("0"):param.getTotalAvailableAmount())
//                .add(amount);
//        ChannelWallet channelWallet = new ChannelWallet();
//        channelWallet.setId(param.getId());
//        channelWallet.setTotalUnavailableAmount(totalUnavailableAmount);
//        channelWallet.setTotalAvailableAmount(totalAvailableAmount);
//        channelWallet.setUpdateTime(new Date());
//        channelWalletService.updateByPrimaryKeySelective(channelWallet);
//
//    }
//
//
//}
