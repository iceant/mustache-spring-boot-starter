package com.pointcx.mustache.spring.boot.starter.detail.utils;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpringExpressUtil {

    public static <T> T eval(String express, EvaluationContext evaluationContext){
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(express);
        return (T) expression.getValue(evaluationContext);
    }

    public static <T>  T eval(String express, ParserContext parserContext, EvaluationContext evaluationContext, Object rootObject){
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(express, parserContext);
        return (T) expression.getValue(evaluationContext, rootObject);
    }

    public static StandardEvaluationContext createEvaluationContext(Object rootObject){
        StandardEvaluationContext ctx = new StandardEvaluationContext();
        ctx.setRootObject(rootObject);
        return ctx;
    }

}
