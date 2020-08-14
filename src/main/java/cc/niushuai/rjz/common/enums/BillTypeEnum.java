package cc.niushuai.rjz.common.enums;

/**
 * @author ns
 * @date 2020/8/14
 */
public enum BillTypeEnum {

    PAY(0, "支出"),
    INCOME(1, "收入");

    private int type;
    private String desc;

    BillTypeEnum(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
