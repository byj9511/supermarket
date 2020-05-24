package com.byy.common.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {
    //ListValue这个注解对应的数据效验器
    private HashSet<Integer> listValue=new HashSet();

    @Override
    public void initialize(ListValue constraintAnnotation) {
        //初始化：获取到请求体中的数据（标注了ListValue的成员变量的值），添加到容器当中
        Arrays.stream(constraintAnnotation.listValue()).forEach(listValue::add);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        //真正进行数据效验的地方
        return listValue.contains(value);
    }
}
