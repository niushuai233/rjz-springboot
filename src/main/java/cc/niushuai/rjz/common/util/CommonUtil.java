package cc.niushuai.rjz.common.util;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ns
 * @date 2020/1/14
 */
public class CommonUtil {

    private static final Logger log = LoggerFactory.getLogger(CommonUtil.class);


    /**
     * 首字母大写
     *
     * @param str
     * @return String
     *
     * @author ns
     * @date 2020/1/14 9:03
     **/
    public static String upperFirstChar(String str) {
        if (null == str || "".equals(str.trim())) {
            return str;
        }
        return (str.charAt(0) + "").toUpperCase() + str.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param str
     * @return String
     *
     * @author ns
     * @date 2020/1/14 9:03
     **/
    public static String lowerFirstChar(String str) {
        if (null == str || "".equals(str.trim())) {
            return str;
        }
        return (str.charAt(0) + "").toLowerCase() + str.substring(1);
    }

    /**
     * 将指定的字符替换为空串
     *
     * @param str
     * @param keys
     * @return java.lang.String
     *
     * @author ns
     * @date 2020/1/14 9:19
     **/
    public static String replaceAllEmpty(String str, String... keys) {
        for (String key : keys) {
            str = str.replace(key, "");
        }
        return str;
    }

    /**
     * 字符串集合转字符串数组
     *
     * @param strList 字符串集合
     * @return String[] 转换后的字符串结果
     *
     * @author ns
     * @date 2020/1/14 9:35
     **/
    public static String[] list2StringArray(List<String> strList) {
        if (CollectionUtils.isEmpty(strList)) {
            return null;
        }

        String[] strArr = new String[strList.size()];

        for (int i = 0; i < strList.size(); i++) {
            strArr[i] = strList.get(i);
        }

        return strArr;
    }


    /**
     * 利用反射替换字符串中的值
     *
     * @param content 消息体
     * @param t       实体类
     * @return String 替换后的结果
     *
     * @author ns
     * @date 2020/1/14 9:27
     **/
    public static <T> String fillContent(String content, T t) {
        int leftIndex = content.indexOf("${");
        if (leftIndex != -1) {
            int rightIndex = content.indexOf("}");
            String keyword = content.substring(leftIndex, rightIndex + 1);

            String entityValue = "";
            String tmpKeyword = CommonUtil.upperFirstChar(CommonUtil.replaceAllEmpty(keyword, "${", "}"));

            try {
                // 捕捉异常, 如果找不到该字段就放弃
                Class<?> tClass = t.getClass();
                entityValue = String.valueOf(tClass.getMethod("get" + tmpKeyword).invoke(t));
            } catch (Exception e) {
                log.error("{} 不存在方法 {}", t.getClass(), "get" + tmpKeyword);
            }

            return fillContent(content.replace(keyword, entityValue), t);
        }

        return content;
    }


    /**
     * list 转 map
     *
     * @param list  待转换的list
     * @param key   指定的字段当key
     * @param field 指定的字段当value 如果为空 则使用list中的对象做value
     * @return Map<Object, Object>
     *
     * @author ns
     * @date 2020/1/16 16:39
     **/
    public static Map list2Map(List list, String key, String field) throws Exception {

        Map<Object, Object> resultMap = new HashMap<Object, Object>();

        if (CollectionUtils.isEmpty(list)) {
            return resultMap;
        }

        for (Object t : list) {
            Object invoke = t.getClass().getMethod("get" + upperFirstChar(key)).invoke(t);
            Object val = t;
            if (StringUtils.isNotEmpty(field)) {
                val = t.getClass().getMethod("get" + upperFirstChar(field)).invoke(t);
            }
            resultMap.put(invoke, val);
        }
        return resultMap;
    }
}