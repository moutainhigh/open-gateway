package open.gateway.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by miko on 2018/11/15.
 *
 * @apiNote Jackson相关操作
 */
public abstract class JSON {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    static {
        JSON_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL); // 设置不序列化null字段
        JSON_MAPPER.setTimeZone(TimeZone.getDefault()); // 设置时区
        JSON_MAPPER.setDateFormat(new SimpleDateFormat(DATE_FORMAT)); // 设置日期转换格式
        JSON_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 设置json字符串中出现未知属性不报错
    }

    public static ObjectMapper getJsonMapper() {
        return JSON_MAPPER;
    }

    /**
     * 对象转json
     *
     * @param o 源数据
     * @return json格式字符串
     * @apiNote 对象转json
     */
    public static String toJSONString(Object o) {
        if (o == null) {
            return null;
        }
        try {
            return JSON_MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    /**
     * json转对象
     *
     * @param json  json格式字符串
     * @param clazz 类型
     * @return 转换后的对象
     * @apiNote json转对象
     */
    public static <T> T parse(String json, Class<T> clazz) {
        if (json == null || json.length() < 1) {
            return null;
        }
        try {
            return JSON_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * json转node
     *
     * @param json json格式字符串
     * @return jsonNode
     */
    public static JsonNode parse(String json) {
        try {
            return JSON_MAPPER.readTree(json);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * 字节数组转对象
     *
     * @param content 字节数组
     * @param clazz   类型
     * @return json对象
     */
    public static <T> T parse(byte[] content, Class<T> clazz) {
        if (content == null) {
            return null;
        }
        try {
            return JSON_MAPPER.readValue(new String(content), clazz);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * json转集合类型
     *
     * @param json  json
     * @param clazz 集合中对象的类型
     * @return 对象集合
     */
    public static <T> List<T> parseArrayList(String json, Class<T> clazz) {
        return parseCollection(json, clazz, ArrayList.class);
    }

    /**
     * json转集合类型
     *
     * @param json                json
     * @param clazz               集合中对象的类型
     * @param collectionTypeClazz 集合类型
     * @return 对象集合
     */
    public static <T> List<T> parseCollection(String json, Class<T> clazz, Class<?> collectionTypeClazz) {
        if (json == null || json.length() < 1) {
            return null;
        }
        try {
            return JSON_MAPPER.readValue(json, getCollectionType(collectionTypeClazz, clazz));
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * 获取json中指定的字段值
     *
     * @param json json字符串
     * @param node key
     */
    public static String getJsonNodeValue(String json, String node) {
        JsonNode jsonNode;
        try {
            jsonNode = JSON_MAPPER.readTree(json).get(node);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (jsonNode == null) {
            return null;
        }
        return jsonNode.toString();
    }

    private static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return JSON_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static class JsonException extends RuntimeException {

        public JsonException(String msg) {
            super(msg);
        }

        public JsonException(Throwable e) {
            super(e);
        }

        public JsonException(String message, Throwable cause) {
            super(message, cause);
        }

    }

}
