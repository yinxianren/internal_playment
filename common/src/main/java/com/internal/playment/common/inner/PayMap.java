package com.internal.playment.common.inner;

import java.util.HashMap;
import java.util.Map;

/**
 *  描述  hashMap 扩展类
 * @author  panda
 * @date 20190719
 * @param <K>
 * @param <V>
 */
public class PayMap<K,V> extends HashMap<K,V> implements  IPayMap<K,V> {

    public PayMap(){
        super();
    }

    public PayMap(int initialCapacity){
        super(initialCapacity);
    }

    public PayMap(int initialCapacity, float loadFactor){
        super(initialCapacity,loadFactor);
    }

    public PayMap(Map<? extends K, ? extends V> m){
        super(m);
    }

    /**
     *  描述：扩展父类方法，实现链接操作
     *      父类的方法
     *        public V put(K key, V value) {
     *         return putVal(hash(key), key, value, false, true);
     *        }
     * @param key
     * @param value
     * @return
     */
    public PayMap<K,V> lput(K key, V value){
        super.put(key,value);
        return this;
    }

}
