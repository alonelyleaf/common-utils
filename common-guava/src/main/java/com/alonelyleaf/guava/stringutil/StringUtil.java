package com.alonelyleaf.guava.stringutil;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bijl
 * @date 2019/10/11
 */
public class StringUtil {

    public String join(List<String> strings, String connector){

        if (null == strings || strings.size() == 0){
            return "";
        }

        if (null == connector){
            return Joiner.on("").skipNulls().join(strings);
        }

        return Joiner.on(connector).skipNulls().join(strings);
    }

    public List<String> split(String text, String separator){

        List<String> list = new ArrayList<>();

        if (null == text || "".equals(text)){
            return list;
        }

        if (null == separator || "".equals(separator)){
            list.add(text);
            return list;
        }

        Splitter splitter = Splitter.on(separator).omitEmptyStrings().trimResults();

        return (List<String>) splitter.split(text);
    }
}
