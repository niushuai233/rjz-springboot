package cc.niushuai.rjz.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(10000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.getMessageConverters().add(new RestTemplateConfig.WMappingJackson2HttpMessageConverter());
        return restTemplate;
    }

    public class WMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        public WMappingJackson2HttpMessageConverter() {
            List<MediaType> mediaTypes = new ArrayList();
            mediaTypes.add(MediaType.TEXT_PLAIN);
            mediaTypes.add(MediaType.TEXT_HTML);
            mediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
            this.setSupportedMediaTypes(mediaTypes);
        }
    }
}
