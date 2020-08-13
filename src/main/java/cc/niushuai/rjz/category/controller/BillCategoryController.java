package cc.niushuai.rjz.category.controller;

import cc.niushuai.rjz.category.entity.BillCategory;
import cc.niushuai.rjz.category.service.BillCategoryService;
import cc.niushuai.rjz.common.bean.R;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author ns
 * @date 2020/8/13
 */
@Slf4j
@RestController
@RequestMapping("/bill/category")
public class BillCategoryController {

    @Resource
    private BillCategoryService billCategoryService;

    @PostMapping("/billCategoryList")
    public R billCategoryList(@RequestBody BillCategory billCategory) {

        QueryWrapper<BillCategory> wrapper = new QueryWrapper<>();
        wrapper.eq(BillCategory.COL_USER_ID, billCategory.getUserId())
                .eq(BillCategory.COL_RECORD_TYPE, billCategory.getRecordType())
                .eq(BillCategory.COL_IS_DELETE, 1);
        List<BillCategory> billCategoryList = billCategoryService.list(wrapper);

        return R.ok().put("categoryList", billCategoryList);
    }

    @PostMapping("/addBillCategory")
    public R addBillCategory(@RequestBody BillCategory billCategory) {

        billCategory.setCreateTime(new Date());
        billCategory.setUpdateTime(new Date());
        billCategoryService.save(billCategory);

        return R.ok().put("code", 1);
    }

    @PostMapping("/deleteBillCategory")
    public R deleteBillCategory(@RequestBody BillCategory billCategory) {

        billCategory.setCreateTime(new Date());
        billCategory.setUpdateTime(new Date());
        billCategory.setIsDelete(0);
        billCategoryService.updateById(billCategory);

        return R.ok().put("code", 1);
    }
}
