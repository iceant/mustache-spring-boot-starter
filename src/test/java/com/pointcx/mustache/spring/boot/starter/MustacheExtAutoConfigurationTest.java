package com.pointcx.mustache.spring.boot.starter;

import com.pointcx.mustache.spring.boot.starter.detail.SpringBootMustacheCollector;
import com.samskivert.mustache.Mustache;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.mustache.MustacheAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(classes = {MustacheAutoConfiguration.class,
        MustacheExtAutoConfiguration.class,
        MessageSourceAutoConfiguration.class
})
@RunWith(SpringRunner.class)
public class MustacheExtAutoConfigurationTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    public void testMustacheCollectorAssembly() {
        Mustache.Compiler compiler = applicationContext.getBean(Mustache.Compiler.class);
        Assert.assertTrue(compiler.collector instanceof SpringBootMustacheCollector);
    }

    @Test
    public void testI18n() {
        Mustache.Compiler compiler = applicationContext.getBean(Mustache.Compiler.class);

        String result = compiler.compile("{{#layout}}'Hello #{{name}}' {{#head}}head is here{{/head}} {{#script}} script value is here{{/script}} {{#i18n}}app.name({{name}}){{/i18n}} {{/layout}}").execute(new TestMustacheContext());
        System.out.println("result:" + result);
    }

    @Test
    public void testExp() {
        Mustache.Compiler compiler = applicationContext.getBean(Mustache.Compiler.class);

        //Create and set a calendar
        GregorianCalendar c = new GregorianCalendar();
        c.set(1856, 7, 9);

// The constructor arguments are name, birthday, and nationality.
        Inventor tesla = new Inventor("Nikola Tesla", c.getTime(), "Serbian");

        String result = compiler.compile("{{exp:'5.00' matches '^-?\\\\d+(\\\\.\\\\d{2})?$'}}").execute(tesla);
        System.out.println(result);
    }


    @Test
    public void testExpWithMap() {
        Mustache.Compiler compiler = applicationContext.getBean(Mustache.Compiler.class);
        Map<String, Object> map = new HashMap<>();
        map.put("name", "Chen Peng");
        String result = compiler.compile("{{exp:name}}").execute(map);
        System.out.println(result);
    }

    @Test
    public void testExpWithList(){
        for(String name: applicationContext.getBeanDefinitionNames()){
            System.out.println(name);
        }


        Mustache.Compiler compiler = applicationContext.getBean(Mustache.Compiler.class);
        List<Inventor> users = new ArrayList<>();
        users.add(new Inventor("Chen Peng", new Date(), "China"));
        users.add(new Inventor("Nikola Tesla", new Date(), "Serbian"));

        Map<String, Object> model = new HashMap<>();
        model.put("users", users);

        String result = compiler.compile("{{#users}}{{exp:.}}\n{{exp:name}}\n\n" +
                "{{exp:@mustacheCompiler.collector}}\n" +
                "{{/users}}")
                .execute(model);
        System.out.println(result);
    }

    @Test
    public void testDot(){
        Object data = new Object(){
            public Object inventor = new Inventor("Chen Peng", new Date(), "China");
        };
        Mustache.Compiler compiler = applicationContext.getBean(Mustache.Compiler.class);
        String result = compiler.compile("{{#inventor}}{{this.name}}{{/inventor}}").execute(data);
        System.out.println(result);
    }
}