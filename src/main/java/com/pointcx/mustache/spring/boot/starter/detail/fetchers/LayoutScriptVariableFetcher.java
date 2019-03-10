package com.pointcx.mustache.spring.boot.starter.detail.fetchers;

import com.pointcx.mustache.spring.boot.starter.detail.layout.Layout;
import com.pointcx.mustache.spring.boot.starter.detail.utils.MustacheContextHolder;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.IOException;
import java.io.Writer;

public class LayoutScriptVariableFetcher implements Mustache.VariableFetcher {
    @Override
    public Object get(Object ctx, String name) throws Exception {
        return new Mustache.Lambda() {
            @Override
            public void execute(Template.Fragment frag, Writer out) throws IOException {
                Layout layout = MustacheContextHolder.get("layout");
                layout.script = frag.execute(MustacheContextHolder.context());
            }
        };
    }
}
