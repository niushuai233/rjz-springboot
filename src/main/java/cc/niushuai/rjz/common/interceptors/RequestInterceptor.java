package cc.niushuai.rjz.common.interceptors;

import cc.niushuai.rjz.common.cache.ICacheManager;
import cc.niushuai.rjz.common.enums.ResultStatusEnum;
import cc.niushuai.rjz.common.exception.BizException;
import cc.niushuai.rjz.common.util.ApplicationContextUtils;
import cc.niushuai.rjz.common.util.KeyConstant;
import cc.niushuai.rjz.user.entity.UserToken;
import cc.niushuai.rjz.user.service.UserTokenService;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author ns
 * @date 2020/8/14
 */
@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.info("req url - {}", request.getRequestURL());

        // 是否存在token
        String token = request.getHeader(KeyConstant.TOKEN);
        if (StrUtil.isEmpty(token)) {
            return false;
        }
        log.info("token: {}", token);

        // token是否有效
        ICacheManager iCacheManager = ApplicationContextUtils.getBean(ICacheManager.class);
        Map<String, UserToken> dataByKey = (Map<String, UserToken>) iCacheManager.getCacheDataByKey(KeyConstant.USER_TOKEN);
        if (null == dataByKey) {
            // token 失效
            log.info("token 失效");
            throw new BizException(ResultStatusEnum.TOKEN_VAILD);
        }

        UserToken userToken = dataByKey.get(token);
        if (null == userToken) {
            throw new BizException(ResultStatusEnum.TOKEN_VAILD);
        }

        if (System.currentTimeMillis() > userToken.getExpireTimeMills()) {
            UserTokenService bean = ApplicationContextUtils.getBean(UserTokenService.class);
            userToken.setIsExpire(1);
            bean.updateById(userToken);
            log.info("token 失效");
            throw new BizException(ResultStatusEnum.TOKEN_VAILD);
        }

        log.info("token 有效");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
