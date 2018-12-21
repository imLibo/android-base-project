package com.cnksi.android.rx;

import android.text.TextUtils;

import java.util.Collection;

/**
 * @author Wastrel
 * @version 1.0
 * @date 2018/6/22 10:09
 * @copyRight 四川金信石信息技术有限公司
 * @since 1.0
 */
public class QueryResult<T> {
    T value;

    public T get() {
        return value;
    }

    public QueryResult(T value) {
        this.value = value;
    }

    public boolean isNull() {
        return value == null;
    }

    public boolean isEmpty() {
        if (value instanceof CharSequence) {
            return TextUtils.isEmpty((CharSequence) value);
        }
        if (value instanceof Collection) {
            return isNull() || ((Collection) value).isEmpty();
        }
        return isNull();
    }

}
