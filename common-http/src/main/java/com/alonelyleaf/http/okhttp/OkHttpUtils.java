package com.alonelyleaf.http.okhttp;

import okhttp3.*;
import okio.BufferedSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Okhttp3基本使用
 * https://www.jianshu.com/p/da4a806e599b
 *
 * 彻底理解OkHttp - OkHttp 源码解析及OkHttp的设计思想
 * https://www.jianshu.com/p/cb444f49a777
 *
 * @author bijl
 * @date 2019/7/9
 */

public class OkHttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(OkHttpUtils.class);

    /**
     * 连接超时
     */
    private int timeout = 10;

    private static OkHttpClient client;

    private static final OkHttpUtils instance = new OkHttpUtils();

    public static OkHttpUtils getInstance() {
        return instance;
    }

    private OkHttpUtils() {

        initClient();
    }

    private void initClient() {

        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool()) //配置线程池
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(new LoggingIntercetor()) //配置拦截器
                .build();

    }

    /**
     * 同步GET请求
     */
    public Response get() {

        String url = "http://wwww.baidu.com";

        Request request = new Request.Builder()
                .url(url)
                .build();

        return get(request);
    }

    /**
     * 同步GET请求
     */
    public Response get(Request request) {

        Call call = client.newCall(request);

        try {
            Response response = call.execute();

            logger.info("run: " + response.body().string());

            return response;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 异步GET请求
     * <p>
     * 异步发起的请求会被加入到 Dispatcher 中的 runningAsyncCalls双端队列中通过线程池来执行。
     */
    public void asyncGet() {

        String url = "http://wwww.baidu.com";

        Request request = new Request.Builder()
                .url(url)
                .get()//默认就是GET请求，可以不写
                .build();


    }

    /**
     * 异步GET请求
     * <p>
     * 异步发起的请求会被加入到 Dispatcher 中的 runningAsyncCalls双端队列中通过线程池来执行。
     */
    public void asyncGet(Request request) {
        Call call = client.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logger.info("onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                logger.info("onResponse: " + response.body().string());
            }
        });
    }

    /**
     * 异步POST请求
     * <p>
     * 异步发起的请求会被加入到 Dispatcher 中的 runningAsyncCalls双端队列中通过线程池来执行。
     */
    public void asyncPost() {

        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody = "I am Jdqm.";
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .addHeader("Content-Type", "text/x-markdown; charset=utf-8")
                .post(RequestBody.create(mediaType, requestBody))
                .build();

        asyncPost(request);
    }

    /**
     * 异步POST请求
     * <p>
     * 异步发起的请求会被加入到 Dispatcher 中的 runningAsyncCalls双端队列中通过线程池来执行。
     */
    public void asyncPost(Request request) {

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                logger.info("onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                logger.info(response.protocol() + " " + response.code() + " " + response.message());
                Headers headers = response.headers();
                for (int i = 0; i < headers.size(); i++) {
                    logger.info(headers.name(i) + ":" + headers.value(i));
                }
                logger.info("onResponse: " + response.body().string());
            }
        });

    }

    /**
     * 同步POST请求
     */
    public Response post() {

        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody = "I am Jdqm.";
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .addHeader("Content-Type", "text/x-markdown; charset=utf-8")
                .post(RequestBody.create(mediaType, requestBody))
                .build();

        return post(request);
    }

    /**
     * 同步POST请求，提交流
     */
    public Response postStream() {

        RequestBody requestBody = new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return MediaType.parse("text/x-markdown; charset=utf-8");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("I am Jdqm.");
            }
        };

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .addHeader("Content-Type", "text/x-markdown; charset=utf-8")
                .post(requestBody)
                .build();

        return post(request);
    }

    /**
     * 同步POST请求，提交文件
     */
    public Response postFile() {

        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        File file = new File("test.md");

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .addHeader("Content-Type", "text/x-markdown; charset=utf-8")
                .post(RequestBody.create(mediaType, file))
                .build();

        return post(request);
    }

    /**
     * 同步POST请求，提交表单
     */
    public Response postForm() {

        RequestBody requestBody = new FormBody.Builder()
                .add("search", "Jurassic Park")
                .build();

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .addHeader("Content-Type", "text/x-markdown; charset=utf-8")
                .post(requestBody)
                .build();

        return post(request);
    }

    /**
     * 同步POST请求，提交分块请求
     * <p>
     * MultipartBody 可以构建复杂的请求体，与HTML文件上传形式兼容。多块请求体中每块请求都是一个请求体，可以定义自己的请求头。
     * 这些请求头可以用来描述这块请求，例如它的 Content-Disposition 。如果 Content-Length 和 Content-Type 可用的话，他们会被自动添加到请求头中。
     */
    public Response postMultipartBody() {

        MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

        // Use the imgur image upload API as documented at https://api.imgur.com/endpoints/image
        MultipartBody body = new MultipartBody.Builder("AaB03x")
                .setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"title\""),
                        RequestBody.create(null, "Square Logo"))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image\""),
                        RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                .build();

        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .addHeader("Content-Type", "text/x-markdown; charset=utf-8")
                .post(body)
                .build();

        return post(request);
    }

    /**
     * 同步POST请求
     */
    public Response post(Request request) {

        Call call = client.newCall(request);

        try {
            Response response = call.execute();

            logger.info("run: " + response.body().string());

            return response;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
