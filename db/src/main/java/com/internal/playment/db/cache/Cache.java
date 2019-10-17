package com.internal.playment.db.cache;

import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/16
 * Time: 下午9:34
 * Description:
 */
public  abstract class Cache {

    protected ExecutorService pool = Executors.newCachedThreadPool();

    @Resource
    protected RedisTemplate<String,Object> redisTemplate;



    protected void put(String tableMapKeyName,String objectKey,Object  object) {
        redisTemplate.opsForHash().put(tableMapKeyName,objectKey,object);
    }

    protected void putAll(String tableMapKeyName, Map<Object,Object> map){
        redisTemplate.opsForHash().putAll(tableMapKeyName,map);
    }
}
