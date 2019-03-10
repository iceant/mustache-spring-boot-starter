package com.pointcx.mustache.spring.boot.starter.detail.fetchers;

import com.pointcx.mustache.spring.boot.starter.detail.utils.SpringExpressUtil;
import com.samskivert.mustache.Mustache;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanExpressionContextAccessor;
import org.springframework.context.expression.BeanFactoryAccessor;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.EnvironmentAccessor;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpringExpressionVariableFetcher implements Mustache.VariableFetcher {

    private final ApplicationContext applicationContext;
    private final String prefix;

    public SpringExpressionVariableFetcher(ApplicationContext applicationContext, String prefix) {
        this.applicationContext = applicationContext;
        this.prefix = prefix;
    }

    @Override
    public Object get(Object ctx, String name) throws Exception {
        String express = name.substring(prefix.length());

        // try mustach.VariableFetcher first
        Mustache.VariableFetcher fetcher=null;
        if (express.equals(".") || express.equals("this")) {
            fetcher = THIS_FETCHER;
        }
        if(fetcher!=null) {
            return fetcher.get(ctx, express);
        }

        // try spring
        StandardEvaluationContext evaluationContext = applicationContext.getBean(StandardEvaluationContext.class);
        evaluationContext = evaluationContext==null?new StandardEvaluationContext():evaluationContext;
        evaluationContext.setRootObject(ctx);
        evaluationContext.addPropertyAccessor(new MapAccessor());
        evaluationContext.addPropertyAccessor(new BeanExpressionContextAccessor());
        evaluationContext.addPropertyAccessor(new BeanFactoryAccessor());
        evaluationContext.addPropertyAccessor(new EnvironmentAccessor());
        evaluationContext.setBeanResolver(new BeanFactoryResolver(applicationContext.getAutowireCapableBeanFactory()));
        return SpringExpressUtil.eval(express, evaluationContext);
    }

    static final Mustache.VariableFetcher THIS_FETCHER = new Mustache.VariableFetcher() {
        public Object get (Object ctx, String name) throws Exception {
            return ctx;
        }
        @Override public String toString () {
            return "THIS_FETCHER";
        }
    };
}
