package cc.niushuai.rjz.common;

import cc.niushuai.rjz.common.cache.ICacheManager;
import cc.niushuai.rjz.common.util.KeyConstant;
import cc.niushuai.rjz.user.entity.UserToken;
import cc.niushuai.rjz.user.service.UserTokenService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author ns
 * @date 2020/8/21
 */
@Slf4j
@Component
public class TokenScheduler {

    @Resource
    private ICacheManager iCacheManager;

    @Resource
    private UserTokenService userTokenService;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void removeExpireToken() {

        try {
            Map<String, UserToken> tokenMap = (Map<String, UserToken>) iCacheManager.getCacheDataByKey(KeyConstant.USER_TOKEN);

            if (null != tokenMap) {
                Set<String> keys = tokenMap.keySet();
                Set<String> needRemoveTokens = new HashSet<>();
                for (String token : keys) {
                    UserToken userToken = tokenMap.get(token);
                    log.info("token: {}, 当前时间: {}, token时间:{}, 过期否? {}", token, DateUtil.now(),
                            DateUtil.formatDateTime(userToken.getExpireTime()), System.currentTimeMillis() > userToken.getExpireTimeMills() ? "是" : "否");
                    if (System.currentTimeMillis() > userToken.getExpireTimeMills()) {
                        needRemoveTokens.add(token);
                    }
                }

                for (String needRemoveToken : needRemoveTokens) {
                    tokenMap.remove(needRemoveToken);
                    UpdateWrapper<UserToken> updateWrapper = new UpdateWrapper<>(UserToken.builder().token(needRemoveToken).build());
                    userTokenService.update(UserToken.builder().isExpire(1).build(), updateWrapper);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
