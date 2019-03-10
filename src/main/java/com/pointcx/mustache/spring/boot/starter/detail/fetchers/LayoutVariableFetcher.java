package com.pointcx.mustache.spring.boot.starter.detail.fetchers;

import com.pointcx.mustache.spring.boot.starter.MustacheExtProperties;
import com.pointcx.mustache.spring.boot.starter.detail.layout.Layout;
import com.pointcx.mustache.spring.boot.starter.detail.utils.MustacheContextHolder;
import com.samskivert.mustache.Mustache;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

public class LayoutVariableFetcher implements Mustache.VariableFetcher {

    private final ApplicationContext applicationContext;
    private final MustacheExtProperties mustacheExtProperties;

    public LayoutVariableFetcher(ApplicationContext applicationContext, MustacheExtProperties mustacheExtProperties) {
        this.applicationContext = applicationContext;
        this.mustacheExtProperties = mustacheExtProperties;
    }

    @Override
    public Object get(Object ctx, String name) throws Exception {
        Mustache.Compiler compiler = MustacheContextHolder.getCompiler();
        if (compiler == null) {
            throw new NoSuchBeanDefinitionException(Mustache.Compiler.class);
        }
        Layout layout = MustacheContextHolder.get(name);
        if (layout == null) {
            layout = new Layout(compiler, StringUtils.isEmpty(mustacheExtProperties.getLayout()) ? null : mustacheExtProperties.getLayout());
            MustacheContextHolder.put(name, layout);
        }
        return layout;
    }
}
