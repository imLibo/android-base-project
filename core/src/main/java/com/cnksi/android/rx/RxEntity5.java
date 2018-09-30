package com.cnksi.android.rx;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/9/30
 * @since 1.0
 */
public class RxEntity5<T1, T2, T3, T4, T5> implements IRxEntity {

    public T1 entity1;
    public T2 entity2;
    public T3 entity3;
    public T4 entity4;
    public T5 entity5;

    public RxEntity5(T1 entity1, T2 entity2, T3 entity3, T4 entity4, T5 entity5) {
        this.entity1 = entity1;
        this.entity2 = entity2;
        this.entity3 = entity3;
        this.entity4 = entity4;
        this.entity5 = entity5;
    }
}
