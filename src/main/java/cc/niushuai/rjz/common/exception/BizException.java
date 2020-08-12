package cc.niushuai.rjz.common.exception;

import cc.niushuai.rjz.common.enums.ResultStatusEnum;

public class BizException extends RuntimeException {
    private static final long serialVersionUID = 1116795940582249536L;
    private String msg;
    private int code;

    public BizException(String msg) {
        super(msg);
        this.code = ResultStatusEnum.HTTP_500.code;
        this.msg = msg;
    }

    public BizException(String msg, Throwable e) {
        super(msg, e);
        this.code = ResultStatusEnum.HTTP_500.code;
        this.msg = msg;
    }

    public BizException(String msg, int code) {
        super(msg);
        this.code = ResultStatusEnum.HTTP_500.code;
        this.msg = msg;
        this.code = code;
    }

    public BizException(String msg, int code, Throwable e) {
        super(msg, e);
        this.code = ResultStatusEnum.HTTP_500.code;
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
