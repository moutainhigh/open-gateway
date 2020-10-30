package org.open.gateway.portal.utils;

import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Created by miko on 2020/7/24.
 *
 * @author MIKO
 */
public class Beans {

    // 缓存的拷贝器
    private static final Map<String, BeanCopier> BEAN_COPIER_CACHE = new ConcurrentHashMap<>();
    // 默认的转换器
    private static final Converter DEFAULT_CONVERTER = new DefaultBeanConverter();

    /**
     * 获取拷贝器
     *
     * @param sourceClass 原始类
     * @param targetClass 目标类
     */
    private static BeanCopier getBeanCopier(Class<?> sourceClass, Class<?> targetClass, Converter converter) {
        String beanKey = generateKey(sourceClass, targetClass);
        if (BEAN_COPIER_CACHE.containsKey(beanKey)) {
            return BEAN_COPIER_CACHE.get(beanKey);
        }
        BeanCopier copier = BeanCopier.create(sourceClass, targetClass, converter == null); // 生成代理类
        BEAN_COPIER_CACHE.put(beanKey, copier);
        return copier;
    }

    /**
     * 生成缓存key
     *
     * @param class1 类1
     * @param class2 类2
     */
    private static String generateKey(Class<?> class1, Class<?> class2) {
        return class1.toString() + class2.toString();
    }

    public static BeanCopierHelper copier() {
        return new BeanCopierHelper();
    }

    public static BeanCopierHelper from(Object source) {
        return copier().source(source);
    }

    static class DefaultBeanConverter implements Converter {

        @Override
        public Object convert(Object value, Class target, Object context) {
            return value;
        }

    }

    public static class BeanCopierHelper {

        private Object source;
        private Converter converter = DEFAULT_CONVERTER;

        public BeanCopierHelper source(Object source) {
            this.source = Objects.requireNonNull(source, "source is required.");
            return this;
        }

        public BeanCopierHelper converter(Converter converter) {
            this.converter = converter;
            return this;
        }

        public void copy(Object target) {
            Objects.requireNonNull(target, "target is required.");
            getBeanCopier(source.getClass(), target.getClass(), converter).copy(source, target, converter);
        }

        public void copy(Supplier<Object> supplier) {
            copy(supplier.get());
        }

        public <T> T convert(T target) {
            copy(target);
            return target;
        }

        public <T> T convert(Supplier<T> supplier) {
            return convert(supplier.get());
        }

    }

}
