package com.internal.playment.common.inner;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;


import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: 陈俊雄
 * Date: 2018/5/30
 * Time: 14:34
 * Project: Management
 * Package: com.rxh.utils
 */
@Slf4j
public class HttpClientUtils {

    private static int TIMEOUT = 60;
    private static final String UTF_8 = "UTF-8";
    public static int getTIMEOUT() {
        return TIMEOUT;
    }

    public static void setTIMEOUT(int TIMEOUT) {
        HttpClientUtils.TIMEOUT = TIMEOUT;
    }


    /**
     * 获取HttpClient
     *
     * @return CloseableHttpClient
     */
    public static CloseableHttpClient getHttpClient() {
        return HttpClients.createDefault();
    }

    /**
     * 获取HttpsClient
     * 并设置TLS版本为1.2(JDK7默认1.0最高支持1.2)
     *
     * @return HttpsClient
     */
    public static CloseableHttpClient getHttpsClient() {
        SSLContext sslContext = null;
        try {
            sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (certificate, authType) -> true).build();
        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .build();
    }

    public static String doPost(CloseableHttpClient httpClient, String url, Map<String, ?> params) {
        return doPost(httpClient, url, params, null, UTF_8);
    }

    public static String doPost(CloseableHttpClient httpClient, String url, Map<String, ?> params, Map<String, String> hearders) {
        return doPost(httpClient, url, params, hearders, UTF_8);
    }

    /**
     * 执行Post请求
     *
     * @param httpClient httpClient
     * @param url        url
     * @param params     请求参数
     * @param encoding   编码
     * @return 请求结果
     */
    public static String doPost(CloseableHttpClient httpClient, String url, Map<String, ?> params, String encoding) {
        return doPost(httpClient, url, params, null, encoding);
    }

    /**
     * 执行Post请求
     *
     * @param httpClient httpClient
     * @param url        url
     * @param params     请求参数
     * @param headers    请求头信息
     * @param encoding   编码
     * @return 请求结果
     */
    public static String doPost(CloseableHttpClient httpClient, String url, Map<String, ?> params, Map<String, String> headers, String encoding) {
        HttpPost httpPost = new HttpPost(url);
        setEntity(params, encoding, httpPost);
        return send(httpClient, httpPost, headers, encoding);
    }

    public static String doPostJson(CloseableHttpClient httpClient, String url, String jsonString) {
        return doPostJson(httpClient, url, jsonString, UTF_8);
    }

    public static String doPostJson(CloseableHttpClient httpClient, String url, String jsonString, String encoding) {
        Map<String, String> headers = new HashMap<String, String>() {
            {
                put("Content-type", "application/json");
            }
        };
        return doPostJson(httpClient, url, jsonString, headers, encoding);
    }

    public static String doPostJson(CloseableHttpClient httpClient, String url, String jsonString, Map<String, String> headers, String encoding) {
        HttpPost httpPost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(jsonString, encoding);
        headers.forEach((k, v) -> stringEntity.setContentType(v));
        httpPost.setEntity(stringEntity);
        return send(httpClient, httpPost, headers, encoding);
    }

    /**
     * 设置请求参数
     *
     * @param params   请求参数
     * @param encoding 编码
     * @param method   Http请求
     */
    private static void setEntity(Map<String, ?> params, String encoding, HttpEntityEnclosingRequestBase method) {
        // 设置请求参数
        if (params != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<>();
            params.forEach((k, v) -> nameValuePairList.add(new BasicNameValuePair(k, String.valueOf(v))));
            try {
                method.setEntity(new UrlEncodedFormEntity(nameValuePairList, encoding));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 发送请求
     *
     * @param httpClient httpClient
     * @param request    request
     * @param headers    请求头
     * @param encoding   编码
     * @return 请求字符串结果
     */
    private static String send(CloseableHttpClient httpClient, HttpRequestBase request, Map<String, String> headers, String encoding) {
        // 设置请求头
        if (headers != null) {
            headers.forEach(request::setHeader);
        } else {
            Header header = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
            request.setHeader(header);
        }
        // 设置超时时间
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(TIMEOUT * 1000)
                .setConnectionRequestTimeout(TIMEOUT * 1000)
                .setSocketTimeout(TIMEOUT * 1000).build();
        request.setConfig(config);
        // 获取请求返回消息
        String result = null;
        try {
            // 执行请求
            log.info("RequestURI:{}" , request.getURI());
            CloseableHttpResponse response = httpClient.execute(request);
            if (response.getStatusLine().getStatusCode() != HttpServletResponse.SC_OK) {
                log.error("ResponseStatusCode:" + response.getStatusLine().getStatusCode());
            }
            // 获取请求返回消息
            result = EntityUtils.toString(response.getEntity(), UTF_8);
        } catch (Exception e) {
            log.error("ErrorMsg:" + e.getMessage() + ",ErrorURI:" + request.getURI());
        }
        // 关闭请求
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 返回请求参数
        return result;
    }
}