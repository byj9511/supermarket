package com.byy.common.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;

public class ListValueConstraintValidator implements ConstraintValidator<ListValue,Integer> {
    private HashSet<Integer> listValue=new HashSet();

    @Override
    public void initialize(ListValue constraintAnnotation) {
        Arrays.stream(constraintAnnotation.listValue()).forEach(listValue::add);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return listValue.contains(value);
    }
}
