package cc.niushuai.rjz.bill.controller;

import cc.niushuai.rjz.bill.entity.BillNote;
import cc.niushuai.rjz.bill.entity.BillNoteVo;
import cc.niushuai.rjz.bill.service.BillNoteService;
import cc.niushuai.rjz.common.bean.R;
import cc.niushuai.rjz.common.enums.BillTypeEnum;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author ns
 * @date 2020/8/13
 */
@Slf4j
@RestController
@RequestMapping("/bill/note")
public class BillNoteController {

    @Resource
    private BillNoteService billNoteService;

    @PostMapping("/billNoteList")
    public R billNoteList(@RequestBody BillNote billNote) {

        QueryWrapper<BillNote> wrapper = new QueryWrapper<>();
        wrapper.eq(BillNote.COL_USER_ID, billNote.getUserId())
                .eq(BillNote.COL_BILL_YEAR, billNote.getBillYear())
                .eq(BillNote.COL_BILL_MONTH, billNote.getBillMonth())
                .eq(BillNote.COL_IS_DELETE, 1)
                .orderByDesc(BillNote.COL_BILL_YEAR)
                .orderByDesc(BillNote.COL_BILL_MONTH)
                .orderByDesc(BillNote.COL_BILL_DAY)
                .orderByDesc(BillNote.COL_CREATE_TIME)
        ;
        List<BillNote> billNoteList = billNoteService.list(wrapper);

        String totalIncomeAmount = "0.00";
        String totalPayAmount = "0.00";

        Map<String, Object> resultMap = new HashMap<>();

        if (CollectionUtil.isNotEmpty(billNoteList)) {
            Set<String> timeSet = new HashSet<>();
            billNoteList.forEach(item -> {
                timeSet.add(getYMD(item));
            });
            Map<String, BillNoteVo> tmpMap = new HashMap<>();
            timeSet.forEach(time -> tmpMap.put(time, new BillNoteVo(time)));

            billNoteList.forEach(item -> {

                String ymd = getYMD(item);
                BillNoteVo noteVo = tmpMap.get(ymd);
                noteVo.getSubBillNoteList().add(item);

                if (BillTypeEnum.PAY.getType() == item.getBillType()) {
                    noteVo.setPayAmount(new BigDecimal(noteVo.getPayAmount()).add(new BigDecimal(item.getPrice())).setScale(2, RoundingMode.HALF_UP).toString());
                } else if (BillTypeEnum.INCOME.getType() == item.getBillType()) {
                    noteVo.setIncomeAmount(new BigDecimal(noteVo.getIncomeAmount()).add(new BigDecimal(item.getPrice())).setScale(2, RoundingMode.HALF_UP).toString());
                }

                tmpMap.put(ymd, noteVo);
            });

            // 对时间进行排序
            Collection<BillNoteVo> billNoteVos = tmpMap.values();
            billNoteVos = CollectionUtil.sort(billNoteVos, (o1, o2) -> {
                int v1 = Long.valueOf(o1.getTime().replace("-", "")).intValue();
                int v2 = Long.valueOf(o2.getTime().replace("-", "")).intValue();
                return v2 - v1;
            });
            resultMap.put("billNoteList", billNoteVos);

            for (BillNoteVo noteVo : billNoteVos) {
                totalPayAmount = new BigDecimal(totalPayAmount).add(new BigDecimal(noteVo.getPayAmount())).setScale(2, RoundingMode.HALF_UP).toString();
                totalIncomeAmount = new BigDecimal(totalIncomeAmount).add(new BigDecimal(noteVo.getIncomeAmount())).setScale(2, RoundingMode.HALF_UP).toString();
            }
        }

        return R.ok().put("statusCode", 200).put("data", resultMap).put("totalPayAmount", totalPayAmount).put("totalIncomeAmount", totalIncomeAmount);
    }

    private String getYMD(BillNote item) {
        Integer billDay = item.getBillDay();
        String billDayStr = billDay + "";
        if (billDay < 10) {
            billDayStr = "0" + billDay;
        }
        Integer billMonth = item.getBillMonth();
        String billMonthStr = billMonth + "";
        if (billMonthStr.length() == 1) {
            billMonthStr = "0" + billMonth;
        }
        return item.getBillYear() + "-" + billMonthStr + "-" + billDayStr;
    }

    @PostMapping("/addBillNote")
    public R addBillNote(@RequestBody BillNote billNote) {


        billNote.setIsDelete(1);
        billNote.setCreateTime(new Date());
        billNote.setUpdateTime(new Date());
        billNoteService.save(billNote);
        return R.ok().put("code", 1);
    }

    @PostMapping("/deleteBillNote")
    public R deleteBillNote(@RequestBody BillNote billNote) {

        billNote.setIsDelete(0);
        billNote.setCreateTime(new Date());
        billNote.setUpdateTime(new Date());
        billNoteService.updateById(billNote);
        return R.ok().put("code", 1);
    }
}
