package com.cheuks.bin.original.test.consul;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.HealthClient;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.health.Service;

public class T1 {

    static Consul consul = Consul.builder().withHostAndPort(HostAndPort.fromParts("10.73.11.43", 8500)).build();

    @Test
    public void x1() {
        AgentClient agent = consul.agentClient();

        //健康检测  
        ImmutableRegCheck check = ImmutableRegCheck.builder().http("http://127.0.0.1:8080/health").interval("5s").build();
        HostAndPort X = HostAndPort.fromString("");
        ImmutableRegistration.Builder builder = ImmutableRegistration.builder();
        builder.id("tomcat1").name("tomcat").addTags("v1").address("127.0.0.1").port(8080).addChecks(check);
        //        builder.id("tomcat1").name("tomcat").addTags("v1").address("192.168.1.104").port(8080);

        agent.register(builder.build());
    }

    @Test
    public void x2() {
        HealthClient client = consul.healthClient();
        String name = "tomcat";
        //获取所有服务  
        System.out.println(client.getAllServiceInstances(name).getResponse().size());

        //获取所有正常的服务（健康检测通过的）  
        client.getHealthyServiceInstances(name).getResponse().forEach((resp) -> {
            System.out.println(resp);
        });
    }

    public static void main(String[] args) {
        consul.agentClient().deregister("tomcat1");

        Object o = consul.catalogClient().getServices();
        System.err.println(o);
        Map<String, Service> services = consul.agentClient().getServices();
        for (Entry<String, Service> en : services.entrySet()) {
            System.out.println(en.getKey() + ":" + en.getValue().getService());
        }
        //        T1 t = new T1();
        //        t.x1();
    }

}
