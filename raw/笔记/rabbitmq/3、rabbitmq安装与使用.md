### RabbitMQ的安装：
    1. 从官网下载 [RabbitMQ](http://wwww.rabbitmq.com) 安装包；
    注意：由于 RabbitMQ 是用 Erlang 语言编写的，所以我们需要下载对应的 ErLang 语言的包。
    具体的版本可以参考 [这里](https://www.rabbitmq.com/which-erlang.html)
    2. 安装 Linux 必要依赖包；
       1). 下载 rabbitmq 安装包
        wget https://github.com/rabbitmq/rabbitmq-server/releases/download/rabbitmq_v3_6_5/rabbitmq-server-3.6.5-1.noarch.rpm
       2). 下载 socat 包(socat做秘钥检查)
        wget http://repo.iotti.biz/CentOS/7/x86_64/socat-1.7.3.2-5.el7.lux.x86_64.rpm
       3). 下载 Erlang
        wget https://www.rabbitmq.com/releases/erlang/erlang-18.3-1.el7.centos.x86_64.rpm 
       4). 下载 tomcat
        wget  https://www-us.apache.org/dist/tomcat/tomcat-8/v8.5.39/bin/apache-tomcat-8.5.39.tar.gz
        
    3. 安装 RabbitMQ (注意顺序)
       1). 安装 Erlang
         rpm -ivh erlang-18.3-1.el7.centos.x86_64.rpm
       2). 安装 Socat
         rpm -ivh socat-1.7.3.2-5.el7.lux.x86_64.rpm
       3). 安装 rabbitmq
         rpm -ivh rabbitmq-server-3.6.5-1.noarch.rpm

    4. 修改配置文件
       vim /usr/lib/rabbitmq/lib/rabbitmq_server-3.6.5/ebin/rabbit.app
        找到  {loopback_users, [<<"guest">>]} 这一样，改成 {loopback_users, [guest]}.

### RabbitMQ 的使用
    1. 服务的启动
     rabbitmq-server start &  (& 表示后台启动)
    2. 服务的停止
     rabbitmqctl stop_app 
    3. 管理插件
     rabbitmq-plugins enable rabbitmq_management
    4. 访问
     5672 端口是 rabbitmq 启动的端口
     15672 是管控台访问的端口
     25672 是集群通信的端口
     http://127.0.0.1:15672
