package com.alonelyleaf.spring.template;

import com.alonelyleaf.spring.template.entity.Template;
import groovy.text.SimpleTemplateEngine;

import java.util.Map;

import static com.alonelyleaf.util.ValidateUtil.isEmpty;

/**
 * @author bijl
 * @date 2020/2/20
 */
public class TemplateUtils {

    private static final String CONTENT = "CONTENT";

    private static final String SUBJECT = "SUBJECT";

    /**
     * 根据模板与参数生成具体内容
     *
     * @param code
     * @param language
     * @param params
     * @return
     */
    private String generate(String type, String code, String language, String receiverType, Map params) {

        Template template = get(code, language, receiverType);

        if (isEmpty(template)) {
            return null;
        }

        if (CONTENT.equals(type)) {

            String script = isEmpty(template.getScript()) ? "" : template.getScript();

            return dynamicTemplate(script + template.getContent(), params);
        }

        return dynamicTemplate(template.getSubject(), params);
    }

    public static String dynamicTemplate(String content, Map params) {

        try {
            return new SimpleTemplateEngine().createTemplate(content).make(params).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取邮件模板
     *
     * @param code
     * @param language
     * @param receiverType
     * @return
     */
    private Template get(String code, String language, String receiverType){

        return new Template();
    }
}
