package cc.niushuai.rjz.user.service.impl;

import cc.niushuai.rjz.category.entity.BillCategory;
import cc.niushuai.rjz.category.mapper.BillCategoryMapper;
import cc.niushuai.rjz.user.entity.DefaultCategory;
import cc.niushuai.rjz.user.entity.UserInfo;
import cc.niushuai.rjz.user.mapper.UserInfoMapper;
import cc.niushuai.rjz.user.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private BillCategoryMapper billCategoryMapper;

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

            // 添加默认分类
            addDefinedCategory(userInfo);
        }
        return null;
    }

    private void addDefinedCategory(UserInfo userInfo) {
        List<DefaultCategory> defaultCategoryList = getDefaultCategory();

        for (DefaultCategory defaultCategory : defaultCategoryList) {

            BillCategory entity = new BillCategory();
            entity.setUserId(userInfo.getId());
            entity.setIsDelete(1);
            entity.setCreateTime(new Date());
            entity.setIsDefault(1);
            entity.setRecordType(defaultCategory.getRecordType());
            entity.setCategoryName(defaultCategory.getCategoryName());
            entity.setIconClassName(defaultCategory.getIconClassName());
            billCategoryMapper.insert(entity);
        }
    }

    private List<DefaultCategory> getDefaultCategory() {
        ArrayList<DefaultCategory> defaultCategories = new ArrayList<>();
        // 支出
        defaultCategories.add(new DefaultCategory("1", "icon-yuangonggongzi", "工资"));
        defaultCategories.add(new DefaultCategory("1", "icon-lijin", "礼金"));
        defaultCategories.add(new DefaultCategory("1", "icon-licai", "理财"));
        defaultCategories.add(new DefaultCategory("1", "icon-jianzhi", "兼职"));

        // 收入
        defaultCategories.add(new DefaultCategory("0", "icon-canju", "饮食"));
        defaultCategories.add(new DefaultCategory("0", "icon-youxi", "娱乐"));
        defaultCategories.add(new DefaultCategory("0", "icon-lvyou", "旅游"));
        defaultCategories.add(new DefaultCategory("0", "icon-jiaotong", "交通"));
        defaultCategories.add(new DefaultCategory("0", "icon-gouwu", "购物"));
        defaultCategories.add(new DefaultCategory("0", "icon-huaban", "医疗"));
        defaultCategories.add(new DefaultCategory("0", "icon-xuexi-", "学习"));

        return defaultCategories;
    }
}
