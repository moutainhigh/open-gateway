package open.gateway.common.utils;

import java.util.*;

/**
 * Created by miko on 2020/7/6.
 *
 * @author MIKO
 */
public class CollectionUtil {

    /**
     * 创建新的set
     *
     * @param set    集合
     * @param values 值
     * @return 新的集合
     */
    @SafeVarargs
    public static <V, S extends Set<V>> S newSet(S set, V... values) {
        if (values != null) {
            set.addAll(Arrays.asList(values));
        }
        return set;
    }

    /**
     * 创建新的hashset
     *
     * @param values 值
     * @return 新的集合
     */
    @SafeVarargs
    public static <V> HashSet<V> newHashSet(V... values) {
        return newSet(new HashSet<>(), values);
    }

    /**
     * 创建map
     *
     * @param m   map对象
     * @param kv  key和value数组, 每2个为一组, 第一个为key, 第二个为value
     * @param <K> key的类型
     * @param <V> value的类型
     * @param <M> map类型
     */
    @SuppressWarnings("unchecked")
    public static <K, V, M extends Map<K, V>> M newMap(M m, Object... kv) {
        if (kv != null) {
            for (int i = 0, len = kv.length; i < len; i = i + 2) {
                Object key = kv[i];
                Object value = i + 1 < len ? kv[i + 1] : null;
                m.put((K) key, (V) value);
            }
        }
        return m;
    }

    /**
     * 创建hash map
     *
     * @param kv  key和value数组, 每2个为一组, 第一个为key, 第二个为value
     * @param <K> key的类型
     * @param <V> value的类型
     */
    public static <K, V> Map<K, V> newHashMap(Object... kv) {
        return newMap(new HashMap<>(), kv);
    }

}
