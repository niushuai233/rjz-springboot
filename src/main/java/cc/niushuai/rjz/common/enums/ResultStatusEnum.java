package cc.niushuai.rjz.common.enums;

public enum ResultStatusEnum {
    /**
     * 成功
     */
    SUCCESS(200, "成功"),
    /**
     * 找不到路由
     */
    HTTP_404(404, "路径不存在，请检查路径是否正确"),
    /**
     * 500异常
     */
    HTTP_500(500, "未知异常，请联系管理员"),
    /**
     * 临时的code为空
     */
    WX_CODE_ID_NULL(1501, "open id is null"),
    /**
     * openid为空
     */
    WX_OPEN_ID_NULL(1502, "wx login tmp code is null"),
    WX_CODE_SESSION_FAILURE(1502, "wx login tmp code is null"),
    TEST(99999, "test");

    /**
     * 代码
     */
    public int code;
    /**
     * 描述信息
     */
    public String description;

    ResultStatusEnum(int code, String description) {
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
