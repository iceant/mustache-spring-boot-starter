package com.pointcx.mustache.spring.boot.starter.detail.fetchers;


import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {{#i18n}}code.key(param1, param2, ... paramn){{/i18n}}
 */
public class I18NVariableFetcher implements Mustache.VariableFetcher {

    private final MessageSource messageSource;

    public I18NVariableFetcher(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private static final Pattern KEY_PATTERN = Pattern.compile("(.*?)[\\s\\(]");
    private static final Pattern ARGS_PATTERN = Pattern.compile("\\(([^\\(\\)]*?)\\)");

    @Override
    public Object get(Object ctx, String name) throws Exception {
        return new Mustache.Lambda() {
            @Override
            public void execute(Template.Fragment frag, Writer writer) throws IOException {
                String express = frag.execute();
                String key = null;
                String args = null;
                List<String> arguments = new ArrayList<String>();

                Matcher keyMatcher = KEY_PATTERN.matcher(express);
                if (keyMatcher.find()) {
                    key = keyMatcher.group(1);
                }
                if(StringUtils.isEmpty(key)){
                    key = express;
                }else {
                    Matcher argsMatcher = ARGS_PATTERN.matcher(express);
                    if (argsMatcher.find()) {
                        args = argsMatcher.group(1);
                    }

                    if (args != null) {
                        StringTokenizer st = new StringTokenizer(args, ",");
                        for (; st.hasMoreTokens(); ) {
                            String token = st.nextToken();
                            arguments.add(token.trim());
                        }
                    }
                }

                final String text = messageSource.getMessage(key, arguments.toArray(), LocaleContextHolder.getLocale());
                writer.write(text);
            }
        };
    }
}
