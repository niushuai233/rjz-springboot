package cc.niushuai.rjz.user.service.impl;

import cc.niushuai.rjz.user.entity.UserToken;
import cc.niushuai.rjz.user.mapper.UserTokenMapper;
import cc.niushuai.rjz.user.service.UserTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserTokenServiceImpl extends ServiceImpl<UserTokenMapper, UserToken> implements UserTokenService {

}
