package cc.niushuai.rjz.common.enums;

public enum ResultStatusEnum {
    SUCCESS(200, "成功"),
    HTTP_404(404, "路径不存在，请检查路径是否正确"),
    HTTP_500(500, "未知异常，请联系管理员");

    public int code;
    public String description;

    private ResultStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
