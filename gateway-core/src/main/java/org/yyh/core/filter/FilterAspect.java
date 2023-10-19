package org.yyh.core.filter;

import java.lang.annotation.*;

/**
 * @PROJECT_NAME: api-gateway
 * @DESCRIPTION: 过滤器注解类
 * @USER: yyh
 * @DATE: 2022/3/11 22:42
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FilterAspect {
    /**
     * 过滤器ID
     * @return
     */
    String id();

    /**
     * 过滤器名称
     * @return
     */
    String name() default "";

    /**
     * 排序
     * @return
     */
    int order() default 0;

}
