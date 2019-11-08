package com.internal.playment.common.tuple;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午9:22
 * Description:
 */
public class Tuple4<A,B,C,D> extends Tuple3<A,B,C> {
    public final D _4;

    public Tuple4(A _, B _2, C _3,D _4) {
        super(_, _2, _3);
        this._4 = _4;
    }
}
