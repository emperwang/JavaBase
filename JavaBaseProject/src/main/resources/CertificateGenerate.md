```bash
keyStore生成:
D:\Programe_files\java8\JDK\bin> keytool.exe -genkey -validity 360 -alias www.wk.org -keyalg RSA -keystore d:\wk.keystore
 
 输入密钥库口令:   123456
 再次输入新口令:
 您的名字与姓氏是什么?
 [Unknown]:  wk
 您的组织单位名称是什么?
 [Unknown]:  enper
 您的组织名称是什么?
 [Unknown]:  wkk
 您所在的城市或区域名称是什么?
 [Unknown]:  bj
 您所在的省/市/自治区名称是什么?
 [Unknown]:  bj
 该单位的双字母国家/地区代码是什么?
 [Unknown]:  CN
 CN=wk, OU=enper, O=wkk, L=bj, ST=bj, C=CN是否正确?
 [否]:  y

 输入 <www.wk.org> 的密钥口令
 (如果和密钥库口令相同, 按回车): 123456
 再次输入新口令:	123456
 
 -genkey 表示生成密钥
 -validity 指定证书有效期,这里是36000天
 -alias 指定别名,这里是www.zlex.org
 -keyalg 指定算法,这里是RSA
 -keystore 指定存储位置,这里是d:\wk.keystore
 
 
 证书生成:
 keytool -export -keystore d:\wk.keystore -alias www.wk.org -file d:\wk.cer -rfc 
 -export 指定为导出操作
 -keystore 指定keystore文件
 -alias 指定导出keystore文件中的别名
 -file 指向导出路径
 -rfc 以文本格式输出，也就是以BASE64编码输出
 
 这里的密码是 123456 
 
```
