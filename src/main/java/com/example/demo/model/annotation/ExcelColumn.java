package com.example.demo.model.annotation;

import java.lang.annotation.*;

/**
 * @author zhouyw
 * @date 2021-12-28
 * @describe com.nesun.smart.statistic.model.annotation
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelColumn {

    /**
     * Excel标题
     *
     * @return
     * @author Lynch
     */
    String value() default "";

    /**
     * Excel从左往右排列位置
     *
     * @return
     * @author Lynch
     */
    int col() default 0;

}
