package com.internal.playment.task.timer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TransOrderTimer  extends  AbstractTimer{


    @Value("${application.queue.trans-order}")
    private String transOrder;


    @Scheduled(initialDelay=60000 * 2 , fixedDelay = 60000 * 10 ) //启动2分钟后执行，每 8 分钟执行一次
    public void task(){

    }

}
