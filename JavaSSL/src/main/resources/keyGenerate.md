密钥生成方式:
**server.key:  服务器密钥**
注意：由于后面要使用wireshark对https的包进行解密操作，故这里指定加密算法为RSA，
不然wireshark对私钥不支持

PS D:\test> keytool -genkeypair -keystore server.key -alias sslserver -storepass 123456 -keyalg RSA -keypass 123456
您的名字与姓氏是什么?
  [Unknown]:  wk
您的组织单位名称是什么?
  [Unknown]:  er
您的组织名称是什么?
  [Unknown]:  er
您所在的城市或区域名称是什么?
  [Unknown]:  gd
您所在的省/市/自治区名称是什么?
  [Unknown]:  gd
该单位的双字母国家/地区代码是什么?
  [Unknown]:  CN
CN=wk, OU=er, O=er, L=gd, ST=gd, C=CN是否正确?
  [否]:  y
Warning:
JKS 密钥库使用专用格式。建议使用 "keytool -importkeystore -srckeystore sslserver.key 
-destkeystore sslserver.key -deststoretype pkcs12" 迁移到行业标准格式 PKCS12。 
   
**client.jks: 客户端密钥**
PS D:\test> keytool -genkeypair -keystore client.key -alias sslclient -storepass 123456 -keyalg RSA -keypass 123456
您的名字与姓氏是什么?
  [Unknown]:  wk
您的组织单位名称是什么?
  [Unknown]:  er
您的组织名称是什么?
  [Unknown]:  er
您所在的城市或区域名称是什么?
  [Unknown]:  gd
您所在的省/市/自治区名称是什么?
  [Unknown]:  gd
该单位的双字母国家/地区代码是什么?
  [Unknown]:  CN
CN=wk, OU=er, O=er, L=gd, ST=gd, C=CN是否正确?
  [否]:  y


Warning:
JKS 密钥库使用专用格式。建议使用 "keytool -importkeystore 
-srckeystore client.key -destkeystore client.key 
-deststoretype pkcs12" 迁移到行业标准格式 PKCS12。

**client1 certificate**
$ keytool.exe -genkeypair -keystore client1.key -alias sslclient1 -storepass 123456 -keyalg RSA -keypass 123456
您的名字与姓氏是什么?
[Unknown]:  jason
您的组织单位名称是什么?
[Unknown]:  loongson
您的组织名称是什么?
[Unknown]:  loongson
您所在的城市或区域名称是什么?
[Unknown]:  gz
您所在的省/市/自治区名称是什么?
[Unknown]:  gd
该单位的双字母国家/地区代码是什么?
[Unknown]:  CN
CN=jason, OU=loongson, O=loongson, L=gz, ST=gd, C=CN是否正确?
[否]:  y

**server.cer:  服务端证书**
PS D:\test> keytool -exportcert -alias sslserver -keystore server.key -storepass 123456 -file server.cer
存储在文件 <server.cer> 中的证书

Warning:
JKS 密钥库使用专用格式。建议使用 "keytool -importkeystore -srckeystore server.key -destkeystore server.key -deststoretype pkcs12" 迁移到行业标准格式 PKCS12。


**client.cer: 客户端证书**
PS D:\test> keytool -exportcert -alias sslclient -keystore .\client.key -storepass 123456 -file client.cer
存储在文件 <client.cer> 中的证书


**将服务器证书导入到客户端的trustKeystore中**
PS D:\test> keytool -importcert  -alias sslserver -keystore clienttrust -storepass 123456 -file .\server.cer
所有者: CN=wk, OU=er, O=er, L=gd, ST=gd, C=CN
发布者: CN=wk, OU=er, O=er, L=gd, ST=gd, C=CN
序列号: 6f52b530
有效期为 Sun Sep 01 17:05:47 CST 2019 至 Sat Nov 30 17:05:47 CST 2019
证书指纹:
         MD5:  73:B8:E1:74:02:47:DD:33:1E:0C:F2:25:C7:15:B0:E5
         SHA1: 91:5B:C7:06:6C:F5:4B:DC:2E:5E:74:C0:FB:A2:B3:E7:26:CE:C5:49
         SHA256: D4:5F:A3:F1:58:D3:0F:9D:27:CD:D1:D8:BE:58:64:2D:16:17:56:C2:36:2C:F4:86:8E:7B:1D:B4:18:A0:E9:3F
签名算法名称: SHA256withRSA
主体公共密钥算法: 2048 位 RSA 密钥
版本: 3

扩展:

#1: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 08 76 45 9E DA 3A E0 33   2B FA 29 CE E2 CA A7 46  .vE..:.3+.)....F
0010: 11 B9 97 91                                        ....
]
]

是否信任此证书? [否]:  y
证书已添加到密钥库中

**将客户端证书导入到服务器trustKeystore中**
PS D:\test> keytool -importcert -alias sslclient -keystore servertrust -storepass 123456 -file .\client.cer
所有者: CN=wk, OU=er, O=er, L=gd, ST=gd, C=CN
发布者: CN=wk, OU=er, O=er, L=gd, ST=gd, C=CN
序列号: 5426565e
有效期为 Sun Sep 01 17:08:46 CST 2019 至 Sat Nov 30 17:08:46 CST 2019
证书指纹:
         MD5:  47:5F:22:83:8D:05:8A:46:9E:59:38:D3:5B:60:21:47
         SHA1: 4D:9C:57:A7:03:08:14:C3:B4:3C:17:64:EE:CF:7C:03:C7:B1:FC:FC
         SHA256: C2:0F:9F:59:59:E5:29:59:D9:DC:EA:A5:66:76:69:8D:20:F2:FD:73:F3:85:5C:16:F5:2C:B3:65:60:36:29:D2
签名算法名称: SHA256withRSA
主体公共密钥算法: 2048 位 RSA 密钥
版本: 3

扩展:

#1: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: 3C 05 64 C3 27 B7 A4 CA   50 4D 23 95 D6 52 37 EF  <.d.'...PM#..R7.
0010: BE BD 37 16                                        ..7.
]
]

是否信任此证书? [否]:  y
证书已添加到密钥库中

**查看trustKeystore中条目**
D:\test>keytool -list -v -keystore clientTrust.jks -storepass 123456
密钥库类型: JKS
密钥库提供方: SUN

您的密钥库包含 1 个条目

别名: mykey
创建日期: 2019-8-31
条目类型: trustedCertEntry

所有者: CN=wk, OU=wk, O=wk, L=GD, ST=GD, C=CN
发布者: CN=wk, OU=wk, O=wk, L=GD, ST=GD, C=CN
序列号: 48bda2be
有效期为 Sat Aug 31 22:26:26 CST 2019 至 Fri Nov 29 22:26:26 CST 2019
证书指纹:
         MD5:  24:D8:CB:9B:08:61:B9:18:0F:74:8D:6D:26:A9:C5:D0
         SHA1: 86:0C:7F:52:B2:0A:73:A5:60:E7:5F:8A:4E:9B:63:B9:C3:28:EF:2D
         SHA256: 6B:1F:DC:0D:96:4C:AC:9B:40:72:C4:ED:BC:24:1C:65:1F:37:42:CF:66:75:81:F1:D3:74:1B:13:10:A6:47:FA
签名算法名称: SHA256withRSA
主体公共密钥算法: 2048 位 RSA 密钥
版本: 3

扩展:

#1: ObjectId: 2.5.29.14 Criticality=false
SubjectKeyIdentifier [
KeyIdentifier [
0000: D8 50 90 01 97 50 D6 55   F1 E1 D0 85 F0 FD CE 1C  .P...P.U........
0010: CC 81 98 CE                                        ....
]
]

*******************************************
*******************************************

**转换为p12行业标准**
PS D:\test> keytool -importkeystore -srckeystore client.key -destkeystore client.pkcs12 -deststoretype pkcs12
正在将密钥库 client.key 导入到 client.pkcs12...
输入目标密钥库口令:
再次输入新口令:
输入源密钥库口令:
已成功导入别名 sslclient 的条目。
已完成导入命令: 1 个条目成功导入, 0 个条目失败或取消

PS D:\test> keytool -importkeystore -srckeystore .\server.key -destkeystore server.pkcs12 -deststoretype pkcs12
正在将密钥库 .\server.key 导入到 server.pkcs12...
输入目标密钥库口令:
再次输入新口令:
输入源密钥库口令:
已成功导入别名 sslserver 的条目。
已完成导入命令: 1 个条目成功导入, 0 个条目失败或取消