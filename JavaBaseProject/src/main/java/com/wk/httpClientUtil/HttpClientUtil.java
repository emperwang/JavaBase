package com.wk.httpClientUtil;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpClientUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    /**
     *  get请求，参数会在httpConfig中配置
     * @param httpConfig 配置信息
     */
    public static String httpGetMethod(HttpConfig httpConfig){
        HttpRequestBase getMethod = null;
        getMethod = getRequestByParam(httpConfig);
        configRequest(getMethod,httpConfig);
        CloseableHttpResponse response = null;
        try {
            response = (CloseableHttpResponse) httpConfig.getClient().execute(getMethod);
            String entityString = convertResponseToString(response);
            logger.info(entityString);
            return entityString;
        } catch (Exception e) {
            logger.error("Httpclient httpGetMethod error,the url is{},the error msg is{}",
                    httpConfig.getUrl(),e.getMessage());
            e.printStackTrace();
        }finally {
            close((CloseableHttpClient) httpConfig.getClient(),response);
        }
        return null;
    }
    public static Map<String,String> httpGetMethodWithStatusCode(HttpConfig httpConfig){
        HttpRequestBase getMethod = null;
        getMethod = getRequestByParam(httpConfig);
        configRequest(getMethod,httpConfig);
        CloseableHttpResponse response = null;
        Map<String, String> result = new HashMap<>();
        Integer statusCode = null;
        String entityString="";
        try {
            response = (CloseableHttpResponse) httpConfig.getClient().execute(getMethod);
            statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != null) {
                if (200 != statusCode && 201 != statusCode) {
                    entityString = convertResponseToString(response);
                }
            }
            logger.info("httpGetMethodWithStatusCode response is {}",result.toString());
            result.put("code",statusCode.toString());
            result.put("message",entityString);
            return result;
        } catch (Exception e) {
            logger.error("Httpclient httpGetMethodWithStatusCode error,the url is{},the error msg is{}",
                    httpConfig.getUrl(),e.getMessage());
            if (null != statusCode){
                result.put("code",statusCode.toString());
            }else{
                result.put("code","500");
            }
            if (!"".equals(entityString)){
                result.put("message",entityString);
            }else {
                result.put("message", e.getMessage());
            }
            return result;
        }finally {
            close((CloseableHttpClient) httpConfig.getClient(),response);
        }
    }

    /**
     *  http post 请求无参数
     * @param httpConfig 配置信息
     */
    public static String httpPostMethod(HttpConfig httpConfig){
        logger.debug("httpclient post method start,the url is{}",httpConfig.getUrl());
        CloseableHttpResponse response = null;
        HttpPost request = (HttpPost) getRequest(httpConfig.getUrl(),httpConfig.getMethods());
        try {
            configRequest(request,httpConfig);
            configParamEntity(request,httpConfig);
            response = (CloseableHttpResponse) httpConfig.getClient().execute(request);
            String entityString = convertResponseToString(response);
            logger.info(entityString);
            return entityString;
        } catch (IOException e) {
            logger.error("Httpclient httpPostMethod error,the url is{},the error msg is{}",
                    httpConfig.getUrl(),e.getMessage());
        }finally {
            close((CloseableHttpClient) httpConfig.getClient(),response);
        }
        return null;
    }

    public static Map<String,String> httpPostMethodWithStatusCode(HttpConfig httpConfig){
        logger.debug("httpclient post method start,the url is{}",httpConfig.getUrl());
        CloseableHttpResponse response = null;
        HttpPost request = (HttpPost) getRequest(httpConfig.getUrl(),httpConfig.getMethods());
        Map<String,String> result = new HashMap<>(2);
        Integer statusCode = null;
        String entityString = "";
        try {
            configRequest(request,httpConfig);
            configParamEntity(request,httpConfig);
            response = (CloseableHttpResponse) httpConfig.getClient().execute(request);
            statusCode = response.getStatusLine().getStatusCode();
            if (200 != statusCode && 201 != statusCode) {
                entityString = convertResponseToString(response);
            }
            result.put("code",statusCode.toString());
            result.put("message",entityString);
            logger.info("httpPostMethodWithStatusCode response is {}",result.toString());
            return result;
        } catch (IOException e) {
            logger.error("Httpclient httpPostMethodWithStatusCode error,the url is{},the error msg is{}",
                    httpConfig.getUrl(),e.getMessage());
            if (null != statusCode){
                result.put("code",statusCode.toString());
            }else{
                result.put("code","500");
            }
            if (!"".equals(entityString)){
                result.put("message", entityString);
            }else {
                result.put("message", e.getMessage());
            }
            return result;
        }finally {
            close((CloseableHttpClient) httpConfig.getClient(),response);
        }
    }

    /**
     *  delete 方法访问
     * @param httpConfig
     */
    public static String httpDeleteMethod(HttpConfig httpConfig){
        logger.debug("httpclient delete method start,the url is{}",httpConfig.getUrl());
        CloseableHttpResponse response = null;
        HttpDelete request = (HttpDelete) getRequest(httpConfig.getUrl(),httpConfig.getMethods());
        try {
            configRequest(request,httpConfig);
            response = (CloseableHttpResponse) httpConfig.getClient().execute(request);
            String entityString = convertResponseToString(response);
            logger.info("httpDeleteMethod response entity JSON is {}",entityString);
            return entityString;
        } catch (IOException e) {
            logger.error("Httpclient httpDeleteMethod error,the url is{},the error msg is{}",
                    httpConfig.getUrl(),e.getMessage());
            e.printStackTrace();
        }finally {
            close((CloseableHttpClient) httpConfig.getClient(),response);
        }
        return null;
    }

    public static Map<String,String> httpDeleteMethodWithStatusCode(HttpConfig httpConfig){
        logger.debug("httpclient delete method start,the url is{}",httpConfig.getUrl());
        CloseableHttpResponse response = null;
        HttpDelete request = (HttpDelete) getRequest(httpConfig.getUrl(),httpConfig.getMethods());
        Map<String,String> result = new HashMap<>(2);
        Integer statusCode = null;
        String entityString = "";
        try {
            configRequest(request,httpConfig);
            response = (CloseableHttpResponse) httpConfig.getClient().execute(request);
            statusCode = response.getStatusLine().getStatusCode();
            if (200 != statusCode && 201 != statusCode) {
                entityString = convertResponseToString(response);
            }
            result.put("code",statusCode.toString());
            result.put("message",entityString);
            logger.info("httpDeleteMethodWithStatusCode response is {}",result.toString());
            return result;
        } catch (IOException e) {
            logger.error("Httpclient httpDeleteMethodWithStatusCode error,the url is{},the error msg is{}",
                    httpConfig.getUrl(),e.getMessage());
            if (null != statusCode){
                result.put("code",statusCode.toString());
            }else{
                result.put("code","500");
            }
            if (!"".equals(entityString)){
                result.put("message", entityString);
            }else {
                result.put("message", e.getMessage());
            }
            return result;
        }finally {
            close((CloseableHttpClient) httpConfig.getClient(),response);
        }
    }

    public static Map<String,String> httpDeleteMethodWithEntityWithStatusCode(HttpConfig httpConfig){
        logger.debug("httpclient delete method start,the url is{}",httpConfig.getUrl());
        CloseableHttpResponse response = null;
        HttpDeleteWithEntity request = (HttpDeleteWithEntity) new HttpDeleteWithEntity(httpConfig.getUrl());
        Map<String,String> result = new HashMap<>(2);
        Integer statusCode = null;
        String entityString = "";
        try {
            configRequest(request,httpConfig);
            configParamEntity(request,httpConfig);
            response = (CloseableHttpResponse) httpConfig.getClient().execute(request);
            statusCode = response.getStatusLine().getStatusCode();
            if (200 != statusCode && 201 != statusCode) {
                entityString = convertResponseToString(response);
            }
            result.put("code",statusCode.toString());
            result.put("message",entityString);
            logger.info("httpDeleteMethodWithStatusCode response is {}",result.toString());
            return result;
        } catch (IOException e) {
            logger.error("Httpclient httpDeleteMethodWithStatusCode error,the url is{},the error msg is{}",
                    httpConfig.getUrl(),e.getMessage());
            if (null != statusCode){
                result.put("code",statusCode.toString());
            }else{
                result.put("code","500");
            }
            if (!"".equals(entityString)){
                result.put("message", entityString);
            }else {
                result.put("message", e.getMessage());
            }
            return result;
        }finally {
            close((CloseableHttpClient) httpConfig.getClient(),response);
        }
    }

    /**
     *  把responseEntity结果转换为string
     * @return
     */
    private static String convertResponseToString(HttpResponse response) throws IOException {
        return  EntityUtils.toString(response.getEntity());
    }

    /**
     *  put 方法
     * @param httpConfig
     */
    public static String  httpPutMethod(HttpConfig httpConfig){
        logger.debug("httpclient post method start,the url is{}",httpConfig.getUrl());
        CloseableHttpResponse response = null;
        HttpPut request = (HttpPut) getRequest(httpConfig.getUrl(),httpConfig.getMethods());
        try {
            configRequest(request,httpConfig);
            configParamEntity(request,httpConfig);
            response = (CloseableHttpResponse) httpConfig.getClient().execute(request);
            String entityString = convertResponseToString(response);
            logger.info(entityString);
            return entityString;
        } catch (IOException e) {
            logger.error("Httpclient httpPutMethod error,the url is{},the error msg is{}",
                    httpConfig.getUrl(),e.getMessage());
            e.printStackTrace();
        }finally {
            close((CloseableHttpClient) httpConfig.getClient(),response);
        }
        return null;
    }

    /**
     *  返回 响应码  响应体
     * @param httpConfig
     * @return
     */
    public static Map<String,String>  httpPutMethodWithStatusCode(HttpConfig httpConfig){
        logger.debug("httpclient put method start,the url is{}",httpConfig.getUrl());
        CloseableHttpResponse response = null;
        HttpPut request = (HttpPut) getRequest(httpConfig.getUrl(),httpConfig.getMethods());
        Map<String,String> result = new HashMap<>(2);
        Integer statusCode = null;
        String entityString = "";
        try {
            configRequest(request,httpConfig);
            configParamEntity(request,httpConfig);
            response = (CloseableHttpResponse) httpConfig.getClient().execute(request);
            statusCode = response.getStatusLine().getStatusCode();
            logger.info("httpPutMethodWithStatusCode statusCode is {}",statusCode);
            if (200 != statusCode && 201 != statusCode) {
                entityString = convertResponseToString(response);
            }
            result.put("code",statusCode.toString());
            result.put("message",entityString);
            if (logger.isDebugEnabled()){
                logger.debug("httpPutMethodWithStatusCode response:{}",result.toString());
            }
            return result;
        } catch (Exception e) {
            logger.error("Httpclient httpPutMethodWithStatusCode error,the url is{},the error msg is{}",
                    httpConfig.getUrl(),e.getMessage());
            if (null != statusCode){
                result.put("code",statusCode.toString());
            }else{
                result.put("code","500");
            }
            if (!"".equals(entityString)){
                result.put("message", entityString);
            }else {
                result.put("message", e.getMessage());
            }
            return result;
        }finally {
            close((CloseableHttpClient) httpConfig.getClient(),response);
        }
    }
    /**
     *  把参数准换为字符串
     * @param params
     * @return
     */
    private static String getParam(Map<String,String> params){
        StringBuilder paramBuilder = new StringBuilder();
        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry entry:entries){
            Object key = entry.getKey();
            Object value = entry.getValue();
            try {
                paramBuilder.append(key).append("=")
                        .append(URLEncoder.encode(value.toString(),"UTF-8"))
                        .append("&");
            } catch (UnsupportedEncodingException e) {
                logger.error("error method is getParam,the Exception method is{}",e.getMessage());
                e.printStackTrace();
            }
        }
        return paramBuilder.toString();
    }

    /**
     *  通过参数去获取不同的request
     * @param httpConfig
     * @return
     */
    private static HttpRequestBase getRequestByParam(HttpConfig httpConfig){
        HttpRequestBase requestBase = null;
        Map<String, String> paramMap = httpConfig.getParamMap();
        if (paramMap != null && paramMap.size() >0 ){
            String params = getParam(paramMap);
            if (httpConfig.getMethods().getName().toLowerCase().equals("delete")){
                requestBase = new HttpDelete(httpConfig.getUrl() + "?" + params);
            }else{
                requestBase = new HttpGet(httpConfig.getUrl()+"?"+params);
            }
        }else {
            requestBase = getRequest(httpConfig.getUrl(), httpConfig.getMethods());
        }

        return requestBase;
    }

    /**
     *  配置entity中的参数信息
     * @param request
     * @param httpConfig
     */
    public static void configParamEntity(HttpRequestBase request,HttpConfig httpConfig) throws UnsupportedEncodingException {
        Map<String, String> paramMap = httpConfig.getParamMap();
        StringEntity entity = null;
        if (paramMap != null && paramMap.size() > 0) {
             entity = new StringEntity(JSONUtil.beanToJson(paramMap), Charset.forName("UTF-8"));
             entity.setContentEncoding("UTF-8");
        }else{
            entity = new StringEntity(httpConfig.getBeanParam(), Charset.forName("UTF-8"));
            entity.setContentEncoding("UTF-8");
        }
        if (request instanceof HttpDeleteWithEntity){
            HttpDeleteWithEntity httpDelete = (HttpDeleteWithEntity) request;
            httpDelete.setEntity(entity);
            return;
        }
        if (request instanceof HttpPost){
            HttpPost httpPost = (HttpPost) request;
            httpPost.setEntity(entity);
            return;
        }
        if (request instanceof HttpPut){
            HttpPut httpPut = (HttpPut) request;
            httpPut.setEntity(entity);
            return;
        }
    }

    /**
     *  设置request的的相关信息，header 和 requestConfig
     * @param request 要配置的请求
     * @param httpConfig 配置存放的位置
     */
    private static void configRequest(HttpRequestBase request,HttpConfig httpConfig){
        Header[] headers = httpConfig.getHeaders();
        RequestConfig requestConfig = httpConfig.getRequestConfig();
        if (headers != null && headers.length >0){
            request.setHeaders(headers);
        }
        if (requestConfig != null){
            request.setConfig(requestConfig);
        }
    }

    /**
     *  关闭连接
     * @param httpClient
     * @param response
     */
    private static void close(CloseableHttpClient httpClient,CloseableHttpResponse response){
        if (httpClient != null){
            try {
                httpClient.close();
            } catch (IOException e) {
                logger.error("httpClient close exception,the method is{},the error msg is{}",
                        "close",e.getMessage());
                e.printStackTrace();
            }
        }
        if (response != null){
            try {
                response.close();
            } catch (IOException e) {
                logger.error("response close exception,the method is{},the error msg is{}",
                        "close",e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     *  获取对应的请求方式
     * @param url
     * @param methods
     * @return
     */
    private static HttpRequestBase getRequest(String url,HttpMethods methods){
        HttpRequestBase request = null;
        switch (methods.getNum()){
            case 1:
                request = new HttpGet(url);
                break;
            case 2:
                request = new HttpPost(url);
                break;
            case 3:
                request = new HttpDelete(url);
                break;
            case 4:
                request = new HttpPut(url);
                break;
            case 5:
                request = new HttpTrace(url);
                break;
            case 6:
                request = new HttpPatch(url);
                break;
            case 7:
                request = new HttpOptions(url);
                break;
            default:
                request = new HttpGet(url);
                break;
        }
        return request;
    }
}
