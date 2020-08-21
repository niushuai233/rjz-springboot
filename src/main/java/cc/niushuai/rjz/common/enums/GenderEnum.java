package cc.niushuai.rjz.common.enums;

/**
 * @author ns
 * @date 2020/8/21
 */
public enum GenderEnum {

    UNKONWN("0", "未知"),
    GENTLEMAN("1", "男"),
    LADY("2", "女");

    private String code;
    private String desc;

    GenderEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getGender(String code) {
        for (GenderEnum genderEnum : GenderEnum.values()) {
            if (genderEnum.getCode().equals(code)) {
                return genderEnum.getDesc();
            }
        }
        return GenderEnum.UNKONWN.getDesc();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
