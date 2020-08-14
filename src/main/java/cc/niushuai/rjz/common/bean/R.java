package cc.niushuai.rjz.common.bean;

import cc.niushuai.rjz.common.enums.ResultStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class R extends HashMap<String, Object> {
    private static final long serialVersionUID = -1131681874214638255L;

    public R() {
        this.put("code", 0);
        this.put("msg", "success");
    }

    public static R error() {
        return error(ResultStatusEnum.HTTP_500);
    }

    public static R error(String msg) {
        return error(ResultStatusEnum.HTTP_500.getCode(), msg);
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R ok(int code, String msg) {
        R r = new R();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static R error(ResultStatusEnum resultStatusEnum) {
        return error(resultStatusEnum.getCode(), resultStatusEnum.getDescription());
    }

    public static R ok(ResultStatusEnum resultStatusEnum) {
        return ok(resultStatusEnum.getCode(), resultStatusEnum.getDescription());
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("msg", msg);
        return r;
    }

    public static R ok(Map<String, Object> map) {
        R r = new R();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return new R();
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}