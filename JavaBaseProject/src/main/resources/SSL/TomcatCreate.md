tomcat.keystore:
```bash
D:\Programe_files\java8\JDK\bin>keytool.exe -v -genkey -alias tomcat -keyalg RSA -keystore D:\tomcat.keystore -validity 36500
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
```


tomcat.cert:
```bash
D:\Programe_files\java8\JDK\bin>keytool.exe -export -keystore d:\tomcat.keystore
 -alias tomcat -file d:\tomcat.cert -rfc
输入密钥库口令:
存储在文件 <d:\tomcat.cert> 中的证书

Warning:
JKS 密钥库使用专用格式。建议使用 "keytool -importkeystore -srckeystore d:\tomcat
.keystore -destkeystore d:\tomcat.keystore -deststoretype pkcs12" 迁移到行业标准
格式 PKCS12。
```


tomcat_client.keystore
```bash
D:\Programe_files\java8\JDK\bin>keytool.exe -import -alias tomcat -file d:\tomca
t.cert -keystore d:\tomcat_client.keystore
输入密钥库口令:
再次输入新口令:
所有者: CN=localhost, OU=wkk, O=bj, L=bj, ST=bj, C=CN
发布者: CN=localhost, OU=wkk, O=bj, L=bj, ST=bj, C=CN
序列号: 6bd2f325
有效期为 Thu Jul 18 16:47:55 CST 2019 至 Sat Jun 24 16:47:55 CST 2119
证书指纹:
         MD5:  9B:BE:F4:31:79:3C:9E:1C:8F:3D:AF:0F:75:59:8E:E9
         SHA1: A8:F9:D1:01:4B:13:75:08:13:9D:87:D2:F6:78:D1:1D:F7:DE:70:55
         SHA256: DD:98:48:11:0B:E9:7E:F7:06:1E:6E:B1:07:02:D2:69:BC:87:FB:FF:F2:
CE:DB:93:70:E4:5B:40:30:7A:0D:6E
签名算法名称: SHA256withRSA
主体公共密钥算法: 2048 位 RSA 密钥
版本: 3

扩展:

#1: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 63 04 37 2E 64 42 47 7A   D6 36 06 63 77 D9 BD 88  c.7.dBGz.6.cw...
0010: 8E AB A3 F4                                        ....
]
]

是否信任此证书? [否]:  y
证书已添加到密钥库中
```