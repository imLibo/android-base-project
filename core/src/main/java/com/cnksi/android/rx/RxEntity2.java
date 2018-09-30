package com.cnksi.android.rx;

/**
 * @author lyongfly
 * @version 1.0
 * @copyRights 四川金信石信息技术有限公司
 * @date 2018/9/30
 * @since 1.0
 */
public class RxEntity2<T1, T2> implements IRxEntity {

    public T1 entity1;
    public T2 entity2;

    public RxEntity2(T1 entity1, T2 entity2) {
        this.entity1 = entity1;
        this.entity2 = entity2;
    }
}
