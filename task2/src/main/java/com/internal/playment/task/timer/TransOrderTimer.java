package com.internal.playment.task.timer;

import com.internal.playment.common.table.business.TransOrderInfoTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class TransOrderTimer  extends  AbstractTimer{


    @Value("${application.queue.trans-order}")
    private String transOrder;


    @Scheduled(initialDelay=60000 * 2 , fixedDelay = 60000 * 10 ) //启动2分钟后执行，每 8 分钟执行一次
    public void task(){
        final String bussType = "【代付补漏任务】";
        log.info("\n================================================================\n" +
                "====收单定时任务开始执行，本次定时任务查询是否漏单，并放到队列执行====\n"+
                "================================================================\n" );
        try{
            SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Instant currentTime = Instant.now();  //当前的时间
            currentTime = currentTime.plusSeconds( -5 * 60 ); //当前的时间推进 5 分钟
            Instant subtractAfterTime = currentTime.plusSeconds( -3 * 24 * 60 *60);     //3天之前的订单
            String  beginTime = formatter.format( Date.from(subtractAfterTime) );
            String  endTime = formatter.format( Date.from(currentTime) );

//            List<TransOrderInfoTable> transOrderInfoTableList = dbCommonRPCComponent

        }catch (Exception e){

        }
    }

}
