package com.pointcx.mustache.spring.boot.starter.detail.layout;

import com.pointcx.mustache.spring.boot.starter.detail.utils.MustacheContextHolder;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.IOException;
import java.io.Writer;

public class Layout implements Mustache.Lambda {
    private final Mustache.Compiler compiler;
    private final String layout;

    public String body;
    public String head;
    public String script;


    public Layout(Mustache.Compiler compiler){
        this.compiler = compiler;
        this.layout = "layout";
    }

    public Layout(Mustache.Compiler compiler, String layout) {
        this.compiler = compiler;
        this.layout = ((layout==null)?"layout":layout);
    }


    @Override
    public void execute(Template.Fragment fragment, Writer writer) throws IOException {
        body = fragment.execute();
        compiler.compile(String.format("{{>%s}}", layout)).execute(MustacheContextHolder.context(), fragment.context(), writer);
    }

    @Override
    public String toString() {
        return "Layout@"+hashCode()+"{" +
                "compiler=" + compiler +
                ", layout='" + layout + '\'' +
                ", body='" + body + '\'' +
                ", head='" + head + '\'' +
                ", script='" + script + '\'' +
                '}';
    }
}
