package cc.niushuai.rjz.common.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

/**
 * RestTemplate 工具类
 *
 * @author ns
 * @date 2020/8/13 15:24
 **/
@Configuration
public class RestTemplateUtil {
    private static RestTemplate restTemplate;

    public RestTemplateUtil() {
    }

    public static <T> T get(String url, Class<T> clazz, String... param) {

        return restTemplate.getForObject(url, clazz, param);
    }

    public static <T> T post(String url, Map<String, Object> headerMap, String body, Class<T> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Encoding", "UTF-8");
        headers.add("Content-Type", "application/json");

        // 填充传入的headerMap
        if (headerMap != null) {
            headerMap.keySet().forEach(headerKey -> headers.add(headerKey, headerMap.get(headerKey) + ""));
        }

        HttpEntity<String> formEntity = new HttpEntity(body, headers);
        return restTemplate.postForObject(url, formEntity, clazz, new Object[0]);
    }

    @Resource
    public void setRestTemplate(RestTemplate restTemplate) {
        RestTemplateUtil.restTemplate = restTemplate;
    }
}
