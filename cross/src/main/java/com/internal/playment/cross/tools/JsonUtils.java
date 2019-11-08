package com.internal.playment.cross.tools;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/2/6
 * Time: 15:19
 * Project: Management
 * Package: com.rxh.utils
 */
public class JsonUtils {
    // 定义jackson对象
    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     *
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            logger.error("JsonUtils", e);
        }
        return null;
    }

    /**
     * 将对象转换成json字符串空字段不添加。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     *
     * @param data
     * @return
     */
    public static String objectToJsonNonNull(Object data) {
        try {
            MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            logger.error("JsonUtils", e);
        }
        return null;
    }

    /**
     * 将json结果集转化为对象
     *
     * @param jsonData json数据
     * @param beanType 对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            // 反序列化忽略Bean对象没有的字段
            MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;//修改
            return MAPPER.readValue(jsonData, beanType);
        } catch (Exception e) {
            logger.error("JsonUtils", e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 支持Json转带泛型类，
     * 如：Map<String, Object> -> jsonToPojo(String jsonData, Map.class, String.class, Object.class)
     *
     * @param jsonData    Json字符串
     * @param beanType
     * @param elementType
     * @param <T>
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<?> beanType, Class<?>... elementType) {
        try {
            // 反序列化忽略Bean对象没有的字段
            MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;//修改
            return MAPPER.readValue(jsonData, MAPPER.getTypeFactory().constructParametricType(beanType, elementType));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("JsonUtils", e);
        }
        return null;
    }

    public static <T> T jsonToPojo(String jsonData, JavaType javaType) {
        try {
            // 反序列化忽略Bean对象没有的字段
            MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;//修改
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("JsonUtils", e);
        }
        return null;
    }

    public static JavaType getJavaType(Type type) {
        return MAPPER.getTypeFactory().constructType(type);
    }

    /**
     * 示例：List<Integer> list = JsonUtils.MAPPER.readValue("[1,2]", JsonUtils.getJavaType(List.class, Integer.class));
     *
     * @param parametrized
     * @param parameterClasses
     * @return
     */
    public static JavaType getJavaType(Class<?> parametrized, Class<?>... parameterClasses) {
        return MAPPER.getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }

    public static JavaType getJavaType(Class<?> rawType, JavaType... parameterTypes) {
        return MAPPER.getTypeFactory().constructParametricType(rawType, parameterTypes);
    }

    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     *
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) {
        JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            return MAPPER.readValue(jsonData, javaType);
        } catch (Exception e) {
            logger.error("JsonUtils", e);
        }
        return null;
    }

    public static JsonNode json2JsonNode(String jsonStr) {
        try {
            return new ObjectMapper().readTree(jsonStr);
        } catch (IOException e) {
            logger.error("JsonUtils", e);
        }
        return null;
    }
    
    public static <T> T readValue(String content, JavaType valueType) {
        try {
            return MAPPER.readValue(content, valueType);
        } catch (IOException e) {
            logger.error("JsonUtils", e);
        }
        return null;
    }

    /**
     *  将json数据转换成List<Map<String,Object></>
     */
     /*public static List<Map<String,Object>> jsonToListMap(String jsonData) {
         try {
             return MAPPER.readValue(jsonData,List.class);
         } catch (IOException e) {
             logger.error("JsonUtils", e);
         }
         return null;
     }*/

    /**
     *  将json数据转换成Map<String,Object></>
     */
    public static Map<String,Object> jsonToMap(String jsonData) {
        try {
            return MAPPER.readValue(jsonData, Map.class);
        } catch (IOException e) {
            logger.error("JsonUtils", e);
        }
        return null;
    }

    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
//        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }
}
