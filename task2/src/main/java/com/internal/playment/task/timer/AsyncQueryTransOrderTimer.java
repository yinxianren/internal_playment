package com.internal.playment.task.timer;

import com.internal.playment.common.enums.StatusEnum;
import com.internal.playment.common.inner.NewPayException;
import com.internal.playment.common.table.business.TransOrderInfoTable;
import com.internal.playment.task.component.ThreadPoolExecutorComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class AsyncQueryTransOrderTimer extends  AbstractTimer {

    @Value("${application.async-query.trans-order}")
    protected String asyncQueryTransOrder;

    /**
     * 代付异步通知漏单查找
     */
    @Scheduled(initialDelay=6000 * 1 , fixedDelay = 60000 * 10) //启动一分钟后执行
    public void task(){
        final String bussType = "【代付异步通知补漏任务】";
        log.info("\n================================================================\n" +
                "=====代付异步通知补漏任务开始执行，查找到结果，并放到队列执行=====\n"+
                "================================================================\n" );

        try{
            SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Instant currentTime = Instant.now();  //当前的时间
            currentTime = currentTime.plusSeconds( -5 * 60 ); //当前的时间推进 5 分钟
            Instant subtractAfterTime = currentTime.plusSeconds( -3 * 24 * 60 *60);     //3天之前的订单
            String  beginTime = formatter.format( Date.from(subtractAfterTime) );
            String  endTime = formatter.format( Date.from(currentTime) );

            List<TransOrderInfoTable> transOrderInfoTableList = dbCommonRPCComponent.apiTransOrderInfoService.getList(new TransOrderInfoTable()
                    .setNotifyStatus(StatusEnum._1.getStatus())
                    .setBeginTime(beginTime)
                    .setEndTime(endTime)
            );
            isHasNotElement(transOrderInfoTableList, RXH_INNER_CODE_M01._1,
                    format("%s-->%s",bussType,RXH_INNER_CODE_M01._2), RXH_INNER_CODE_M01._2);
            log.info("{}--->有{}个漏单对象！",bussType,transOrderInfoTableList.size());
            int index = 0;
            StringBuilder sb = new StringBuilder();
            for(TransOrderInfoTable toi : transOrderInfoTableList ){
                sb.append(format("\n{%s}----第[%s]个---[%s]\n",bussType,++index,toi.toString()));
                ThreadPoolExecutorComponent.executorService.submit(()->activeMqOrderProducerComponent.sendMessage(asyncQueryTransOrder,toi));
            }
            log.info(sb.toString());
            log.info("\n=====================================================================\n" +
                    "=本次代付定时任务已结束，本次定时任务查询[{}]个漏单对象，已放入队列执行=\n"+
                    "=====================================================================\n" ,transOrderInfoTableList.size());
        }catch (Exception e){
            if(e instanceof NewPayException){
                NewPayException nep = (NewPayException) e;
                log.info(nep.getInnerPrintMsg());
            }else   e.printStackTrace();
        }
    }

}
