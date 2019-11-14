package com.internal.playment.common.inner;

import java.util.ArrayList;
import java.util.Collection;

/**
 *  描述： 扩展arrayList,实现更多操作方法
 * @author  panda
 * @date 20190719
 * @param <E>
 */
public class PayList<E> extends ArrayList<E> {

    public PayList(){
        super();
    }


    public PayList(Collection<? extends E> c){
        super(c);
    }

    public PayList(int initialCapacity){
        super(initialCapacity);
    }

    /**
     *   扩展父类方法add(E e)，实现链接操作
     * @param e
     * @return
     */
    public PayList<E> ladd(E e){
        super.add(e);
        return this;
    }
}
