package com.pointcx.mustache.spring.boot.starter;

import com.samskivert.mustache.Mustache;

import java.util.Objects;

public class VariableFetcherDefinition {

    public interface Matcher{
        boolean isMatch(Object ctx, String name);
    }

    private String name;
    private Mustache.VariableFetcher fetcher;
    private Matcher matcher;

    public VariableFetcherDefinition() {
    }

    public VariableFetcherDefinition(String name, Mustache.VariableFetcher fetcher) {
        this.name = name;
        this.fetcher = fetcher;
        this.matcher = new ExactlyMatcher(name);
    }

    public VariableFetcherDefinition(String name, Mustache.VariableFetcher fetcher, Matcher matcher) {
        this.name = name;
        this.fetcher = fetcher;
        this.matcher = matcher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Mustache.VariableFetcher getFetcher() {
        return fetcher;
    }

    public void setFetcher(Mustache.VariableFetcher fetcher) {
        this.fetcher = fetcher;
    }

    public Matcher getMatcher() {
        return matcher;
    }

    public void setMatcher(Matcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableFetcherDefinition that = (VariableFetcherDefinition) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public static class PrefixMatcher implements Matcher{

        final String prefix;

        public PrefixMatcher(String prefix) {
            this.prefix = prefix;
        }


        @Override
        public boolean isMatch(Object ctx, String name) {
            return name.startsWith(prefix);
        }
    }

    public static class ExactlyMatcher implements Matcher{

        private String name;

        public ExactlyMatcher(String name) {
            this.name = name;
        }

        @Override
        public boolean isMatch(Object ctx, String name) {
            return this.name.equals(name);
        }
    }
}
