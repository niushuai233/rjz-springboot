package cc.niushuai.rjz.common.runner;

import cc.niushuai.rjz.common.cache.ICacheManager;
import cc.niushuai.rjz.common.util.KeyConstant;
import cc.niushuai.rjz.user.entity.UserToken;
import cc.niushuai.rjz.user.service.UserTokenService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ns
 * @date 2020/8/14
 */
@Component
public class TokenMapRunner implements ApplicationRunner {

    @Resource
    private UserTokenService userTokenService;
    @Resource
    private ICacheManager iCacheManager;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Map<String, UserToken> map = new HashMap<>();

        QueryWrapper<UserToken> wrapper = new QueryWrapper<>();
        wrapper.eq(UserToken.COL_IS_EXPIRE, 0);
        List<UserToken> list = userTokenService.list(wrapper);

        for (UserToken userToken : list) {
            userToken.setExpireTimeMills(userToken.getExpireTime().getTime());
            map.put(userToken.getToken(), userToken);
        }

        iCacheManager.putCache(KeyConstant.USER_TOKEN, map);
    }
}
