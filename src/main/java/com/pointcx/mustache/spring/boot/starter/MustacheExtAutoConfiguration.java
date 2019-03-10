package com.pointcx.mustache.spring.boot.starter;

import com.pointcx.mustache.spring.boot.starter.detail.MustacheBeanPostProcessor;
import com.pointcx.mustache.spring.boot.starter.detail.SpringBootMustacheCollector;
import com.pointcx.mustache.spring.boot.starter.detail.SpringVariableFetcherBuilder;
import com.pointcx.mustache.spring.boot.starter.detail.fetchers.I18NVariableFetcher;
import com.pointcx.mustache.spring.boot.starter.detail.fetchers.LayoutHeadVariableFetcher;
import com.pointcx.mustache.spring.boot.starter.detail.fetchers.LayoutScriptVariableFetcher;
import com.pointcx.mustache.spring.boot.starter.detail.fetchers.LayoutVariableFetcher;
import com.pointcx.mustache.spring.boot.starter.detail.fetchers.SpringExpressionVariableFetcher;
import com.samskivert.mustache.Mustache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mustache.MustacheProperties;
import org.springframework.boot.autoconfigure.mustache.MustacheResourceTemplateLoader;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.expression.spel.support.StandardEvaluationContext;

@Configuration
@ConditionalOnClass(Mustache.class)
@EnableConfigurationProperties({MustacheExtProperties.class, MustacheProperties.class})
public class MustacheExtAutoConfiguration {

    private final MustacheExtProperties properties;
    private final MustacheProperties mustache;
    private final Environment environment;
    private final ApplicationContext applicationContext;

    public MustacheExtAutoConfiguration(MustacheExtProperties properties,
                                        MustacheProperties mustache,
                                        Environment environment,
                                        ApplicationContext applicationContext) {
        this.properties = properties;
        this.mustache = mustache;
        this.environment = environment;
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnMissingBean(Mustache.TemplateLoader.class)
    public MustacheResourceTemplateLoader mustacheTemplateLoader() {
        MustacheResourceTemplateLoader loader = new MustacheResourceTemplateLoader(
                this.mustache.getPrefix(), this.mustache.getSuffix());
        loader.setCharset(this.mustache.getCharsetName());
        return loader;
    }

    @Bean
    @ConditionalOnMissingBean(Mustache.Compiler.class)
    public Mustache.Compiler compiler(Mustache.Collector collector, Mustache.TemplateLoader templateLoader){
        return Mustache.compiler().withCollector(collector).withLoader(templateLoader);
    }

    @Bean
    @ConditionalOnMissingBean(Mustache.Collector.class)
    public Mustache.Collector collector() {
        return new SpringBootMustacheCollector(applicationContext);
    }

    @Bean
    @ConditionalOnBean(Mustache.Compiler.class)
    @ConditionalOnMissingBean(MustacheBeanPostProcessor.class)
    public MustacheBeanPostProcessor mustacheBeanPostProcessor(){
        return new MustacheBeanPostProcessor(collector());
    }

    @Bean
    @ConditionalOnMissingBean(SpringVariableFetcherBuilder.class)
    public SpringVariableFetcherBuilder springVariableFetcherBuilder(){
        SpringVariableFetcherBuilder builder =  new SpringVariableFetcherBuilder(applicationContext);
        SpringVariableFetcherBuilder.register("i18n", new I18NVariableFetcher(applicationContext));
        SpringVariableFetcherBuilder.register("layout", new LayoutVariableFetcher(applicationContext, properties));
        SpringVariableFetcherBuilder.register("head", new LayoutHeadVariableFetcher(applicationContext, properties));
        SpringVariableFetcherBuilder.register("script", new LayoutScriptVariableFetcher());
        SpringVariableFetcherBuilder.register("exp:", new SpringExpressionVariableFetcher(applicationContext, "exp:"), new VariableFetcherDefinition.PrefixMatcher("exp:"));
        return builder;
    }

    @Bean
    @ConditionalOnMissingBean(StandardEvaluationContext.class)
    public StandardEvaluationContext standardEvaluationContext(){
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        return evaluationContext;
    }
}
