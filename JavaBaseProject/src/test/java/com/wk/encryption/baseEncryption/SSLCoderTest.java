package com.wk.encryption.baseEncryption;

/**
 * D:\Programe_files\java8\JDK\bin>keytool.exe -v -genkey -alias tomcat -keyalg RSA -keystore D:\tomcat.keystore -validity 36500
 输入密钥库口令:
 再次输入新口令:
 您的名字与姓氏是什么?
 [Unknown]:  localhost
 您的组织单位名称是什么?
 [Unknown]:  wkk
 您的组织名称是什么?
 [Unknown]:  bj
 您所在的城市或区域名称是什么?
 [Unknown]:  bj
 您所在的省/市/自治区名称是什么?
 [Unknown]:  bj
 该单位的双字母国家/地区代码是什么?
 [Unknown]:  CN
 CN=localhost, OU=wkk, O=bj, L=bj, ST=bj, C=CN是否正确?
 [否]:  y

 正在为以下对象生成 2,048 位RSA密钥对和自签名证书 (SHA256withRSA) (有效期为 36,50
 0 天):
 CN=localhost, OU=wkk, O=bj, L=bj, ST=bj, C=CN
 输入 <tomcat> 的密钥口令
 (如果和密钥库口令相同, 按回车):
 [正在存储D:\tomcat.keystore]

 Warning:
 JKS 密钥库使用专用格式。建议使用 "keytool -importkeystore -srckeystore D:\tomcat
 .keystore -destkeystore D:\tomcat.keystore -deststoretype pkcs12" 迁移到行业标准
 格式 PKCS12。
 */

import com.wk.asm.aop1.Student;
import org.junit.Test;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;

/**
 *  启动tomcat8.5的https访问
 *  测试SSL下的https访问
 */
public class SSLCoderTest {

    public String password = "123456";
    public String alias = "tomcat";
    public String certificatepath = "d:/tomcat.cert";
    public String keyStorePath = "d:/tomcat.keystore";
    public String clientKeyStorepath = "d:/tomcat_client.keystore";
    public String clientPassword = "123456";

    @Test
    public void test()throws Exception{
        URL url = new URL("https://localhost:8443/");
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
        httpsURLConnection.setDoInput(true);
        httpsURLConnection.setDoOutput(true);

        SSLCoder.configSSLSocketFactory(httpsURLConnection,clientPassword,clientKeyStorepath,clientKeyStorepath);
        InputStream inputStream = httpsURLConnection.getInputStream();

        int length = httpsURLConnection.getContentLength();

        DataInputStream dis = new DataInputStream(inputStream);

        byte[] data = new byte[1024];
        dis.read(data);
        System.out.println(new String(data));
        httpsURLConnection.disconnect();
    }

    @Test
    public void tests(){
        String name = Student.class.getName();
        System.out.println(name.replace(".","/"));
    }
}
