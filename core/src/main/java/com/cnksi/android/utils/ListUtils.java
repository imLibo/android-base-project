package com.cnksi.android.utils;
/*
 *  Copyright 2001-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


import android.text.TextUtils;


import com.cnksi.android.crash.SpiderMan;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * Provides utility methods and decorators for {@link List} instances.
 *
 * @author Federico Barbieri
 * @author Peter Donald
 * @author Paul Jack
 * @author Stephen Colebourne
 * @author Neil O'Toole
 * @author Matthew Hawthorne
 * @version $Revision: 1.28 $ $Date: 2004/04/01 20:12:00 $
 * @since Commons Collections 1.0
 */
public class ListUtils {

    /**
     * An empty unmodifiable list.
     * This uses the {@link Collections Collections} implementation
     * and is provided for completeness.
     */
    public static final List EMPTY_LIST = Collections.EMPTY_LIST;

    /**
     * ListUtils should not normally be instantiated.
     */
    public ListUtils() {
    }

    //-----------------------------------------------------------------------

    /**
     * Returns a new list containing all elements that are contained in
     * both given lists.
     *
     * @param list1 the first list
     * @param list2 the second list
     * @return the intersection of those two lists
     * @throws NullPointerException if either list is null
     */
    public static List intersection(final List list1, final List list2) {
        final ArrayList result = new ArrayList();
        final Iterator iterator = list2.iterator();

        while (iterator.hasNext()) {
            final Object o = iterator.next();

            if (list1.contains(o)) {
                result.add(o);
            }
        }

        return result;
    }

    /**
     * Subtracts all elements in the second list from the first list,
     * placing the results in a new list.
     * <p>
     * <p>
     * This differs from {@link List#removeAll(Collection)} in that
     * cardinality is respected; if list1 contains two
     * occurrences of null and list2 only
     * contains one occurrence, then the returned list will still contain
     * one occurrence.
     *
     * @param list1 the list to subtract from
     * @param list2 the list to subtract
     * @return a new list containing the results
     * @throws NullPointerException if either list is null
     */
    public static <T> List<T> subtract(final List<T> list1, final List<T> list2) {
        final ArrayList result = new ArrayList(list1);
        final Iterator iterator = list2.iterator();

        while (iterator.hasNext()) {
            result.remove(iterator.next());
        }

        return result;
    }

    /**
     * Returns the sum of the given lists.  This is their intersection
     * subtracted from their union.
     *
     * @param list1 the first list
     * @param list2 the second list
     * @return a new list containing the sum of those lists
     * @throws NullPointerException if either list is null
     */
    public static List sum(final List list1, final List list2) {
        return subtract(union(list1, list2), intersection(list1, list2));
    }

    /**
     * Returns a new list containing the second list appended to the
     * first list.  The {@link List#addAll(Collection)} operation is
     * used to append the two given lists into a new list.
     *
     * @param list1 the first list
     * @param list2 the second list
     * @return a new list containing the union of those lists
     * @throws NullPointerException if either list is null
     */
    public static List union(final List list1, final List list2) {
        final ArrayList result = new ArrayList(list1);
        result.addAll(list2);
        return result;
    }

    /**
     * Tests two lists for value-equality as per the equality contract in
     * {@link List#equals(Object)}.
     * <p>
     * <p>
     * <p>
     * This method is useful for implementing List when you cannot
     * extend AbstractList. The method takes Collection instances to enable other
     * collection types to use the List implementation algorithm.
     * <p>
     * <p>
     * <p>
     * The relevant text (slightly paraphrased as this is a static method) is:
     * <p>
     * <p>
     * <p>
     * Compares the two list objects for equality.  Returns
     * true if and only if both
     * lists have the same size, and all corresponding pairs of elements in
     * the two lists are equal.  (Two elements e1 and
     * e2 are equal if (e1==null ? e2==null :
     * e1.equals(e2)).)  In other words, two lists are defined to be
     * equal if they contain the same elements in the same order.  This
     * definition ensures that the equals method works properly across
     * different implementations of the List interface.
     * <p>
     * <p>
     * <p>
     * Note: The behaviour of this method is undefined if the lists are
     * modified during the equals comparison.
     *
     * @param list1 the first list, may be null
     * @param list2 the second list, may be null
     * @return whether the lists are equal by value comparison
     * @see List
     */
    public static boolean isEqualList(final Collection list1, final Collection list2) {
        if (list1 == list2) {
            return true;
        }
        if (list1 == null || list2 == null || list1.size() != list2.size()) {
            return false;
        }

        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();
        Object obj1 = null;
        Object obj2 = null;

        while (it1.hasNext() && it2.hasNext()) {
            obj1 = it1.next();
            obj2 = it2.next();

            if (!(obj1 == null ? obj2 == null : obj1.equals(obj2))) {
                return false;
            }
        }

        return !(it1.hasNext() || it2.hasNext());
    }

    /**
     * Generates a hash code using the algorithm specified in
     * {@link List#hashCode()}.
     * <p>
     * <p>
     * This method is useful for implementing List when you cannot
     * extend AbstractList. The method takes Collection instances to enable other
     * collection types to use the List implementation algorithm.
     *
     * @param list the list to generate the hashCode for, may be null
     * @return the hash code
     * @see List#hashCode()
     */
    public static int hashCodeForList(final Collection list) {
        if (list == null) {
            return 0;
        }
        int hashCode = 1;
        Iterator it = list.iterator();
        Object obj = null;

        while (it.hasNext()) {
            obj = it.next();
            hashCode = 31 * hashCode + (obj == null ? 0 : obj.hashCode());
        }
        return hashCode;
    }

    /**
     * join一个List
     *
     * @param list
     * @param spliter
     * @return
     */
    public static String join(final Collection list, String spliter) {
        return join(list, spliter, null);
    }

    /**
     * 用字符串join一个List,并可以将最终的字符串替换
     *
     * @param list
     * @param spliter
     * @param warpper 如 "'?'",?表示原list中的每项字符串
     * @return
     */
    public static String join(final Collection list, String spliter, String warpper) {
        if (list == null || list.size() == 0) {
            return "";
        }
        String ret = "";
        spliter = spliter == null ? "" : spliter;
        Iterator it = list.iterator();

        while (it.hasNext()) {
            Object obj = it.next();
            if (obj == null || obj.toString().trim().length() == 0) continue;
            String target = obj.toString();
            if (warpper != null) {
                target = warpper.replace("?", target);
            }
            ret = ret + target + spliter;
        }

        if (spliter.length() > 0 && ret.length() > 0) {
            int lastIndexOfSpliter = ret.lastIndexOf(spliter);
            if (lastIndexOfSpliter == ret.length() - 1) {
                ret = ret.substring(0, lastIndexOfSpliter);
            }
        }

        return ret;
    }

    /**
     * 过滤或者遍历List
     *
     * @param list
     * @param func
     * @param <T>
     * @return
     */
    public static <T extends Object> List<T> listFilter(final List<T> list, ListFilter<T> func) {
        final ArrayList<T> list1 = new ArrayList<>();
        final Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            T result = func.filter(iterator.next());
            if (result != null) {
                list1.add(result);
            }
        }
        return list1;
    }

    public interface ListFilter<T> {
        /**
         * @param val List中的对象
         * @return 返回新的对象(可以修改), 如果返回null表示舍弃
         */
        T filter(T val);
    }

    /**
     * 将一维List分割成二维List
     *
     * @param list
     * @param count
     * @param <T>
     * @return
     */
    public static <T extends Object> ArrayList<ArrayList<T>> split(final ArrayList<T> list, int count) {
        ArrayList<ArrayList<T>> ret = new ArrayList<>();
        ArrayList<T> subList = new ArrayList<>();
        Iterator<T> listIterator = list.iterator();
        while (listIterator.hasNext()) {
            subList.add(listIterator.next());
            if (subList.size() % count == 0) {
                ret.add(subList);
                subList = new ArrayList<>();
            }
        }
        return ret;
    }

    /**
     * 通过反射让list中的每项执行方法
     *
     * @param list
     * @param methodName
     * @param <T>
     * @return
     */
    public static <T extends Object, K> ArrayList<K> invokeMethodWithArgs(final Collection<T> list, String methodName, Class[] argsClass, Object[] argsValue) {
        ArrayList<K> ret = new ArrayList<>();
        try {
            Iterator<T> listIterator = list.iterator();
            while (listIterator.hasNext()) {
                T instance = listIterator.next();

                Method method = instance.getClass().getMethod(methodName, argsClass);
                ret.add((K) method.invoke(instance, argsValue));
            }
        } catch (Exception e) {
            SpiderMan.writeError(e);
        }
        return ret;
    }

    /**
     * 通过执行list中返回String的元素方法,构造出一个新的字符串
     *
     * @param list
     * @param methodName
     * @param argsClass
     * @param argsValue
     * @param spliter
     * @param <T>
     * @return
     */
    public static <T extends Object> String join(final Collection<T> list, String methodName, Class[] argsClass, Object[] argsValue, String spliter) {
        ArrayList<String> newList = invokeMethodWithArgs(list, methodName, argsClass, argsValue);
        return join(newList, spliter);
    }


    public interface ListSelection<K, V> {
        /**
         * @return 返回需要分组的Key列表
         */
        List<K> getKeyList();

        /**
         * 返回数据对应的Key值
         *
         * @param val 原来list中的值,返回null表示舍弃
         * @return 对用的Key
         */
        K selectionFilter(V val);
    }

    /**
     * 将list分组成一个map
     *
     * @param list          将要分组的list对象
     * @param listSelection
     * @param <K>           Key的类型
     * @param <V>           值的类型
     * @return
     */
    public static <K extends Object, V extends Object> LinkedHashMap<K, ArrayList<V>> listSelection(final Collection<V> list, ListSelection<K, V> listSelection) {
        LinkedHashMap<K, ArrayList<V>> ret = new LinkedHashMap<>();
        for (K key : listSelection.getKeyList()) {
            ret.put(key, new ArrayList<V>());
        }
        if (list == null || list.size() == 0) {
            return ret;
        }
        for (V value : list) {
            K finalKey = listSelection.selectionFilter(value);
            if (finalKey != null && ret.containsKey(finalKey)) {
                ret.get(finalKey).add(value);
            }
        }
        return ret;
    }



    public interface Func<T1, T2> {
        T2 action(T1 t);
    }

    public interface Action<T1> {
        void action(T1 t);
    }

    public interface Func2<T1, T2, T3> {
        T3 action(T1 t1, T2 t2);
    }

    /**
     * 争对某一字段或关键字建立索引Map。
     *
     * @param src         目标源
     * @param keySelector 关键字选择器
     * @param <K>
     * @param <V>
     * @return 返回关联Map。
     */
    public static <K, V> Map<K, V> indexMap(List<V> src, Func<V, K> keySelector) {
        Map<K, V> rs = new HashMap<>();
        if (src == null || src.size() == 0) return rs;
        for (V v : src) {
            rs.put(keySelector.action(v), v);
        }
        return rs;
    }

    /**
     * List转换为Map
     *
     * @param src         源List
     * @param keySelector key选择器
     * @param <K>         key类型
     * @param <V>
     * @return 返回根据Key 选择器分类后的Map 该Map为LinkedHashMap
     */
    public static <K, V> Map<K, List<V>> groupBy(List<V> src, Func<V, K> keySelector) {
        return groupBy(src, new LinkedHashMap<>(), keySelector);
    }


    /**
     * List转换为Map
     *
     * @param dstMap      目标Map
     * @param src         源List
     * @param keySelector key选择器
     * @param <K>         key类型
     * @param <V>
     * @return 返回根据Key 选择器分类后的Map
     */
    public static <K, V> Map<K, List<V>> groupBy(List<V> src, Map<K, List<V>> dstMap, Func<V, K> keySelector) {
        if (src == null || src.size() == 0) return dstMap;
        List<V> temp;
        for (V v : src) {
            K key = keySelector.action(v);
            if ((temp = dstMap.get(key)) == null) {
                temp = new ArrayList<>();
                dstMap.put(key, temp);
            }
            temp.add(v);
        }
        return dstMap;
    }

    /**
     * 根据条件过滤List.
     *
     * @param src      源List
     * @param selector 选择器，返回true时会被选择
     * @param <T>
     * @return 返回过滤后的新List.
     */
    public static <T> List<T> filter(List<T> src, Func<T, Boolean> selector) {
        List<T> rs = new ArrayList<>();
        if (src != null) {
            for (T t : src) {
                if (selector.action(t)) {
                    rs.add(t);
                }
            }
        }
        return rs;
    }

    /**
     * List类型变换
     *
     * @param src     源List
     * @param convert 类型转换器
     * @param <T1>    源类型
     * @param <T2>    目标类型
     * @return 返回变换后的List
     */
    public static <T1, T2> List<T2> map(List<T1> src, Func<T1, T2> convert) {
        return map(src, new ArrayList<T2>(), convert);
    }

    /**
     * List类型变换
     *
     * @param src     源List
     * @param dst     目标List，如果为空或与源List是同一个对象则会创建一个ArrayList.
     * @param convert 转换器
     * @param <T1>    源类型
     * @param <T2>    目标类型
     * @return 返回添加后的List。
     */
    public static <T1, T2> List<T2> map(List<T1> src, List<T2> dst, Func<T1, T2> convert) {
        if (src == null) return dst;
        if (src == dst) {
            //目标不能与源是同一个List.
            dst = new ArrayList<>();
        }
        if (dst == null) dst = new ArrayList<>();
        for (T1 t1 : src) {
            dst.add(convert.action(t1));
        }
        return dst;
    }

    /**
     * List 去重。相同的对象会被移除，其与HashSet规则一致。
     *
     * @param src 源List
     * @param <T>
     * @return 返回通过HashSet去重之后的List
     */
    public static <T> List<T> distinct(List<T> src) {
        if (src == null || src.size() == 0) return new ArrayList<>();
        return new ArrayList<>(new LinkedHashSet<T>(src));
    }

    /**
     * List 去重。相同的对象会被移除，其与HashSet规则一致。
     *
     * @param src         源List
     * @param keySelector 根据keySelector返回的Key来进行过滤。
     * @param <T>
     * @return 返回通过HashSet去重之后的List
     */
    public static <T, K> List<T> distinctBy(List<T> src, Func<T, K> keySelector) {
        if (src == null || src.size() == 0) return new ArrayList<>();
        HashSet<K> set = new HashSet<>();
        List<T> rs = new ArrayList<>();
        for (T t : src) {
            if (set.add(keySelector.action(t))) {
                rs.add(t);
            }
        }
        return rs;
    }

    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }

    /**
     * 按照转化器转换成String
     *
     * @param src     源List
     * @param convert 转换器 第二个bool值表示是否是最后一个元素
     * @param <T>
     * @return
     */
    public static <T> String toString(List<T> src, Func2<T, Boolean, String> convert) {
        if (src == null || src.size() == 0) return "";
        StringBuilder builder = new StringBuilder();
        for (int i = 0, size = src.size(); i < size; i++) {
            builder.append(convert.action(src.get(i), i == size - 1));
        }
        return builder.toString();
    }

    /**
     * 按照转化器转换成String
     *
     * @param src     源List
     * @param convert 转换器 第二个bool值表示是否是最后一个元素
     * @param <T>
     * @return
     */
    public static <T> String toString(Collection<T> src, String separator, Func<T, String> convert) {
        if (src == null || src.size() == 0) return "";
        StringBuilder builder = new StringBuilder();
        Iterator<T> iterator = src.iterator();
        while (iterator.hasNext()) {
            builder.append(convert.action(iterator.next())).append(separator);
        }

        return builder.delete(builder.length() - separator.length(), builder.length()).toString();
    }

    /**
     * 按照转化器转换成String
     *
     * @param src     源List
     * @param convert 转换器 第二个bool值表示是否是最后一个元素
     * @param <T>
     * @return
     */
    public static <T> String toSqlInParamString(Collection<T> src, Func<T, String> convert) {
        if (src == null || src.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Iterator<T> iterator = src.iterator();
        while (iterator.hasNext()) {
            String s = convert.action(iterator.next());
            if (s != null) {
                builder.append("'").append(s).append("',");
            } else {
                builder.append("null,");
            }
        }

        return builder.delete(builder.length() - 1, builder.length()).toString();
    }

    /**
     * 根据条件查找第一符合要求的元素
     *
     * @param src      目标源
     * @param selector 条件选择器
     * @param <T>
     * @return 找到第一个就返回，找不到返回null;
     */
    public static <T> T find(List<T> src, Func<T, Boolean> selector) {
        if (src == null || src.size() == 0) return null;
        for (T t : src) {
            if (selector.action(t)) {
                return t;
            }
        }
        return null;
    }

    /**
     * 将JavaBean转换成Map<String,String>的形式
     *
     * @param src
     * @param convert
     * @param <T>
     * @return
     */
    public static <T> LinkedHashMap<String, String> toStringMap(List<T> src, Func2<T, Map<String, String>, String> convert) {
        LinkedHashMap<String, String> rs = new LinkedHashMap<>();
        if (src != null) {
            for (T t : src) {
                convert.action(t, rs);
            }
        }
        return rs;
    }

    /**
     * 将JavaBean转换成Map<String,String>的形式
     *
     * @param src
     * @param keySelector
     * @param valueSelector
     * @param <T>
     * @return
     */
    public static <T> LinkedHashMap<String, String> toStringMap(List<T> src, Func<T, String> keySelector, Func<T, String> valueSelector) {
        LinkedHashMap<String, String> rs = new LinkedHashMap<>();
        if (src != null) {
            for (T t : src) {
                rs.put(keySelector.action(t), valueSelector.action(t));
            }
        }
        return rs;
    }

    /**
     * 生成可编辑的ArrayList
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> ArrayList<T> asArray(T... obj) {
        ArrayList<T> rs = new ArrayList<>();
        for (T t : obj) {
            rs.add(t);
        }
        return rs;
    }

    /**
     * 将List变成树形结构的List
     *
     * @param src
     * @param idSelector
     * @param pidSelector
     * @param <T>
     * @return
     */
    public static <T> List<Node<T>> toTreeList(List<T> src, Func<T, String> idSelector, Func<T, String> pidSelector) {
        Map<String, Node<T>> map = new LinkedHashMap<>(src.size());
        for (T model : src) {
            map.put(idSelector.action(model), new Node<>(model));
        }
        for (Map.Entry<String, Node<T>> entry : map.entrySet()) {
            Node<T> node = entry.getValue();
            String pid = pidSelector.action(node.value);
            if (!TextUtils.isEmpty(pid)) {
                Node<T> nodeParent = map.get(pid);
                node.setParent(nodeParent);
            }
        }
        List<Node<T>> rs = new ArrayList<>();
        for (Map.Entry<String, Node<T>> entry : map.entrySet()) {
            if (entry.getValue().isTop()) {
                rs.add(entry.getValue());
            }
        }
        return rs;
    }

    public static class Node<T> {
        public T value;
        public Node<T> parent;
        public List<Node<T>> child = new ArrayList<>();

        public Node(T value) {
            this.value = value;
        }

        public Node<T> setParent(Node<T> parent) {
            //移除之前的关联
            if (this.parent != null) {
                this.parent.removeChild(this);
            }
            this.parent = parent;
            if (this.parent != null) {
                this.parent.child.add(this);
            }
            return this;
        }

        public Node<T> addChild(T value) {
            addChild(new Node<>(value));
            return this;
        }

        public Node<T> addChild(Node<T> node) {
            node.setParent(this);
            return this;
        }

        public boolean isEmpty() {
            return child.isEmpty();
        }

        public Node<T> removeChild(Node<T> node) {
            child.remove(node);
            return this;
        }

        public boolean isTop() {
            return parent == null;
        }

        public void action(ListUtils.Action<Node<T>> action) {
            action.action(this);
            for (Node<T> node : child) {
                node.action(action);
            }
        }

    }
}
