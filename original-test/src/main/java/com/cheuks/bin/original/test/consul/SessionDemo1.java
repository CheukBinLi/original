package com.cheuks.bin.original.test.consul;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.bouncycastle.util.IPAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.AgentClient;
import com.orbitz.consul.Consul;
import com.orbitz.consul.KeyValueClient;
import com.orbitz.consul.SessionClient;
import com.orbitz.consul.async.EventResponseCallback;
import com.orbitz.consul.model.EventResponse;
import com.orbitz.consul.model.agent.ImmutableCheck;
import com.orbitz.consul.model.agent.ImmutableRegCheck;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.event.Event;
import com.orbitz.consul.model.session.ImmutableSession;
import com.orbitz.consul.model.session.SessionCreatedResponse;
import com.orbitz.consul.model.session.SessionInfo;

public class SessionDemo1 {

	private final static Logger LOG = LoggerFactory.getLogger(SessionDemo1.class);

	// String server = "192.168.31.88";
	String server = "192.168.3.66";
	// String server = "10.73.11.118";

	Consul consul = Consul.builder().withHostAndPort(HostAndPort.fromParts(server, 8500)).build();

	public void a() throws InterruptedException {
		AgentClient client = consul.agentClient();
		SessionClient session = consul.sessionClient();
		KeyValueClient keyValueClient = consul.keyValueClient();

		consul.eventClient().listEvents(new EventResponseCallback() {

			public void onFailure(Throwable throwable) {
				throwable.printStackTrace();
			}

			public void onComplete(EventResponse eventResponse) {
				for (Event e : eventResponse.getEvents()) {
					System.err.println(e.getName());
				}
			}
		});

		List<SessionInfo> list = session.listSessions();
		for (SessionInfo sessionInfo : list) {
			LOG.info(sessionInfo.getId() + ":" + sessionInfo.getName());
			session.destroySession(sessionInfo.getId());
			keyValueClient.releaseLock("lock", sessionInfo.getId());
		}

		SessionCreatedResponse response = session.createSession(ImmutableSession.builder().name("lock").addChecks("v1/agent/checks/so_10088").build());
		Object lockX = session.getSessionInfo(response.getId());
		String lock = response.getId();
		LOG.info(response.toString());
		System.err.println(keyValueClient.getValue("NNNNN"));
		keyValueClient.acquireLock("NNNNN", lock);
		System.err.println(keyValueClient.putValue("NNNNN", "xxxxxxxxxx"));
		// synchronized (SessionDemo1.class) {
		// SessionDemo1.class.wait();
		// }
		keyValueClient.releaseLock("NNNNN", lock);
		System.err.println(keyValueClient.putValue("NNNNNxxx", "nba1024"));
		System.err.println(keyValueClient.getValue("NNNNNxxx"));
		System.err.println(keyValueClient.putValue("abc", "111"));
		System.err.println(keyValueClient.getValue("abc"));

	}

	public static void main(String[] args) throws InterruptedException {
		SessionDemo1 sd = new SessionDemo1();
		// 健康检查
		sd.server_health();
		// 注册检测服务
		sd.registerCheck();
		//
		sd.a();
		synchronized (SessionDemo1.class) {
			SessionDemo1.class.wait();
		}
	}

	public void registerCheck() {
		try {
			AgentClient client = consul.agentClient();
			ImmutableCheck check;
			check = ImmutableCheck.builder().tcp(InetAddress.getLocalHost().getHostAddress() + ":10010").id("so_10010").name("本地10010端口").interval("10s").build();
			// client.deregisterCheck("so_10088");
			ImmutableRegCheck regCheck = ImmutableRegCheck.builder().tcp(InetAddress.getLocalHost().getHostAddress() + ":10010").interval("10s").build();

			ImmutableRegistration register = ImmutableRegistration.builder().address(InetAddress.getLocalHost().getHostAddress()).port(10088).addTags("localhost_test").name("测试服务").id("test_10088").check(regCheck).build();
			client.registerCheck(check);
			client.register(register);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void server_health() {
		Thread thread = new Thread(new Runnable() {
			public void run() {
				try {
					ServerSocket server = new ServerSocket(10010);
					Socket client;
					OutputStream out;
					while (true) {
						client = server.accept();
						out = client.getOutputStream();
						out.write("hi".getBytes());
						out.flush();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

}
