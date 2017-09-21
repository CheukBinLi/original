# original
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
<:beans xmlns="http://www.springframework.org/schema/beans" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:rmi="http://cheuks.bin.com/schema/rmi" xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://cheuks.bin.com/schema/rmi http://cheuks.bin.com/schema/rmi.xsd">
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

＜/beans>	
