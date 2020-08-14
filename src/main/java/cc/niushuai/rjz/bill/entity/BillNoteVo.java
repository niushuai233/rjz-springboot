package cc.niushuai.rjz.bill.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ns
 * @date 2020/8/13
 */
@Data
public class BillNoteVo {

    private String time;
    private String incomeAmount;
    private String payAmount;
    private List<BillNote> subBillNoteList = new ArrayList<>();

    public BillNoteVo(String time) {
        this.time = time;
        this.incomeAmount = "0";
        this.payAmount = "0";
    }
}
