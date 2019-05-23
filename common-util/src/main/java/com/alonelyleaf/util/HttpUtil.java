package com.alonelyleaf.util;

import jodd.http.HttpRequest;
import jodd.util.StringPool;

import java.util.Map;

public class HttpUtil {

    public static String get(String url) {
        return get(url, 0);
    }

    public static String get(String url, String queryString) {
        return get(url, queryString, 0);
    }

    public static String get(String url, Map query) {
        return get(url, query, 0);
    }

    public static String get(String url, int milliseconds) {
        return getRequest(url, milliseconds).send().bodyText();
    }

    public static String get(String url, String queryString, int milliseconds) {
        return getRequest(url, queryString, milliseconds).send().bodyText();
    }

    public static String get(String url, Map query, int milliseconds) {
        return getRequest(url, query, milliseconds).send().bodyText();
    }

    public static String post(String url) {
        return post(url, 0);
    }

    public static String post(String url, Map query) {
        return post(url, query, 0);
    }

    public static String post(String url, String body) {
        return post(url, body, 0);
    }

    public static String post(String url, int milliseconds) {
        return postRequest(url, milliseconds).send().bodyText();
    }

    public static String post(String url, Map query, int milliseconds) {
        return postRequest(url, query, milliseconds).send().bodyText();
    }

    public static String post(String url, String body, int milliseconds) {
        return postRequest(url, body, milliseconds).send().bodyText();
    }

    //--------------------request-----------------------

    /**
     * get
     *
     * @param url
     * @param milliseconds 0 as infinite timeout
     * @return
     */
    public static HttpRequest getRequest(String url, int milliseconds) {
        return HttpRequest.get(url).charset(StringPool.UTF_8).timeout(milliseconds);
    }

    public static HttpRequest getRequest(String url, Map query, int milliseconds) {
        return getRequest(url, milliseconds).query(query);
    }

    public static HttpRequest getRequest(String url, String queryString, int milliseconds) {
        return getRequest(url, milliseconds).queryString(queryString);
    }

    /**
     * post
     *
     * @param url
     * @param milliseconds 0 as infinite timeout
     * @return
     */
    public static HttpRequest postRequest(String url, int milliseconds) {
        return HttpRequest.post(url).charset(StringPool.UTF_8).timeout(milliseconds);
    }

    public static HttpRequest postRequest(String url, Map form, int milliseconds) {
        return postRequest(url, milliseconds).form(form);
    }

    public static HttpRequest postRequest(String url, String body, int milliseconds) {
        return postRequest(url, milliseconds).bodyText(body);
    }
}
