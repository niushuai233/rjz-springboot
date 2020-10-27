package cc.niushuai.rjz.user.service;

import cc.niushuai.rjz.user.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserInfoService extends IService<UserInfo> {

    UserInfo login(UserInfo userInfo);
}
