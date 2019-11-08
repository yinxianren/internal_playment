package com.internal.playment.common.tuple;

/**
 * Created with IntelliJ IDEA.
 * User: panda
 * Date: 2019/10/22
 * Time: 下午9:23
 * Description:
 */
public class Tuple5<A,B,C,D,E> extends Tuple4<A,B,C,D> {

    public final E _5;

    public Tuple5(A _, B _2, C _3, D _4, E _5) {
        super(_, _2, _3, _4);
        this. _5 = _5;
    }
}
