> Fork 自 GFW.Press，自己魔改版本

## 一、环境准备

- **该项目采用 Java 编写，运行于 JDK1.8，由于内部采用高强度加密机制，而美国进出口软件法案限制
高强度加密软件出口，所以下载的 sun JDK 需要替换 JCE 加密库， JCE [下载地址](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)
，使用教程请自行 Google，Linux 下直接使用 OpneJDK 不受影响。**

- **项目正式切换到 Maven 构建，Maven 安装配置教程请自行 Google，下载一个压缩包解压开配置一下环境变量即可。**

- **由于本项目客户端 UI 元素所依赖的类库在 Maven 中央仓库中没有，所以需要将其安装到本地仓库，
Maven 安装完成后请 copy `gfw.pree/other/beautyeye_lnf.jar` 到任意位置，并执行 mvn_install_beautyeye
内的命令，出现 BUILD SUCCESS 表示成功。**

## 二、客户端

Windows 请访问 http://gfw.press/GFW.Press.msi 下载客户端安装包 配置填写完成，点击“确定”按钮即可使用。

由于 Jave 跨平台特性(我不信)，推荐使用 Maven 直接生成 可运行的 jar 包运行，生成方法直接 cd 到项目根目录
(与 pom.xml 平级)，执行 `mvn assembly:assembly` 将在 target 目录下生成可直接运行的 jar 包(前提是 JDK、
Maven 没问题)。

Linux/Windwos 图形化界面可直接双击 gfw.press.jar 运行图形化客户端，命令行下可使用 
`java -jar gfw.press_fat.jar client` 运行命令行客户端，默认客户端会加载同级目录下的 
**client.json** 配置文件用于连接服务器，以下为截图(我把命令写成了别名)

<img width="614" alt="server" src="https://cloud.githubusercontent.com/assets/13043245/15628329/f60b8a9a-2530-11e6-9d93-424f7ded2242.png">

<img width="585" alt="client" src="https://cloud.githubusercontent.com/assets/13043245/15628331/fcd97332-2530-11e6-9984-31a60b1fa375.png">

<img width="603" alt="help" src="https://cloud.githubusercontent.com/assets/13043245/15628337/2d647b50-2531-11e6-9682-07e8909e97c0.png">


## 三、服务器

### 第一步： 生成可执行 jar

使用 maven 生成 gfw.press.jar 并 copy 到任意目录

### 第二步：安装 JDK

``` sh
# CentOS
yum install java-1.8.0-openjdk.x86_64 -y ;
# Ubuntu
apt-get install software-properties-common
add-apt-repository ppa:openjdk-r/ppa
apt-get update
apt-get install openjdk-8-jdk
```

### 第三步：安装代理软件

**Ubuntu 系统直接运行项目根目录下的 3proxy.sh 即可，Centos 执行 `yum instsall 3proxy -y`，
配制文件请参考 3proxy.sh 中两个 `EOF` 标记之间的配置内容，后端也可以采用 squid，具体后端方法请自行 Google 摸索.....**

### 第四步：创建配置文件

在 gfw.press.jar 同级目录创建 user.txt(用户配置)、server.json(服务器监听配置)，两个配置文件项目中有样例，
**user.txt 每行表示一个帐号，由 端口号+空格+密码组成，密码长度至少8位，必需包含大小写字母和数字。**

### 第五步：运行

执行以下命令即可

``` sh
java -jar gfw.press.jar server
```

**关于日志乱码、服务器内存使用等可参考以下启动命令，每个参数的意义请自行 Google，在此不一一阐述。**

``` sh
java -Dfile.encoding=utf-8 -Dsun.jnu.encoding=utf-8 -Duser.timezone=Asia/Shanghai -Xmn128M -Xms128M -Xmx256M -jar gfw.press_fat.jar [server/client/online]
```

**gfw.press.jar 后面可选四个参数 server、client、online、gui(不写数默认执行此参数)，
建议将 gfw.press.jar 到其前面的命令设置别名，以下为样例**

``` sh
# ~/.bashrc
alias gfw="java -Dfile.encoding=utf-8 -Dsun.jnu.encoding=utf-8 -Duser.timezone=Asia/Shanghai -Xmn128M -Xms128M -Xmx256M -jar gfw.press.jar"
# 执行命令
gfw server # 启动服务端
gfw client # 启动命令行客户端
gfw gui    # 启动 GUI 客户端(默认)
gfw online # 统计在线人数
```

## 四、其他说明

- 2016-08-26 最新可执行 jar 已经打包好存放与 dist 目录，直接将此目录复制到任意位置运行 jar 即可

