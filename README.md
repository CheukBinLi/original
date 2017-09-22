# original
### 功能描述
```
一切接口马上变为程远程接口，分布式调用就变得及其简单。无需做特定适配、修改、继承等等操作，只有一点要注意：
如果想要客户端知道实现抛出了什么错误，那么实现的异常抛出一定要向上一层抛出，不然无法拦截到错误信息。

使用方法:只需要实上添加注解/在XML文件里配置一下即可以。对象完全依赖spring注入管理。
直接使用spring的注解即可注入。或者xml注入对应的 注册id。

程序是长连接，终止是效率、和便捷，尽可以的简单。
支持负载均衡，服务器挂掉自动寻找其它服务器(自动发现),支持更换序列模块（自带fst,JDK序列化，kryo）
更多的可以自己增加(probuf、thrift、json、xml等等)，只需要实现序列化接口即可。

 此工具类组城主要有:netty做网络核心部件，上一版nio做网络部件池化管理易容出现问题这一版直接移除，
 因此网络部件没有做模块更换序列的设计。消息处理，可以自定义增加功能，不在介绍了。

注册中心使用了最为广泛的zookeeper,consul模块没时间做，还不能用。现在有的功能：p2p、zookeeper 两种模式，p2p一般开发调试用。
P2P模式:        p2p://192.168.1.101:10086   或者   192.168.1.101:10086
zookeeper模式:  zookeeper://192.168.1.101:2181
consul模式:     consul://192.168.1.101:8500
```
#### 例如 - 注解例子
##### 接口(服务端/客户端)
```
package xx.inf;
@RmiConsumerAnnotation
public interface test2I {
	int a(String a, int b, char c, long d, Boolean e);
}
```
##### 实现(服务端)
```
package server.inf.impl;
@RmiProviderAnnotation(interfaceClass = test2I.class)
public class test2 implements test2I {
	public int a(String a, int b, char c, long d, Boolean e) {
		return 0;
	}
}
```
##### 服务端xml配置
```
<rmi:config>>
        <rmi:registry serverAddress="zookeeper://127.0.0.1:2181" />
       <rmi:protocol port="119" />
</rmi:config></br>
<rmi:annotation-driven>></br>
       <!--扫描指定包路径,扫描实现-->
       <rmi:service packagePath="server.inf.impl" applicationName="MMX" />
</rmi:annotation-driven>
```
##### 客户端xml配置
```
<rmi:config>
	<rmi:registry serverAddress="zookeeper://127.0.0.1:2181" />
	<rmi:protocol port="119" />
</rmi:config>
<rmi:annotation-driven>
	<!--扫描指定包路径,扫描接口,支持多段路径扫描用豆号分隔-->
	<rmi:reference packagePath="xx.inf,yyy.inf" applicationName="MMX" />
</rmi:annotation-driven>
```
##### 使用 提供者端
```
public class A{

	public static main(String[] args){
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-config.xml");
		applicationContext.start();
	}
}
```
##### 使用 消费端
```
@Component
public class A{
	@Autowired
	private test2I test2i;
	
	public static main(String[] args){
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-config.xml");
		applicationContext.start();
		A a=applicationContext.getBean(A.class);
		a.test2i.a(1,2,3,4);
		//方法2
		test2I t2=applicationContext.getBean(test2I.class);
		a.test2i.a(1,2,3,4);
	}
}
```
#### xml配置使用
```
<bean id="test3impl" class="server.inf.impl.test3.impl"/>
<!--服务器配置-->
<rmi:service-group applicationName="NBA"> 
	<rmi:service id="CCTV-1" interface="com.cheuks.bin.original.rmi.t.test2I" class="com.cheuks.bin.original.rmi.t.test2" /> 
	<!--直接引用spring的bean-->
	<rmi:service interface="com.cheuks.bin.original.rmi.t.test3I" ref="test3impl" />
</rmi:service-group> 

<!--客户端配置-->
<rmi:reference-group applicationName="NBA"> 
	<rmi:reference interface="com.cheuks.bin.original.rmi.t.test2I" />
</rmi:reference-group>
```
直接配置就可以。使用方法跟注解那个main函数的方法一样。

#### 注意
##### 一般使场景
##### 提供者使用注解(注解实现类)
##### 消费者使用xml配置
##### 原因：提供者给用户的接口90%都是打成jar包。所以一般情况都会这样使用
##### 如果同一个项目既运行行了服务端，同时也运行了消费端，而已用房又用了直接注入的注解，注入时没指定注入的实现ID，会抛出异常。原因是程序注册了两个实现，所有必须指定实现的ID。最好还是不要用把  服务端/客户端同时运行在一个进程里。
##### 同一个接口，不同的实现，最多是通过放在不同的 <应用名>（applicationName）或者不同的<版本>(version) 来区分。



### maven仓库引用
只需加入私有库信息
```
	<repositories>
		<repository>
			<id>original-maven-repository</id>
			<url>https://raw.github.com/fdisk123/original/snapshot2.11</url>
		</repository>
	</repositories>
```
### 引入配置文件
```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:rmi="http://cheuks.bin.com/schema/rmi" xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://cheuks.bin.com/schema/rmi http://cheuks.bin.com/schema/rmi.xsd">

	<!-- 序列化实例，可以自由替换，默认使用fst-->
	<bean id="abcdef" class="com.cheuks.bin.original.cache.DefaultCacheSerialize" />

	<rmi:config>
		<!-- <rmi:registry serverAddress="zookeeper://10.73.18.105:2181" /> -->
		<!--注册中心地址(不带zookeeper://协议显为P2P模式，即连接到指定服务服务提供者) -->
		<rmi:registry serverAddress="127.0.0.1:119" />
		<!--本机信息配置,存在多张已连接的网卡，必需手工配置:localAddress、localName-->
		<rmi:protocol port="119"/>
		<!-- 序列化实例，可以自由替换，默认使用fst-->
		<!--<rmi:protocol port="119" refSerialize="abcdef" />-->
	</rmi:config>

	<!--服务供者，暴露的服务--> 
	<!-- <rmi:service-group applicationName="NBA"> 
		<rmi:service id="CCTV-1" interface="com.cheuks.bin.original.rmi.t.test2I" class="com.cheuks.bin.original.rmi.t.test2" /> 
	</rmi:service-group>--> 
	
	<!--消费者，暴露的需求服务--> 
	<!--<rmi:reference-group applicationName="NBA">
		<rmi:reference interface="com.cheuks.bin.original.rmi.t.test2I" id="CCTV2" /> 
	</rmi:reference-group> -->

	<!-- 注解驱动 -->
	<rmi:annotation-driven>
		<!--服务供者，暴露的服务--> 
		<!--  <rmi:service packagePath="com.cheuks.bin.original.rmi.t" applicationName="MMX" /> -->
		<!--消费者，暴露的需求服务--> 
		<!-- <rmi:reference packagePath="com.cheuks.bin.original.rmi.t" applicationName="MMX"/> -->
	</rmi:annotation-driven>
<br/>
</beans>
```
日志输出log4j
```
#log4j.logger.com.cheuks=ALL
log4j.logger.com.cheuks.bin.original.rmi=info
```

##### 东西写得不好请见谅。
