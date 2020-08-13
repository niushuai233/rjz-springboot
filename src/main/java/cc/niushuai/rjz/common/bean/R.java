package cc.niushuai.rjz.common.bean;

import cc.niushuai.rjz.common.enums.ResultStatusEnum;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class R extends HashMap<String, Object> {

    private static final long serialVersionUID = -1131681874214638255L;

    private int code;
    @Builder.Default
    private String msg = "success";

    public static R error() {
        return error(ResultStatusEnum.HTTP_500);
    }

    public static R error(String msg) {
        return error(ResultStatusEnum.HTTP_500.getCode(), msg);
    }

    public static R error(int code, String msg) {
        return R.builder().code(code).msg(msg).build();
    }

    public static R ok(int code, String msg) {
        return R.builder().code(code).msg(msg).build();
    }

    public static R error(ResultStatusEnum resultStatusEnum) {
        return error(resultStatusEnum.getCode(), resultStatusEnum.getDescription());
    }

    public static R ok(ResultStatusEnum resultStatusEnum) {
        return ok(resultStatusEnum.getCode(), resultStatusEnum.getDescription());
    }

    public static R ok(String msg) {
        return R.builder().msg(msg).build();
    }

    public static R ok(Map<String, Object> map) {
        R r = R.builder().build();
        r.putAll(map);
        return r;
    }

    public static R ok() {
        return R.builder().build();
    }

    @Override
    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}