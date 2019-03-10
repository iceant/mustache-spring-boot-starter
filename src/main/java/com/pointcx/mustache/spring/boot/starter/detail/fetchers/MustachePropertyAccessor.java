package com.pointcx.mustache.spring.boot.starter.detail.fetchers;

import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

public class MustachePropertyAccessor implements PropertyAccessor {
    private Object mustacheContext;

    public MustachePropertyAccessor(Object ctx) {
        this.mustacheContext = ctx;
    }

    @Override
    public Class<?>[] getSpecificTargetClasses() {
        return new Class[0];
    }

    @Override
    public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException {

        return false;
    }

    @Override
    public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException {
        return null;
    }

    @Override
    public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException {
        return false;
    }

    @Override
    public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException {

    }
}
