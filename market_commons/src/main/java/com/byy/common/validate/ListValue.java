package com.byy.common.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotEmpty;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
//自定义效验规则需要引入validation依赖
@Documented
@Constraint(validatedBy = {ListValueConstraintValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface ListValue {
    //将会加载ValidationMessages.properties（文件名不可改变）中com.byy.common.validate.ListValue.message（key）对应的value
    String message() default "{com.byy.common.validate.ListValue.message}";
    //可以填写数据效验组
    //Class<?>[]表示可以填写多个类（类的数组）
    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    int[] listValue() default {};
}
