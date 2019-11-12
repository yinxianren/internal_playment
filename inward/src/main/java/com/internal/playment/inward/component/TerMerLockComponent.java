//package com.internal.playment.inward.component;
//
//
//import com.internal.playment.common.inner.NewPayAssert;
//import com.internal.playment.common.inner.PayUtil;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class TerMerLockComponent implements NewPayAssert, PayUtil {
//
//    @Data
//    @AllArgsConstructor
//    private  class LockObject{
//        private long createTime;
//        private long updateTime;
//    }
//
//    private static ConcurrentHashMap<String,LockObject> concurrentHashMap =new ConcurrentHashMap<>();
//
//    synchronized public  Object getLock(String terMerId) throws Exception {
//        if(isBlank(terMerId)) throw new Exception("参数terMerId为NULL！");
//        LockObject object = concurrentHashMap.get(terMerId);
//        if(isNull(object)){
//            long time = System.currentTimeMillis();
//            object = new LockObject(time,time);
//            concurrentHashMap.put(terMerId,object);
//        }
//        object.setUpdateTime(System.currentTimeMillis());
//        return object;
//    }
//
//    @Scheduled(initialDelay=60000 * 60 * 24 , fixedDelay = 60000 * 60 * 24)
//    synchronized public  void  clean(){
//        Map<String,LockObject> map = new LinkedHashMap<>(concurrentHashMap);
//        final long currentTime = System.currentTimeMillis();
//        map.forEach((k,v)->{
//            long updateTime =  v.getUpdateTime();
//            if(currentTime - updateTime >= 86400000 * 3 )
//                concurrentHashMap.remove(k);
//        });
//    }
//
//}
