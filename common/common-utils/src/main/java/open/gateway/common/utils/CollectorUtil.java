package open.gateway.common.utils;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 * Created by miko on 2019/11/14.
 *
 * @author MIKO
 */
public class CollectorUtil {

    /**
     * 集合合并器
     *
     * @param <C> 集合类型
     * @param <R> 集合中元素的类型
     */
    public static <C extends Collection<R>, R> BinaryOperator<C> mergeCollection() {
        return (C c1, C c2) -> {
            c1.addAll(c2);
            return c1;
        };
    }

    /**
     * 集合转换器
     *
     * @param supplier 集合提供者
     * @param <E>      集合中元素的类型
     * @param <S>      集合类型
     */
    public static <E, S extends Collection<E>> Collector<E, S, S> toCollection(Supplier<S> supplier) {
        return Collector.of(
                supplier,
                Collection::add,
                mergeCollection()
        );
    }

    /**
     * 集合转换器
     *
     * @param supplier 集合提供者
     * @param mapper   映射集合内元素
     * @param <E>      集合中元素的类型
     * @param <R>      经过映射后的类型
     * @param <S>      集合类型
     */
    public static <E, R, S extends Collection<R>> Collector<E, S, S> toCollection(Supplier<S> supplier, Function<E, R> mapper) {
        return Collector.of(
                supplier,
                (collection, s) -> collection.add(mapper.apply(s)),
                mergeCollection()
        );
    }

    /**
     * TreeSet转换器
     *
     * @param comparator 比较TreeSet元素的顺序
     * @param <E>        集合内元素的类型
     */
    public static <E> Collector<E, TreeSet<E>, TreeSet<E>> toTreeSet(Comparator<E> comparator) {
        return toCollection(() -> new TreeSet<>(comparator));
    }

    /**
     * HashSet转换器
     *
     * @param mapper 映射器
     * @param <E>    原始集合中元素的类型
     * @param <R>    映射后的类型
     */
    public static <E, R> Collector<E, HashSet<R>, HashSet<R>> toHashSet(Function<E, R> mapper) {
        return toCollection(HashSet::new, mapper);
    }

    /**
     * ArrayList转换器
     *
     * @param mapper 映射器
     * @param <E>    原始集合中元素的类型
     * @param <R>    映射后的类型
     */
    public static <E, R> Collector<E, ArrayList<R>, ArrayList<R>> toArrayList(Function<E, R> mapper) {
        return toCollection(ArrayList::new, mapper);
    }


}
