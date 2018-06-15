Fork 自 GFW.Press魔改版, 并完善wiki。本折腾纯属闲的蛋疼，从使用上来讲，比原版没感觉到任何优势。如果你想对java构建jar包流程有基本的认识，那么跟我来吧！

环境

    操作系统: CentOS7
    版本：魔改版
    源码下载：https://github.com/uk0/gfw.press

安装OpenJDK

    搜索JDK版本（务必安装java 1.8版本）
    yum search java-1.8
    
    安装
    yum install -y java-1.8.0-openjdk-devel.x86_64

    验证目录文件
    cd /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.171-8.b10.el7_5.x86_64/lib  && ls
    
    amd64  ct.sym  dt.jar  ir.idl  jconsole.jar  jexec  orb.idl  sa-jdi.jar  tools.jar
    终于见到了久违的tool.jar 和dt.jar包。因为centOS 7.0自带的JDK压根就没有这些jar包，所以不管你环境变量配置正确与否，“javac”命令也不能用，因为这个命令依赖tool.jar这个jar包。

    配置环境变量
    vim /etc/profile
    加入：
    export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.171-8.b10.el7_5.x86_64
    export PATH=$JAVA_HOME/bin:$PATH
    export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
    
    生效
    source /etc/profile
    
    验证环境变量
    javac -version

安装配置maven

    maven官网：http://maven.apache.org/download.cgi
    
    wget http://mirror.bit.edu.cn/apache/maven/maven-3/3.5.3/binaries/apache-maven-3.5.3-bin.tar.gz
    tar zxvf apache-maven-3.5.3-bin.tar.gz
    mv apache-maven-3.5.3 /opt/

    配置环境变量
    vim /etc/profile
    export M2_HOME=/opt/apache-maven-3.5.3
    export PATH=$PATH:$M2_HOME/bin
    
    生效
    source /etc/profile
    
    验证
    mvn -version

安装客户端 UI 元素所依赖的类库

    unzip gfw.press-master.zip
    cp gfw.press-master/others/beautyeye_lnf.jar /root 
    cd /root/gfw.press-master   #务必在此目录安装jar包,此目录有pom.xml文件
    
    安装UI元素类库jar包
    mvn install:install-file -Dfile=/root/beautyeye_lnf.jar -DgroupId=org.jb2011.lnf.beautyeye -DartifactId=beautyeye_lnf -Dversion=3.7 -Dpackaging=jar
    
    成功后会输出BUILD SUCCESS字样

编辑user.txt密码文件

    编辑好user.txt密码文件，上传并替换/root/gfw.press-master目录下同名文件。

构建可执行jar包

    cd /root/gfw.press-master
    mvn assembly:assembly

    错误: [ERROR] Failed to execute goal org.apache.maven.plugins:maven-resources-plugin:2.6:resources (default-resources) on project gfw.press: Unable to parse configuration of mojo org.apache.maven.plugins:maven-resources-plugin:2.6:resources for parameter #: Cannot find default setter in class org.apache.maven.model.Resource -> [Help 1]
    
    原因：资源无法解析
    解决方法：手动下载maven-resources-plugin2.6.jar，手动安装
    官网插件：http://maven.apache.org/ref/3.3.9/plugin-management.html
    第三方下载：https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-resources-plugin/2.6
    
    cd /root/gfw.press-master
    mvn install:install-file -Dfile=/root/maven-resources-plugin-2.6.jar -DgroupId=org.apache.maven.plugins -DartifactId=maven-resources-plugin -Dversion=2.6 -Dpackaging=jar
    注：后边的安装信息对照/root/gfw.press-master/pom.xml配置
    
    然后,修改pom.xml文件,注释掉这个插件的目录约定【改成如下配置】
    <plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-resources-plugin</artifactId>
       <version>2.6</version>
       <configuration>
          <!--resources >src/main/resources/*</resources -->
          <encoding>UTF-8</encoding>
          <!-- 指定编码格式，否则在DOS下运行mvn>命令时当发生文件资源copy时将使用系统默认使用GBK编码 -->
       </configuration>
    </plugin>

    重新构建
    mvn assembly:assembly
    
    稍后片刻出现以下信息说明构建成功
    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 01:31 min
    [INFO] Finished at: 2018-06-13T03:34:47Z
    [INFO] ------------------------------------------------------------------------
    
    注：jar包文件存放位置：源码目录中的dist目录
    
    拷贝jar包到/opt目录
    mkdir -p /opt/gfw.press
    cp gfw.press-1.0.0-SNAPSHOT.jar /opt/gfw.press
    cp dist/log4j.properties /opt/gfw.press    

安装代理

    yum install squid -y 

    配置
    sed -i "s/shutdown_lifetime 3 seconds//g" /etc/squid/squid.conf 
    sed -i "s/access_log none//g" /etc/squid/squid.conf 
    sed -i "s/cache_log \/dev\/null//g" /etc/squid/squid.conf 
    sed -i "s/logfile_rotate 0//g" /etc/squid/squid.conf 
    sed -i "s/cache deny all//g" /etc/squid/squid.conf 
    echo "shutdown_lifetime 3 seconds" >> /etc/squid/squid.conf 
    echo "dns_nameservers 1.1.1.1 8.8.8.8" >> /etc/squid/squid.conf 
    echo "access_log none" >> /etc/squid/squid.conf 
    echo "cache_log /dev/null" >> /etc/squid/squid.conf 
    echo "logfile_rotate 0" >> /etc/squid/squid.conf 
    echo "cache deny all" >> /etc/squid/squid.conf 

    启动服务
    systemctl enable squid.service
    systemctl start squid.service 
    systemctl status squid.service 
    
    验证
    netstat -apn | grep 3128  
    tail -f  /var/log/squid/access.log

    配置防火墙
    iptables -F 
    iptables -A INPUT -p tcp --dport 22 -j ACCEPT 
    iptables -A INPUT -p tcp --dport 80 -j ACCEPT 
    iptables -A INPUT -p tcp --dport 443 -j ACCEPT 
    iptables -A INPUT -p tcp --dport 25 -j ACCEPT 
    iptables -A INPUT -p tcp --dport 110 -j ACCEPT 
    iptables -A INPUT -p tcp --dport 10000:10100 -j ACCEPT   //与user.txt指定端口保持一致
    iptables -P INPUT DROP 
    iptables -P FORWARD DROP 
    iptables -P OUTPUT ACCEPT 
    iptables -A INPUT -i lo -j ACCEPT 
    iptables -A INPUT -m state --state ESTABLISHED,RELATED -j ACCEPT 
    iptables -A INPUT -p icmp -j ACCEPT 
    /sbin/service iptables save 
    
    systemctl start iptables.service 
    systemctl enable iptables.service 

创建配置文件

    修改user.txt和server.json到/opt/gfw.press目录中（与可执行jar包放在同一个目录中）
    
    注1：cat /opt/gfw.press/server.json   //此文件内容如下： 
    {
    	"ProxyPort":"3128",
    	"ProxyHost":"127.0.0.1"
    }
    
    注2: user.txt 每行表示一个帐号，由 端口号+空格+密码组成，密码长度至少8位，必需包含大小写字母和数字。

运行服务

    服务器运行服务
    java -Dfile.encoding=utf-8 -Dsun.jnu.encoding=utf-8 -Duser.timezone=Asia/Shanghai -Xmn128M -Xms128M -Xmx256M -jar /opt/gfw.press/gfw.press-1.0.0-SNAPSHOT.jar server

    配置别名
    vim /etc/profile
    
    加入
    alias gfw="java -Dfile.encoding=utf-8 -Dsun.jnu.encoding=utf-8 -Duser.timezone=Asia/Shanghai -Xmn128M -Xms128M -Xmx256M -jar /opt/gfw.press/gfw.press-1.0.0-SNAPSHOT.jar"
    
    source /etc/profile

    使用
    gfw server  #启动服务端

相关脚本

    后台启动脚本
    
    #cat /etc/init.d/gfw.press.mg 
    
    #!/bin/bash
    DIR=/opt/gfw.press
    cd $DIR
    nohup /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.171-8.b10.el7_5.x86_64/bin/java -Dfile.encoding=utf-8 -Dsun.jnu.encoding=utf-8 -Duser.timezone=Asia/Shanghai -Xmn128M -Xms128M -Xmx256M -jar /opt/gfw.press/gfw.press-1.0.0-SNAPSHOT.jar server &

    进程检测自启脚本
    vim /etc/init.d/gfw_auto_start
    
    #!/bin/bash
    
    NUM=`netstat -lntup|grep 10009|wc -l`
    
    if [ $NUM -ne 1 ];then
       pkill java;cd /etc/init.d && ./gfw.press.mg
    fi
    
    增加执行权限
    chmod +x gfw_auto_start
    
    crontab -e
    */10 * * * * /etc/init.d/gfw_auto_start >>/dev/null 2>&1

客户端

    Windows 请访问 http://gfw.press/GFW.Press.msi 下载客户端安装包 配置填写完成，点击“确定”按钮即可使用。




