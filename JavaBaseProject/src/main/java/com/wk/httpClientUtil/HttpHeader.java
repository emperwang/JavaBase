package com.wk.httpClientUtil;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.HashMap;
import java.util.Map;

public class HttpHeader {

    private HttpHeader(){}

    public static HttpHeader instance(){
        return new HttpHeader();
    }

    private Map<String,Header> headerMap = new HashMap<>();


    /**
     * 自定义header头信息
     *
     * @param key	header-key
     * @param value	header-value
     * @return 返回当前对象
     */
    public HttpHeader other(String key, String value) {
        headerMap.put(key, new BasicHeader(key, value));
        return this;
    }
    /**
     * 指定客户端能够接收的内容类型
     * 例如：Accept: text/plain, text/html
     *
     * @param accept accept
     * @return 返回当前对象
     */
    public HttpHeader accept(String accept) {
        headerMap.put(HttpRequestHead.ACCEPT,
                new BasicHeader(HttpRequestHead.ACCEPT, accept));
        return this;
    }

    /**
     * 浏览器可以接受的字符编码集
     * 例如：Accept-Charset: iso-8859-5
     *
     * @param acceptCharset accept-charset
     * @return 返回当前对象
     */
    public HttpHeader acceptCharset(String acceptCharset) {
        headerMap.put(HttpRequestHead.ACCEPT_CHARSET,
                new BasicHeader(HttpRequestHead.ACCEPT_CHARSET, acceptCharset));
        return this;
    }

    /**
     * 指定浏览器可以支持的web服务器返回内容压缩编码类型
     * 例如：Accept-Encoding: compress, gzip
     *
     * @param acceptEncoding accept-encoding
     * @return 返回当前对象
     */
    public HttpHeader acceptEncoding(String acceptEncoding) {
        headerMap.put(HttpRequestHead.ACCEPT_ENCODING,
                new BasicHeader(HttpRequestHead.ACCEPT_ENCODING, acceptEncoding));
        return this;
    }

    /**
     * 浏览器可接受的语言
     * 例如：Accept-Language: en,zh
     *
     * @param acceptLanguage accept-language
     * @return 返回当前对象
     */
    public HttpHeader acceptLanguage(String acceptLanguage) {
        headerMap.put(HttpRequestHead.ACCEPT_LANGUAGE,
                new BasicHeader(HttpRequestHead.ACCEPT_LANGUAGE, acceptLanguage));
        return this;
    }

    /**
     * 可以请求网页实体的一个或者多个子范围字段
     * 例如：Accept-Ranges: bytes
     *
     * @param acceptRanges accept-ranges
     * @return 返回当前对象
     */
    public HttpHeader acceptRanges(String acceptRanges) {
        headerMap.put(HttpRequestHead.ACCEPT_RANGES,
                new BasicHeader(HttpRequestHead.ACCEPT_RANGES, acceptRanges));
        return this;
    }

    /**
     * HTTP授权的授权证书
     * 例如：Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
     *
     * @param authorization authorization
     * @return 返回当前对象
     */
    public HttpHeader authorization(String authorization) {
        headerMap.put(HttpRequestHead.AUTHORIZATION,
                new BasicHeader(HttpRequestHead.AUTHORIZATION, authorization));
        return this;
    }

    /**
     * 指定请求和响应遵循的缓存机制
     * 例如：Cache-Control: no-cache
     *
     * @param cacheControl cache-control
     * @return 返回当前对象
     */
    public HttpHeader cacheControl(String cacheControl) {
        headerMap.put(HttpRequestHead.CACHE_CONTROL,
                new BasicHeader(HttpRequestHead.CACHE_CONTROL, cacheControl));
        return this;
    }

    /**
     * 表示是否需要持久连接（HTTP 1.1默认进行持久连接）
     * 例如：Connection: close 短链接； Connection: keep-alive 长连接
     *
     * @param connection connection
     * @return 返回当前对象
     */
    public HttpHeader connection(String connection) {
        headerMap.put(HttpRequestHead.CONNECTION,
                new BasicHeader(HttpRequestHead.CONNECTION, connection));
        return this;
    }

    /**
     * HTTP请求发送时，会把保存在该请求域名下的所有cookie值一起发送给web服务器
     * 例如：Cookie: $Version=1; Skin=new;
     *
     * @param cookie cookie
     * @return 返回当前对象
     */
    public HttpHeader cookie(String cookie) {
        headerMap.put(HttpRequestHead.COOKIE,
                new BasicHeader(HttpRequestHead.COOKIE, cookie));
        return this;
    }

    /**
     * 请求内容长度
     * 例如：Content-Length: 348
     *
     * @param contentLength content-length
     * @return 返回当前对象
     */
    public HttpHeader contentLength(String contentLength) {
        headerMap.put(HttpRequestHead.CONTENT_LENGTH,
                new BasicHeader(HttpRequestHead.CONTENT_LENGTH, contentLength));
        return this;
    }

    /**
     * 请求的与实体对应的MIME信息
     * 例如：Content-Type: application/x-www-form-urlencoded
     *
     * @param contentType content-type
     * @return 返回当前对象
     */
    public HttpHeader contentType(String contentType) {
        headerMap.put(HttpRequestHead.CONTENT_TYPE,
                new BasicHeader(HttpRequestHead.CONTENT_TYPE, contentType));
        return this;
    }
    /**
     * 请求发送的日期和时间
     * 例如：Date: Tue, 15 Nov 2010 08:12:31 GMT
     *
     * @param date	date
     * @return 返回当前对象
     */
    public HttpHeader date(String date) {
        headerMap.put(HttpRequestHead.DATE,
                new BasicHeader(HttpRequestHead.DATE, date));
        return this;
    }

    /**
     * 请求的特定的服务器行为
     * 例如：Expect: 100-continue
     *
     * @param expect expect
     * @return 返回当前对象
     */
    public HttpHeader expect(String expect) {
        headerMap.put(HttpRequestHead.EXPECT,
                new BasicHeader(HttpRequestHead.EXPECT, expect));
        return this;
    }

    /**
     * 发出请求的用户的Email
     * 例如：From: user@email.com
     *
     * @param from from
     * @return 返回当前对象
     */
    public HttpHeader from(String from) {
        headerMap.put(HttpRequestHead.FROM,
                new BasicHeader(HttpRequestHead.FROM, from));
        return this;
    }

    /**
     * 指定请求的服务器的域名和端口号
     * 例如：Host: blog.csdn.net
     *
     * @param host host
     * @return 返回当前对象
     */
    public HttpHeader host(String host) {
        headerMap.put(HttpRequestHead.HOST,
                new BasicHeader(HttpRequestHead.HOST, host));
        return this;
    }

    /**
     * 只有请求内容与实体相匹配才有效
     * 例如：If-Match: “737060cd8c284d8af7ad3082f209582d”
     *
     * @param ifMatch if-match
     * @return 返回当前对象
     */
    public HttpHeader ifMatch(String ifMatch) {
        headerMap.put(HttpRequestHead.IF_MATCH,
                new BasicHeader(HttpRequestHead.IF_MATCH, ifMatch));
        return this;
    }

    /**
     * 如果请求的部分在指定时间之后被修改则请求成功，未被修改则返回304代码
     * 例如：If-Modified-Since: Sat, 29 Oct 2010 19:43:31 GMT
     *
     * @param ifModifiedSince if-modified-Since
     * @return 返回当前对象
     */
    public HttpHeader ifModifiedSince(String ifModifiedSince) {
        headerMap.put(HttpRequestHead.IF_MODIFIED_SINCE,
                new BasicHeader(HttpRequestHead.IF_MODIFIED_SINCE, ifModifiedSince));
        return this;
    }

    /**
     * 如果内容未改变返回304代码，参数为服务器先前发送的Etag，与服务器回应的Etag比较判断是否改变
     * 例如：If-None-Match: “737060cd8c284d8af7ad3082f209582d”
     *
     * @param ifNoneMatch if-none-match
     * @return 返回当前对象
     */
    public HttpHeader ifNoneMatch(String ifNoneMatch) {
        headerMap.put(HttpRequestHead.IF_NONE_MATCH,
                new BasicHeader(HttpRequestHead.IF_NONE_MATCH, ifNoneMatch));
        return this;
    }

    /**
     * 如果实体未改变，服务器发送客户端丢失的部分，否则发送整个实体。参数也为Etag
     * 例如：If-Range: “737060cd8c284d8af7ad3082f209582d”
     *
     * @param ifRange if-range
     * @return 返回当前对象
     */
    public HttpHeader ifRange(String ifRange) {
        headerMap.put(HttpRequestHead.IF_RANGE,
                new BasicHeader(HttpRequestHead.IF_RANGE, ifRange));
        return this;
    }

    /**
     * 只在实体在指定时间之后未被修改才请求成功
     * 例如：If-Unmodified-Since: Sat, 29 Oct 2010 19:43:31 GMT
     *
     * @param ifUnmodifiedSince if-unmodified-since
     * @return 返回当前对象
     */
    public HttpHeader ifUnmodifiedSince(String ifUnmodifiedSince) {
        headerMap.put(HttpRequestHead.IF_UNMODIFIED_SINCE,
                new BasicHeader(HttpRequestHead.IF_UNMODIFIED_SINCE, ifUnmodifiedSince));
        return this;
    }

    /**
     * 限制信息通过代理和网关传送的时间
     * 例如：Max-Forwards: 10
     *
     * @param maxForwards max-forwards
     * @return 返回当前对象
     */
    public HttpHeader maxForwards(String maxForwards) {
        headerMap.put(HttpRequestHead.MAX_FORWARDS,
                new BasicHeader(HttpRequestHead.MAX_FORWARDS, maxForwards));
        return this;
    }

    /**
     * 用来包含实现特定的指令
     * 例如：Pragma: no-cache
     *
     * @param pragma pragma
     * @return 返回当前对象
     */
    public HttpHeader pragma(String pragma) {
        headerMap.put(HttpRequestHead.PRAGMA,
                new BasicHeader(HttpRequestHead.PRAGMA, pragma));
        return this;
    }

    /**
     * 连接到代理的授权证书
     * 例如：Proxy-Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
     *
     * @param proxyAuthorization proxy-authorization
     * @return 返回当前对象
     */
    public HttpHeader proxyAuthorization(String proxyAuthorization) {
        headerMap.put(HttpRequestHead.PROXY_AUTHORIZATION,
                new BasicHeader(HttpRequestHead.PROXY_AUTHORIZATION, proxyAuthorization));
        return this;
    }

    /**
     * 只请求实体的一部分，指定范围
     * 例如：Range: bytes=500-999
     *
     * @param range range
     * @return 返回当前对象
     */
    public HttpHeader range(String range) {
        headerMap.put(HttpRequestHead.RANGE,
                new BasicHeader(HttpRequestHead.RANGE, range));
        return this;
    }

    /**
     * 先前网页的地址，当前请求网页紧随其后,即来路
     * 例如：Referer: http://www.zcmhi.com/archives/71.html
     *
     * @param referer referer
     * @return 返回当前对象
     */
    public HttpHeader referer(String referer) {
        headerMap.put(HttpRequestHead.REFERER,
                new BasicHeader(HttpRequestHead.REFERER, referer));
        return this;
    }

    /**
     * 客户端愿意接受的传输编码，并通知服务器接受接受尾加头信息
     * 例如：TE: trailers,deflate;q=0.5
     *
     * @param te te
     * @return 返回当前对象
     */
    public HttpHeader te(String te) {
        headerMap.put(HttpRequestHead.TE,
                new BasicHeader(HttpRequestHead.TE, te));
        return this;
    }

    /**
     * 向服务器指定某种传输协议以便服务器进行转换（如果支持）
     * 例如：Upgrade: HTTP/2.0, SHTTP/1.3, IRC/6.9, RTA/x11
     *
     * @param upgrade upgrade
     * @return 返回当前对象
     */
    public HttpHeader upgrade(String upgrade) {
        headerMap.put(HttpRequestHead.UPGRADE,
                new BasicHeader(HttpRequestHead.UPGRADE, upgrade));
        return this;
    }

    /**
     * User-Agent的内容包含发出请求的用户信息
     *
     * @param userAgent user-agent
     * @return 返回当前对象
     */
    public HttpHeader userAgent(String userAgent) {
        headerMap.put(HttpRequestHead.USER_AGENT,
                new BasicHeader(HttpRequestHead.USER_AGENT, userAgent));
        return this;
    }

    /**
     * 关于消息实体的警告信息
     * 例如：Warn: 199 Miscellaneous warning
     *
     * @param warning warning
     * @return 返回当前对象
     */
    public HttpHeader warning(String warning) {
        headerMap.put(HttpRequestHead.WARNING,
                new BasicHeader(HttpRequestHead.WARNING, warning));
        return this;
    }

    /**
     * 通知中间网关或代理服务器地址，通信协议
     * 例如：Via: 1.0 fred, 1.1 nowhere.com (Apache/1.1)
     *
     * @param via via
     * @return 返回当前对象
     */
    public HttpHeader via(String via) {
        headerMap.put(HttpRequestHead.VIA,
                new BasicHeader(HttpRequestHead.VIA, via));
        return this;
    }

    /**
     * 设置此HTTP连接的持续时间（超时时间）
     * 例如：Keep-Alive: 300
     *
     * @param keepAlive keep-alive
     * @return 返回当前对象

     */
    public HttpHeader keepAlive(String keepAlive) {
        headerMap.put(HttpRequestHead.KEEP_ALIVE,
                new BasicHeader(HttpRequestHead.KEEP_ALIVE, keepAlive));
        return this;
    }

    public String accept() {
        return get(HttpRequestHead.ACCEPT);
    }

    public String acceptCharset() {
        return get(HttpRequestHead.ACCEPT_CHARSET);
    }

    public String acceptEncoding() {
        return get(HttpRequestHead.ACCEPT_ENCODING);
    }

    public String acceptLanguage() {
        return get(HttpRequestHead.ACCEPT_LANGUAGE);
    }

    public String acceptRanges() {
        return get(HttpRequestHead.ACCEPT_RANGES);
    }

    public String authorization() {
        return get(HttpRequestHead.AUTHORIZATION);
    }

    public String cacheControl() {
        return get(HttpRequestHead.CACHE_CONTROL);
    }

    public String connection() {
        return get(HttpRequestHead.CONNECTION);
    }

    public String cookie() {
        return get(HttpRequestHead.COOKIE);
    }

    public String contentLength() {
        return get(HttpRequestHead.CONTENT_LENGTH);
    }

    public String contentType() {
        return get(HttpRequestHead.CONTENT_TYPE);
    }

    public String date() {
        return get(HttpRequestHead.DATE);
    }

    public String expect() {
        return get(HttpRequestHead.EXPECT);
    }

    public String from() {
        return get(HttpRequestHead.FROM);
    }

    public String host() {
        return get(HttpRequestHead.HOST);
    }

    public String ifMatch() {
        return get(HttpRequestHead.IF_MATCH);
    }

    public String ifModifiedSince() {
        return get(HttpRequestHead.IF_MODIFIED_SINCE);
    }

    public String ifNoneMatch() {
        return get(HttpRequestHead.IF_NONE_MATCH);
    }

    public String ifRange() {
        return get(HttpRequestHead.IF_RANGE);
    }

    public String ifUnmodifiedSince() {
        return get(HttpRequestHead.IF_UNMODIFIED_SINCE);
    }

    public String maxForwards() {
        return get(HttpRequestHead.MAX_FORWARDS);
    }

    public String pragma() {
        return get(HttpRequestHead.PRAGMA);
    }

    public String proxyAuthorization() {
        return get(HttpRequestHead.PROXY_AUTHORIZATION);
    }

    public String referer() {
        return get(HttpRequestHead.REFERER);
    }

    public String te() {
        return get(HttpRequestHead.TE);
    }

    public String upgrade() {
        return get(HttpRequestHead.UPGRADE);
    }

    public String userAgent() {
        return get(HttpRequestHead.USER_AGENT);
    }

    public String via() {
        return get(HttpRequestHead.VIA);
    }

    public String warning() {
        return get(HttpRequestHead.WARNING);
    }

    public String keepAlive() {
        return get(HttpRequestHead.KEEP_ALIVE);
    }


    /**
     * 获取head信息
     *
     * @return
     */
    private String get(String headName) {
        if (headerMap.containsKey(headName)) {
            return headerMap.get(headName).getValue();
        }
        return null;
    }

    /**
     * 返回header头信息
     *
     * @return	返回构建的header头信息数组
     */
    public Header[] build() {
        Header[] headers = new Header[headerMap.size()];
        int i = 0;
        for (Header header : headerMap.values()) {
            headers[i] = header;
            i++;
        }
        headerMap.clear();
        headerMap = null;
        return headers;
    }
    /**
     *  常用头信息
     */
    private static class HttpRequestHead{
        public static final String ACCEPT = "accept";
        public static final String ACCEPT_CHARSET = "Accept-Charset";
        public static final String ACCEPT_ENCODING = "Accept-Encoding";
        public static final String ACCEPT_LANGUAGE = "Accept-Language";
        public static final String ACCEPT_RANGES = "Accept-Ranges";
        public static final String AUTHORIZATION = "Authorization";
        public static final String CACHE_CONTROL = "Cache-Control";
        public static final String CONNECTION = "Connection";
        public static final String COOKIE = "Cookie";
        public static final String CONTENT_LENGTH = "Content-Length";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String DATE = "Date";
        public static final String EXPECT = "Expect";
        public static final String FROM = "From";
        public static final String HOST = "Host";
        public static final String IF_MATCH = "If-Match";
        public static final String IF_MODIFIED_SINCE = "If-modified-Since";
        public static final String IF_NONE_MATCH = "If-None-Match";
        public static final String IF_RANGE = "If-Range";
        public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
        public static final String KEEP_ALIVE = "Keep-Alive";
        public static final String MAX_FORWARDS = "Max-Forwards";
        public static final String PRAGMA = "Pragma";
        public static final String PROXY_AUTHORIZATION = "Proxy-Authorization";
        public static final String RANGE = "Range";
        public static final String REFERER = "Referer";
        public static final String TE = "TE";
        public static final String UPGRADE = "Upgrade";
        public static final String USER_AGENT = "User-Agent";
        public static final String VIA = "Via";
        public static final String WARNING = "Warning";
    }

    /**
     *  常用头信息
     */
    public static class Headers{
        public static final String APP_FORM_URLENCODED = "application/x-www-form-urlencoded";
        public static final String TEXT_PLAIN = "text/plain";
        public static final String TEXT_HTML = "text/html";
        public static final String TEXT_XML = "text/xml";
        public static final String TEXT_JSON = "application/json";
        public static final String CONNECTION_CLOSE = "close";
        public static final String KEEP_ALIVE = "keep-alive";
        public static final String EXPECT_CONTINUE = "100-continue";

        public static final String CONTENT_CHARSET_ISO_8859 = Consts.ISO_8859_1.name();
        public static final String CONTENT_CHARSET_UTF8 = Consts.UTF_8.name();
        public static final String DEFAULT_PROTOCOL_CHARSET = Consts.ASCII.name();
    }
}
