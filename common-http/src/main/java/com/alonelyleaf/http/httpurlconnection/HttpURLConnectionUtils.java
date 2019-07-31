package com.alonelyleaf.http.httpurlconnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * java原生方式，不易用，正式项目中一般不直接使用
 *
 * ava.net包下的原生java api提供的http请求。
 *
 * 使用步骤：
 *
 * 1、通过统一资源定位器（java.net.URL）获取连接器（java.net.URLConnection）。
 *
 * 2、设置请求的参数。
 *
 * 3、发送请求。
 *
 * 4、以输入流的形式获取返回内容。
 *
 * 5、关闭输入流。
 *
 * @author bijl
 * @date 2019/7/5
 */
public class HttpURLConnectionUtils {

    private static Logger logger = LoggerFactory.getLogger(HttpURLConnectionUtils.class);

    //String urlPath = "https://www.baidu.com/";
    public void get(String urlPath) {

        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlPath);

            //得到connection对象。
            connection = (HttpURLConnection) url.openConnection();

            //设置请求方式
            connection.setRequestMethod("GET");
            //连接
            connection.connect();
            //得到响应码
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //得到响应流
                InputStream inputStream = connection.getInputStream();

                //将响应流转换成字符串

            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != connection){
                //关闭连接
                connection.disconnect();
            }
        }
    }
}
