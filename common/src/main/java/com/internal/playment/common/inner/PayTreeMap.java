package com.internal.playment.common.inner;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *  描述 ：在treemap 基础上增加链接操作
 * @author  panda
 * @date 20190719
 * @param <K>
 * @param <V>
 */
public class PayTreeMap<K,V> extends TreeMap<K,V> {

    public PayTreeMap() {
        super();
    }

    public PayTreeMap(Comparator<? super K> comparator) {
        super(comparator);
    }

    public PayTreeMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    public PayTreeMap(SortedMap<K, ? extends V> m) {
        super(m);
    }

    public PayTreeMap<K,V> lput(K key, V value){
        super.put(key,value);
        return  this;
    }
}