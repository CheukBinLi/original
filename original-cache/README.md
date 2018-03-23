## Redis多机集群


### Redis集群。网上很多教程，只是按着它的步骤来做只能在单机上跑，而已不有点抗。也不用密码验证
#### 开始：
````
1:redis集群最少需要要6个服务器端，因此先搞6台虚拟机 我用 centOS-7 mini 每台给最少内存
或者用docker 运行6个centos:latest,用--link 来互联即可。
  IP固定为:200、201、202、203、204、205、206
  Vi  /etc/sysconfig/network-scripts/ifcfg-XXX  里修改
一般只修改 
BOOTPROTO=static
ONBOOT=yes
IPADDR=192.168.1.200 #IP
NETMASK=255.255.255.0
GATEWAY=192.168.1.1 #网关
DNS1=192.168.1.1  #写路由地址

安装ruby、zlib、rubygems (一般安装ruby会同时安装其它两个组件)
注意：最好不要去下载 ruby.gz来安装。试过安装完后会出现找不到zlib的错误信息，
试了网上很多方法都不行。有兴趣折腾的可以玩玩，也可能是我手贱
Yum install –y ruby
下载ruby redis 接口(可以gem install redis 直接安装但速度奇慢)
自选择要下载的版本  https://rubygems.org/gems/redis
安装 gem install  redis-XX.gem   (redis-XX.gem上面网址下载的gem脚本)

下载安装redis (说起安装这东东，试过一次去面试被问这东东会安装吗？回答会。面试官很豪爽的叫，去给我安装来看看。
怎么不问这个在开发上用在什么地方？为什么要用？觉是被看成是去应聘做管理员的公司的名字还很
叼不公开了自己猜猜吧广州番禺的一家公司是奥园地产的子公司叫:O  MY GOD)
官方安装教程：先安装  yum install gcc
$ wget http://download.redis.io/releases/redis-3.2.1.tar.gz
$ tar xzf redis-3.2.1.tar.gz
$ cd redis-3.2.1
$ make
如果报错，make指定参数:   make MALLOC=libc 再跑
安装（可以省略）：make install 
运行:  src/redis-server &
客户端连接:  src/redis-cli
测试get/set 能过后输入   shutdown   关闭redis

配置redis
复制一份: cp redis.conf cluster.conf
修改 cluster.conf相关参数
port 2000
daemonize yes #后台服务必须的
cluster-enabled yes
cluster-config-file nodes.conf  #集群信息（自动生成）
appendonly yes
#bind 127.0.0.1 注掉
Protected-mode no #确保空密码能连接

用scp把redis复制到其它机器
scp redis-xx  root@192.168.1.x:/tmp/redis-xx
都复制完就把每一台的redis开起来
daemonize yes 这个参数配成YES后启动服务是没有提示启动是否正常的，你可以用客户端连接看看，也要看看端口是否以在监听
netstat –tln
如果工具没安装 yum install –y net-tools 安装
一切正常了
建立群集:
./src/redis-trib.rb create --replicas 1 192.168.1.200:2000  ….. 192.168.1.205:2000

--replicas 1   1表示一个主再多少个从机

中间要输入yes回车
完成。
用客户端连接随便一台试试
在客户端查看信息:  
CLUSTER INFO
CLUSTER SLOTS

到此集群完成
集群完成后，现在可能配置连接密码验证
cluster.conf修改
requirepass 123456
或者用客户端设置
config set requirepass 123456
注意：能过客户端设置的密码，在重启后消失。
redis-trib.rb 有比较多的功能，添加节点、删除节点等等。还要有就集群后  redis-cli 单机客户端 基上每次get/set 操作都会返回 
[err]move …. 的跳转信息，这是正常的。
````
