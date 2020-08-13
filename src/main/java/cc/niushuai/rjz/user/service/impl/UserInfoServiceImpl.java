package cc.niushuai.rjz.user.service.impl;

import cc.niushuai.rjz.user.entity.UserInfo;
import cc.niushuai.rjz.user.mapper.UserInfoMapper;
import cc.niushuai.rjz.user.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Override
    public String login(UserInfo userInfo) {

        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .eq(UserInfo.COL_OPEN_ID, userInfo.getOpenId())
                .eq(UserInfo.COL_IS_DELETE, 1);

        UserInfo user = baseMapper.selectOne(queryWrapper);

        if (user != null) {
            // 存在该openid 直接返回即可
            userInfo.setId(user.getId());
            user.setLastLoginTime(new Date());
            baseMapper.updateById(user);
            // 更新上次登陆时间
        } else {
            // 不存在该openid 新增即可
            userInfo.setIsDelete(1);
            userInfo.setCreateTime(new Date());
            userInfo.setLastLoginTime(new Date());
            baseMapper.insert(userInfo);
        }
        return null;
    }
}
