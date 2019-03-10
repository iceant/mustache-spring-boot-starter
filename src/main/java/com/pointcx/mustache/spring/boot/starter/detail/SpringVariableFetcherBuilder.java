package com.pointcx.mustache.spring.boot.starter.detail;

import com.pointcx.mustache.spring.boot.starter.VariableFetcherDefinition;
import com.samskivert.mustache.Mustache;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class SpringVariableFetcherBuilder{
    private final ApplicationContext applicationContext;
    private static final List<VariableFetcherDefinition> variableFetcherDefinitions = new ArrayList<>();

    public SpringVariableFetcherBuilder(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public static <T extends Mustache.VariableFetcher>
    void register(String name, Mustache.VariableFetcher fetcher){
        VariableFetcherDefinition definition = new VariableFetcherDefinition(name, fetcher);
        if(!variableFetcherDefinitions.contains(definition))
            variableFetcherDefinitions.add(definition);
    }

    public static <T extends Mustache.VariableFetcher>
    void register(String name, Mustache.VariableFetcher fetcher, VariableFetcherDefinition.Matcher matcher){
        VariableFetcherDefinition definition = new VariableFetcherDefinition(name, fetcher, matcher);
        if(!variableFetcherDefinitions.contains(definition))
            variableFetcherDefinitions.add(definition);
    }


    public static Mustache.VariableFetcher build(Mustache.Collector collector, Object ctx, String name){
        for(VariableFetcherDefinition variableFetcherDefinition: variableFetcherDefinitions){
            if(variableFetcherDefinition.getMatcher().isMatch(ctx, name)){
                return build(variableFetcherDefinition);
            }
        }
        return null;
    }

    private static Mustache.VariableFetcher build(VariableFetcherDefinition variableFetcherDefinition) {
        if(variableFetcherDefinition==null) return null;
        return variableFetcherDefinition.getFetcher();
    }

    public static boolean isAdoptable(Object mustacheContext, String name) {
        for(VariableFetcherDefinition variableFetcherDefinition: variableFetcherDefinitions){
            if(variableFetcherDefinition.getMatcher().isMatch(mustacheContext, name)){
                return true;
            }
        }
        return false;
    }


}
