# original
### 功能描述
    一切接口马上变远程远程。无需做特定适配、修改、继承等等操作。只需要实上添加注解/在XML文件里配置一下即可以。对象完全依赖spring注入管理。直接使用spring的注解即可注入。或者xml注入对应的 注册id。

#### 例如 - 注解例子
##### 接口(服务端/客户端)
package xx.inf;
@RmiConsumerAnnotation
public interface test2I {
	int a(String a, int b, char c, long d, Boolean e);
}
##### 实现(服务端)
package server.inf.impl;
@RmiProviderAnnotation(interfaceClass = test2I.class)
public class test2 implements test2I {
	public int a(String a, int b, char c, long d, Boolean e) {
		return 0;
	}
}

##### 服务端xml配置
<<rmi:config></br>
	<rmi:registry serverAddress="zookeeper://127.0.0.1:2181" /></br>
	<rmi:protocol port="119" /></br>
</rmi:config></br>
<rmi:annotation-driven></br>
	<!--扫描指定包路径,扫描实现--></br>
	<rmi:service packagePath="server.inf.impl" applicationName="MMX" /></br>
</rmi:annotation-driven></br>

##### 客户端xml配置
<rmi:config>
	<rmi:registry serverAddress="zookeeper://127.0.0.1:2181" />
	<rmi:protocol port="119" />
</rmi:config>
<rmi:annotation-driven>
	<!--扫描指定包路径,扫描接口-->
	<rmi:reference packagePath="xx.inf" applicationName="MMX" />
</rmi:annotation-driven>

##### 使用
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

#### xml配置使用
<bean id="test3impl" class="server.inf.impl.test3.impl"/>
<rmi:service-group applicationName="NBA"> 
	<rmi:service id="CCTV-1" interface="com.cheuks.bin.original.rmi.t.test2I" class="com.cheuks.bin.original.rmi.t.test2" /> 
	<!--直接引用spring的bean-->
	<rmi:service id="CCTV-1" interface="com.cheuks.bin.original.rmi.t.test3I" ref="test3impl" />
</rmi:service-group> 
	
<rmi:reference-group applicationName="NBA"> 
	<rmi:reference interface="com.cheuks.bin.original.rmi.t.test2I" id="CCTV2" />
</rmi:reference-group>

直接配置就可以。使用方法跟注解那个main函数的方法一样。




### maven仓库引用
    只需加入私有库信息

	<repositories>
		<repository>
			<id>original-maven-repository</id>
			<url>https://raw.github.com/fdisk123/original/snapshot2.11</url>
		</repository>
	</repositories>

### 引入配置文件

＜?xml version="1.0" encoding="UTF-8"?>
<br/>
＜beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:rmi="http://cheuks.bin.com/schema/rmi" xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://cheuks.bin.com/schema/rmi http://cheuks.bin.com/schema/rmi.xsd">
<br/>
	<!-- 序列化实例，可以自由替换，默认使用fst-->
	＜bean id="abcdef" class="com.cheuks.bin.original.cache.DefaultCacheSerialize" />

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
＜/beans>	
