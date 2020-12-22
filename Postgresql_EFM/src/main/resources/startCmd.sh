# 启动server端的命令
/usr/java/jdk1.8.0_211-amd64/jre/bin/java -cp /usr/efm-2.1/lib/EFM-2.1.2.jar
 -Xmx32m com.enterprisedb.efm.main.ServiceCommand __int_start /etc/efm-2.1/efm.properties

# efm 启动前命令
/usr/bin/sudo /usr/efm-2.1/bin/efm_root_functions writenodes efm "$$( cat /etc/efm-2.1/efm.nodes.default )"