package open.gateway.common.utils;

import java.util.*;

/**
 * Created by miko on 2020/7/6.
 *
 * @author MIKO
 */
public class CollectionUtil {

    /**
     * 拷贝map
     *
     * @param source 源数据map
     * @param target 目标map
     * @param <K>    map的key
     * @param <V>    map的value
     * @param <M>    目标map的类型
     * @return 新的map
     */
    public static <K, V, M extends Map<K, V>> M copy(Map<K, V> source, M target) {
        if (target == null) {
            throw new IllegalArgumentException("target map is null");
        }
        if (source == null) {
            return null;
        }
        source.forEach(target::put);
        return target;
    }

    /**
     * 拷贝map到hashmap
     *
     * @param map 源数据
     * @param <K> map的key
     * @param <V> map的value
     * @return 新的map
     */
    public static <K, V> HashMap<K, V> copyToHashMap(Map<K, V> map) {
        return copy(map, new HashMap<>());
    }

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

    public static <K, V> Map<K, V> newHashMap(Object... kv) {
        return newMap(new HashMap<>(), kv);
    }

}
