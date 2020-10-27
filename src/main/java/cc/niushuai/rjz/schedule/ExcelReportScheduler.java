package cc.niushuai.rjz.schedule;

import cc.niushuai.rjz.bill.entity.BillNote;
import cc.niushuai.rjz.bill.service.BillNoteService;
import cc.niushuai.rjz.common.util.CommonUtil;
import cc.niushuai.rjz.common.util.MailUtil;
import cc.niushuai.rjz.common.util.excel.ExcelUtil;
import cc.niushuai.rjz.user.entity.UserInfo;
import cc.niushuai.rjz.user.service.UserInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.format.FastDateFormat;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;


/**
 * 每月最后一号推送消息到微信
 *
 * @author ns
 * @date 2020/10/26
 */
@Slf4j
@Component
public class ExcelReportScheduler {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private BillNoteService billNoteService;

    @Value("${niushuai.rjz.title:rjz账单}")
    private String title;

    @Value("${niushuai.rjz.content:rjz账单}")
    private String content;

    @Scheduled(cron = "${niushuai.rjz.excel-cron:0 30 21 28-31 * ?}")
    public void run() {

        // 最后一天 才执行
        if (CommonUtil.todayIsLastDay()) {
            log.info("是最后一天");

            List<UserInfo> list = userInfoService.list();

            if (CollectionUtil.isNotEmpty(list)) {
                list.forEach(item -> task(item));
            }
        } else {
            log.info("非最后一天");
        }
    }

    private void task(UserInfo item) {
        if (StrUtil.isEmpty(item.getNickName())) {
            return;
        }
        try {


            QueryWrapper<BillNote> wrapper = new QueryWrapper<BillNote>();
            wrapper.eq(BillNote.COL_USER_ID, item.getId())
                    .eq(BillNote.COL_BILL_YEAR, DateUtil.year(new Date()))
                    .eq(BillNote.COL_BILL_MONTH, DateUtil.month(new Date()) + 1)
                    .eq(BillNote.COL_IS_DELETE, 1)
                    .orderByAsc(BillNote.COL_BILL_DAY);

            List<BillNote> billNotes = billNoteService.list(wrapper);

            if (CollectionUtil.isEmpty(billNotes)) {
                return;
            }

            Workbook workBook = ExcelUtil.getWorkBook(billNotes, BillNote.class, null);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            workBook.write(byteArrayOutputStream);

            String yyyyMM = DateUtil.format(new Date(), FastDateFormat.getInstance("yyyyMM"));
            MailUtil.sendMimeMessageWithAttachFile(title + yyyyMM, content + yyyyMM, getFileName(item.getNickName()), new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), item.getEmail());

        } catch (Exception e) {
            log.error("向 {} 发送账单附件到邮箱失败: {}", item, e.getMessage(), e);
        } finally {
            log.info("导出 {} 完成", item.getNickName());
        }
    }

    private String getFileName(String nickName) {
        return "rjz账单-" + nickName + "-" + DateUtil.format(new Date(), FastDateFormat.getInstance("yyyyMM")) + ".xlsx";
    }
}
