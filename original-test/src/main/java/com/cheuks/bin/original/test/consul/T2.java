package com.cheuks.bin.original.test.consul;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


import com.google.common.net.HostAndPort;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.CatalogClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
import com.orbitz.consul.model.ConsulResponse;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Member;
import com.orbitz.consul.model.catalog.ImmutableCatalogDeregistration;
import com.orbitz.consul.model.catalog.ImmutableCatalogRegistration;
import com.orbitz.consul.model.health.Node;
import com.orbitz.consul.model.health.Service;

public class T2 {

//    private static final Logger log = LoggerFactory.getLogger(T2.class);

    static Consul consul = Consul.builder().withHostAndPort(HostAndPort.fromParts("10.73.11.104", 8500)).build();;
    //    static Consul consul = Consul.builder().build();

    public void register(String serviceName, String serviceId) throws MalformedURLException {
        AgentClient client = consul.agentClient();
        ImmutableRegistration.Builder builder = ImmutableRegistration.builder();
        //        builder.address("10.73.11.40").port(8080).name("tomcat").check;
        client.register(8080, new URL("http://localhost:8080/milestone-web/health"), 10, "tomcat", "nba123", "v1");
    }

    public void keyRegister() {
        KeyValueClient client = consul.keyValueClient();
        client.putValue("T2_keyRegister", "T2_keyRegister_test");
    }

    public void clean() {
//        log.info("***********************consul上无效服务清理开始*******************************************");
        //获取所有的members的信息  
        List<Member> members = consul.agentClient().getMembers();
        for (int i = 0; i < members.size(); i++) {
            //获取每个member的IP地址  
            String address = members.get(i).getAddress();
//            log.info("member的IP地址为:{}", address);
            //根据role变量获取每个member的角色  role：consul---代表服务端   role：node---代表客户端  
            String role = members.get(i).getTags().get("role");
//            log.info("{}机器的role为：{}=====注释*role为consul代表服务端   role为node代表客户端", address, role);
            //判断是否为client  
            if (role.equals("node")) {
                //将IP地址传给ConsulClient的构造方法，获取对象  
                //根据clearClient，获取当前IP下所有的服务 使用迭代方式 获取map对象的值  
                Iterator<Map.Entry<String, Service>> it = Consul.builder().withHostAndPort(HostAndPort.fromParts(members.get(i).getAddress(), members.get(i).getPort())).build().agentClient().getServices().entrySet().iterator();
                while (it.hasNext()) {
                    //迭代数据  
                    Map.Entry<String, Service> serviceMap = it.next();
                    //获得Service对象  
                    Service service = serviceMap.getValue();
                    //获取服务名称  
                    String serviceName = service.getService();
                    //获取服务ID  
                    String serviceId = service.getId();
//                    log.info("在{}客户端上的服务名称 :{}**服务ID:{}", address, serviceName, serviceId);
                    //根据服务名称获取服务的健康检查信息  
                    //获取健康状态值  PASSING：正常  WARNING  CRITICAL  UNKNOWN：不正常  
                    consul.agentClient().deregister(serviceId);
                }
            }
        }
    }

    public void cleanNodes() {
        ConsulResponse<List<Node>> c = consul.catalogClient().getNodes();
        List<Node> nodes = c.getResponse();
        CatalogClient client = consul.catalogClient();
        ImmutableCatalogDeregistration deregistration;
        //        for (Node node : nodes) {
        //            log.info(node.getNode());
        //            deregistration = ImmutableCatalogDeregistration.builder().node(node.getNode()).build();
        //            client.deregister(deregistration);
        //        }
        //        ImmutableCatalogRegistration o = ImmutableCatalogRegistration.builder().address("10.73.11.103").node("os_2").build();
        client.register(ImmutableCatalogRegistration.builder().address("10.73.11.104").node("main_server").build());
        client.register(ImmutableCatalogRegistration.builder().address("10.73.11.103").node("os_2").build());
        client.register(ImmutableCatalogRegistration.builder().address("10.73.11.101").node("win_1").build());

    }

    public static void main(String[] args) throws MalformedURLException {
//        PropertyConfigurator.configure(T2.class.getResource("/").getPath() + "log4j.properties");
        T2 t2 = new T2();
        consul.agentClient().deregister("192.168.3.66");
        consul.agentClient().deregister("192.168.3.62");
        //        consul.agentClient().deregister("consul");

        Map<String, Service> a = consul.agentClient().getServices();
        System.err.println(a);
        try {
            t2.clean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        t2.cleanNodes();
        //        t2.keyRegister();
        //        t2.register(null, null);
        //
        //        ImmutableSession session = ImmutableSession.builder().name("session_test_1").build();
        //        consul.sessionClient().createSession(session);
        //
        //        t2.clean();
        //        synchronized (T2.class) {
        //            try {
        //                T2.class.wait();
        //                //                consul.keyValueClient()
        //                //                consul.sessionClient().
        //            } catch (InterruptedException e) {
        //                e.printStackTrace();
        //            }
        //        }
        //        //        consul.agentClient().deregister("xx");
    }

}
