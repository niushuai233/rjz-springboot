package cc.niushuai.rjz.common.util.excel;

import java.lang.annotation.*;

/**
 * excel 字段注解
 * <pre>
 *      title 属性名与字段对应关系
 *      order 导入或导出列排序号
 * </pre>
 *
 * @author ns
 * @date 2019/12/7 11:11
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ExcelField {

    /**
     * Excel中表头字段
     */
    String title();

    /**
     * 是否别名优先
     */
    boolean enableAlias() default false;

    /**
     * 别名
     */
    String alias() default "";

    /**
     * 排序号
     */
    int order() default 0;
}
