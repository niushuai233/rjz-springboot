package cc.niushuai.rjz.common.config;

import cc.niushuai.rjz.common.bean.R;
import cc.niushuai.rjz.common.enums.ResultStatusEnum;
import cc.niushuai.rjz.common.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class BizExceptionConfig {

    public BizExceptionConfig() {
    }

    @ExceptionHandler({BizException.class})
    public R handleBizExceptionConfig(BizException e) {
        return R.builder().statusCode(e.getCode()).message(e.getMessage()).build();
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    public R handlerNoFoundException(Exception e) {
        log.error(e.getMessage(), e);
        return R.error(ResultStatusEnum.HTTP_404);
    }

    @ExceptionHandler({Exception.class})
    public R handleException(Exception e) {
        log.error(e.getMessage(), e);
        return R.error();
    }
}