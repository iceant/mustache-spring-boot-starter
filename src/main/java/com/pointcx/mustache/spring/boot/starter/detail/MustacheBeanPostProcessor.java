package com.pointcx.mustache.spring.boot.starter.detail;

import com.pointcx.mustache.spring.boot.starter.detail.utils.MustacheContextHolder;
import com.samskivert.mustache.Mustache;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MustacheBeanPostProcessor implements BeanPostProcessor {
    private final Mustache.Collector collector;

    public MustacheBeanPostProcessor(Mustache.Collector collector) {
        this.collector = collector;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof Mustache.Compiler){
            Mustache.Compiler compiler = (Mustache.Compiler) bean;
            if(!(compiler.collector instanceof SpringBootMustacheCollector)){
                compiler = compiler.withCollector(collector);
            }
            MustacheContextHolder.setCompiler(compiler);
            return compiler;
        }
        return bean;
    }
}
