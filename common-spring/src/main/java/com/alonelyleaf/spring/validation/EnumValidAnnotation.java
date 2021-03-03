package com.alonelyleaf.spring.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 枚举校验注解
 *
 * @author bijl
 * @date 2021/3/3 下午4:00
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumValidator.class})
@Documented
public @interface EnumValidAnnotation {

    /**
     * 提示信息
     *
     * @return
     */
    String message() default "";

    /**
     * 是否允许空值
     *
     * @return
     */
    boolean allowNull() default true;

    /**
     * 分组
     *
     * @return
     */
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    /**
     * 目标类（枚举）
     *
     * @return
     */
    Class<?>[] target() default {};

}
