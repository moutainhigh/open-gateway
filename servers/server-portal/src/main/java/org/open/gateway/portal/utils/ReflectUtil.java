package org.open.gateway.portal.utils;

import com.esotericsoftware.reflectasm.ConstructorAccess;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Created by miko on 2020/7/24.
 *
 * @author MIKO
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class ReflectUtil {

    /**
     * 缓存的构造方法
     */
    private static final Map<String, ConstructorAccess> CONSTRUCTOR_ACCESS_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取构造器
     *
     * @param targetClass 目标类
     * @return 构造器
     */
    public static ConstructorAccess getConstructorAccess(Class<?> targetClass) {
        ConstructorAccess constructorAccess = CONSTRUCTOR_ACCESS_CACHE.get(targetClass.toString());
        if (constructorAccess == null) {
            constructorAccess = ConstructorAccess.get(targetClass);
            CONSTRUCTOR_ACCESS_CACHE.put(targetClass.toString(), constructorAccess);
            return constructorAccess;
        }
        return constructorAccess;
    }

    /**
     * 创建一个实例
     *
     * @param targetClass 目标类
     * @return 实例
     */
    public static <T> T newInstance(Class<T> targetClass) {
        return (T) getConstructorAccess(targetClass).newInstance();
    }

    /**
     * 获取接口泛型类
     *
     * @param clazz 接口类
     * @return 返回泛型类
     */
    public static Class<?> getSuperInterfaceClass(Class<?> clazz) {
        Type[] types = clazz.getGenericInterfaces();
        for (Type t : types) {
            ParameterizedType pt = (ParameterizedType) t;
            // 父接口名称
            String superInterfaceName = pt.getRawType().getTypeName();
            if (superInterfaceName != null) {
                return (Class<?>) pt.getActualTypeArguments()[0];
            }
        }
        throw new IllegalArgumentException(String.format("clazz:%s can not found parent type argument", clazz));
    }

    /**
     * 获取所有字段
     *
     * @param typeClass     类型
     * @param includeParent 是否包括父类
     * @param filter        过滤器
     * @return 字段集合
     */
    public static List<Field> getFields(Class<?> typeClass, boolean includeParent, Function<Field, Boolean> filter) {
        List<Field> fields = new ArrayList<>();
        Class<?> clazz = typeClass;
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                // 筛选出非静态，非final的字段
                if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                    // 满足自定义过滤器
                    if (filter == null || filter.apply(field)) {
                        fields.add(field);
                    }
                }
            }
            // 获取父类
            if (includeParent) {
                clazz = clazz.getSuperclass();
            } else {
                clazz = null;
            }
        }
        return fields;
    }

    /**
     * 获取所有字段
     *
     * @param typeClass 类型
     * @return 字段集合
     */
    public static List<Field> getFields(Class<?> typeClass) {
        return getFields(typeClass, true, null);
    }

}
