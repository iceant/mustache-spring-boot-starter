package com.pointcx.mustache.spring.boot.starter.detail.fetchers;

import com.pointcx.mustache.spring.boot.starter.MustacheExtProperties;
import com.pointcx.mustache.spring.boot.starter.detail.layout.Layout;
import com.pointcx.mustache.spring.boot.starter.detail.utils.MustacheContextHolder;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.io.Writer;

public class LayoutHeadVariableFetcher implements Mustache.VariableFetcher {
    private final ApplicationContext applicationContext;
    private final MustacheExtProperties mustache;

    public LayoutHeadVariableFetcher(ApplicationContext applicationContext, MustacheExtProperties mustache) {
        this.applicationContext = applicationContext;
        this.mustache = mustache;
    }


    @Override
    public Object get(Object ctx /*not lambda*/, String name) throws Exception {
        return new Mustache.Lambda() {
            @Override
            public void execute(Template.Fragment frag, Writer out) throws IOException {
                Layout layout = MustacheContextHolder.get("layout");
                layout.head = frag.execute(MustacheContextHolder.context());
            }
        };
    }
}
