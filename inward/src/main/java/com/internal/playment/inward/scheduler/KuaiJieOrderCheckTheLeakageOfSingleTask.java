package com.internal.playment.inward.scheduler;//package com.rxh.anew.scheduler;
//
//
//import com.alibaba.fastjson.JSON;
//import com.rxh.activeMQ.TransOrderMQ;
//import com.rxh.exception.PayException;
//import com.rxh.payInterface.PayAssert;
//import com.rxh.payInterface.PayUtil;
//import com.rxh.service.AbstractPayService;
//import com.rxh.service.square.ChannelInfoService;
//import com.rxh.service.square.MerchantInfoService;
//import com.rxh.service.square.SystemOrderTrackService;
//import com.rxh.service.trading.PayOrderService;
//import com.rxh.service.trading.TransOrderService;
//import com.rxh.square.pojo.ChannelInfo;
//import com.rxh.square.pojo.MerchantInfo;
//import com.rxh.square.pojo.PayOrder;
//import com.rxh.square.pojo.TransOrder;
//import com.rxh.utils.SystemConstant;
//import com.rxh.vo.OrderObjectToMQ;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.time.Instant;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
//@Component
//public class KuaiJieOrderCheckTheLeakageOfSingleTask implements PayAssert, PayUtil {
//
//
//    private Logger logger = LoggerFactory.getLogger(getClass());
//    @Autowired
//    private TransOrderMQ transOrderMQ;
//    @Autowired
//    private TransOrderService transOrderService;
//    @Autowired
//    private PayOrderService  payOrderService;
//    @Autowired
//    private ChannelInfoService channelInfoService;
//    @Autowired
//    private MerchantInfoService merchantInfoService;
//    @Autowired
//    private SystemOrderTrackService systemOrderTrackService;
//
//
//
//    @Scheduled(cron = "0 0/15 * * * ?")
//    public void execute(){
//        logger.info("【执行快捷定时器查询是否漏单！】");
//        Instant currentTime = Instant.now();  //当前的时间
//        Instant subtractAfterTime = currentTime.plusSeconds(-16*60);     //16分钟之前的订单
//        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String  beginTime=formatter.format( Date.from(subtractAfterTime));
//        String endTime=formatter.format(Date.from(currentTime));
//        //查询代付订单
//        TransOrder transOrder=new TransOrder();
////        transOrder.setOrderStatus(SystemConstant.MQ_EXCEPITON_PROCESSED);
//        transOrder.setOrderStatusList(Arrays.asList(SystemConstant.MQ_EXCEPITON_PROCESSED,SystemConstant.MQ2_PROCESSED_33));
//        transOrder.setBeginTime(beginTime);
//        transOrder.setEndTime(endTime);
//        List<TransOrder> transOrderList=transOrderService.getTransOrderByWhereCondition(transOrder);
//        logger.info("【执行快捷定时器查询是否漏单！】,代付漏单查询到{}条数据；",transOrderList.size());
//        //处理代付订单
//        if(null != transOrderList ){
//            for(TransOrder  t : transOrderList){
//                AbstractPayService.pool.execute(()->{
//                    try{
//                        logger.info("【执行快捷定时器查询是否漏单！】,代付漏单查对象：{}", JSON.toJSONString(t));
//                        this.handleTransOrder(t);
//                    }catch (Exception e){
////                        logger.warn(e.getMessage());
//                        e.printStackTrace();
//                    }
//                });
//            }
//        }
//        //处理收款订单
//        PayOrder payOrder=new PayOrder();
////        payOrder.setOrderStatus(SystemConstant.MQ_EXCEPITON_PROCESSED);
//        payOrder.setOrderStatusList(Arrays.asList(SystemConstant.MQ_EXCEPITON_PROCESSED,SystemConstant.MQ_PROCESSED_23));
//        payOrder.setBeginTime(beginTime);
//        payOrder.setEndTime(endTime);
//        List<PayOrder> payOrderList=payOrderService.getPayOrderByWhereCondition(payOrder);
//        logger.info("【执行快捷定时器查询是否漏单！】,收单漏单查询到{}条数据；",payOrderList.size());
//        if(null != payOrderList){
//            for (PayOrder p: payOrderList) {
//                AbstractPayService.pool.execute(()->{
//                    try{
//                        logger.info("【执行快捷定时器查询是否漏单！】,收单漏单查对象：{}", JSON.toJSONString(p));
//                        this.handlePayOrder(p);
//                    }catch (Exception e){
////                        logger.warn(e.getMessage());
//                        e.printStackTrace();
//                    }
//                });
//            }
//        }
//    }
//
//    /**
//     *  处理收款订单
//     * @param p
//     * @throws PayException
//     */
//    private void handlePayOrder(PayOrder p) throws PayException {
//        String payId=p.getPayId();
//        String merId=p.getMerId();
//        //获取通道信息
//        ChannelInfo channelInfo=channelInfoService.selectByChannelId(p.getChannelId());
//        isNull(channelInfo,format("【快捷支付定时任务---收单订单处理】订单号:%s,商户号：%s,获取通道信息为null!",payId,merId));
//        //获取用户信息
//        MerchantInfo merchantInfo=merchantInfoService.getMerchantById(merId);
//        isNull(merchantInfo,format("【快捷支付定时任务---收单订单处理】订单号:%s,商户号：%s,获取用户信息为null!",payId,merId));
////        //获取系统跟踪信息
////        List<SystemOrderTrack> systemOrderTrackList=systemOrderTrackService.selectAllSystemOrder(new PayMap<String,Object>()
////                .lput("merId",merId)
////                .lput("merOrderId",p.getMerOrderId())
////                .lput("orderId",payId)
////                .lput("startPage",0)
////                .lput("pageSize",5)
////        );
////        isNull(systemOrderTrackList,format("【快捷支付定时任务---收单订单处理】订单号:%s,商户号：%s,获取系统订单跟踪对象为null!",payId,merId));
////        if(systemOrderTrackList.size() != 1)
////            throw  new PayException(format("【快捷支付定时任务---收单订单处理】订单号:%s,商户号：%s,获取系统订单跟踪有多记录，无法取得唯一性",payId,merId));
//
////        SystemOrderTrack systemOrderTrack=systemOrderTrackList.get(0);
////        Map<String,Object> tradeInfoMap= JsonUtils.jsonToMap(systemOrderTrack.getTradeInfo());
//        BigDecimal payFee=  p.getTerminalFee().multiply(new BigDecimal(100)).divide(p.getAmount());
//        isNull(payFee,format("【快捷支付定时任务---收单订单处理】订单号:%s,商户号：%s,获取payFee对象为null!",payId,merId));
//        OrderObjectToMQ orderObjectToMQ=getOrderObjectToMQ(p,payFee,channelInfo,merchantInfo);
//        //发送到mq
//        transOrderMQ.sendObjectMessageToTransOderMQ(orderObjectToMQ);
//        //更新订单状态为队列处理中
//        p.setOrderStatus(SystemConstant.MQ_PROCESSED_23);
//        payOrderService.updateByPrimaryKey(p);
//    }
//    /**
//     *  封装发送到 mq对象参数
//     * @return
//     */
//    private OrderObjectToMQ getOrderObjectToMQ(PayOrder payOrder,BigDecimal payFee,ChannelInfo channelInfo,MerchantInfo merchantInfo){
//
//
//
//        BigDecimal amount= payOrder.getAmount();
//        String merId=payOrder.getMerId();
//        String merOrderId=payOrder.getMerOrderId();
//        String payType=payOrder.getPayType();
//        String channelId=channelInfo.getChannelId();
//        Integer channelType=channelInfo.getType();//
//        String payId=payOrder.getPayId();
//        String terminalMerId=payOrder.getTerminalMerId();
//        String parentId=merchantInfo.getParentId();
//        String channelTransCode=channelInfo.getChannelTransCode();
////        Integer  orderStatus=payOrder.getOrderStatus();
//        return new OrderObjectToMQ()
//                .lsetAmount(amount)
//                .lsetPayFee(payFee)
//                .lsetMerId(merId)
//                .lsetMerOrderId(merOrderId)
//                .lsetPayType(payType)
//                .lsetChannelId(channelId)
//                .lsetChannelType(channelType)
//                .lsetPayId(payId)
//                .lsetTerminalMerId(terminalMerId)
//                .lsetParentId(parentId)
//                .lsetChannelTransCode(channelTransCode)
//                .lsetOrderStatus(20)
//                ;
//    }
//
//    /**
//     *   处理代付订单
//     * @param t
//     * @throws PayException
//     */
//    private void handleTransOrder(TransOrder  t) throws PayException {
//        String transId=t.getTransId();
//        String merId=t.getMerId();
//        //获取通道信息
//        ChannelInfo channelInfo=channelInfoService.selectByChannelId(t.getChannelId());
//        isNull(channelInfo,format("【快捷支付定时任务---代付订单处理】订单号:%s,商户号：%s,获取通道信息为null!",transId,merId));
//        //获取用户信息
//        MerchantInfo merchantInfo=merchantInfoService.getMerchantById(t.getMerId());
//        isNull(merchantInfo,format("【快捷支付定时任务---代付订单处理】订单号:%s,商户号：%s,获取用户信息为null!",transId,merId));
//        //获取收款订单号
//        PayOrder  payOrder=new PayOrder();
//        String[] originalMerOrderIds = t.getOriginalMerOrderId().split("\\|");
//        payOrder.setMerOrderIdList(Arrays.asList(originalMerOrderIds));
//        payOrder.setTerminalMerId(t.getTerminalMerId());
//        payOrder.setMerId(t.getMerId());
//        List<PayOrder> payOrderList=payOrderService.getPayOrderByWhereCondition(payOrder);
//        isNotElement(payOrderList,format("【快捷支付定时任务---代付订单处理】订单号:%s,商户号：%s,获取收单订单信息为null!",transId,merId));
//        state(!(originalMerOrderIds.length != payOrderList.size()),
//                format("【快捷支付定时任务---代付订单处理】订单号:%s,商户号：%s,多订单号有不匹配订单!",transId,merId)  );
//        OrderObjectToMQ orderObjectToMQ=getOrderObjectToMQ( t,  channelInfo, merchantInfo, originalMerOrderIds);
//        isNull(payOrder,format("【快捷支付定时任务---代付订单处理】订单号:%s,商户号：%s,封装OrderObjectToMQ对象为null!",transId,merId));
//        //发送到mq
//        transOrderMQ.sendObjectMessageToTransOderMQ(orderObjectToMQ);
//        //更新订单状态为队列处理中
//        t.setOrderStatus(SystemConstant.MQ2_PROCESSED_33);
//        transOrderService.updateByPrimaryKey(t);
//    }
//
//
//
//
//    /**
//     *
//     * @param transOrder
//     * @param channelInfo
//     * @param merchantInfo
//     * @param originalMerOrderIds
//     * @return
//     */
//    private OrderObjectToMQ getOrderObjectToMQ(TransOrder transOrder, ChannelInfo channelInfo,MerchantInfo merchantInfo,String[] originalMerOrderIds){
//        String merId=transOrder.getMerId();
//        String merOrderId=transOrder.getMerOrderId();
//        String channelId=channelInfo.getChannelId();
//        String payType=transOrder.getPayType();
//        BigDecimal realAmount=transOrder.getRealAmount();
//        BigDecimal amount=transOrder.getAmount();
//        BigDecimal terminalFee=transOrder.getTerminalFee();
//        Integer channelType=channelInfo.getType();//
//        String transId=transOrder.getTransId();
//        String channelTransCode=channelInfo.getChannelTransCode();
//        String terminalMerId=transOrder.getTerminalMerId();
//        String parentId=merchantInfo.getParentId();
//        String[] payIds=originalMerOrderIds;
//
//        return new OrderObjectToMQ()
//                .lsetMerId(merId)
//                .lsetMerOrderId(merOrderId)
//                .lsetChannelId(channelId)
//                .lsetChannelType(channelType)
//                .lsetPayType(payType)
//                .lsetRealAmount(realAmount)
//                .lsetAmount(amount)
//                .lsetTerminalFee(terminalFee)
//                .lsetTransId(transId)
//                .lsetChannelTransCode(channelTransCode)
//                .lsetTerminalMerId(terminalMerId)
//                .lsetParentId(parentId)
//                .lsetOrderStatus(30)
//                .lsetPayIds(payIds)
//                .lsetPayId(null)
//                ;
//    }
//
//}
