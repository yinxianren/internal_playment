package com.internal.playment.common.tuple;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午9:13
 * Description:
 */
public class Tuple2<A,B> implements Serializable {

    public final A _1;
    public final B _2;

    public Tuple2(A _1,B _2){
      this._1 = _1;
      this._2 = _2;
    }

}
