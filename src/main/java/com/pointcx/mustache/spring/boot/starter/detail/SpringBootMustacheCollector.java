package com.pointcx.mustache.spring.boot.starter.detail;

import com.samskivert.mustache.Mustache;
import org.springframework.boot.autoconfigure.mustache.MustacheEnvironmentCollector;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;

public class SpringBootMustacheCollector extends MustacheEnvironmentCollector implements EnvironmentAware {

    private final ApplicationContext applicationContext;

    public SpringBootMustacheCollector(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Mustache.VariableFetcher createFetcher(Object ctx, String name) {
        Mustache.VariableFetcher fetcher ;
        if(applicationContext!=null) {
            SpringVariableFetcherBuilder springVariableFetcherBuilder = applicationContext.getBean(SpringVariableFetcherBuilder.class);
            if (springVariableFetcherBuilder == null) return null;
            fetcher = springVariableFetcherBuilder.build(this, ctx, name);
            if (fetcher != null) return fetcher;
        }
        fetcher = super.createFetcher(ctx, name);
        if(fetcher!=null) return fetcher;

        return null;
    }
}
