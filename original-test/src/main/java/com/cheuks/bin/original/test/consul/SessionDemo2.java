package com.cheuks.bin.original.test.consul;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
import com.orbitz.consul.model.event.Event;
import com.orbitz.consul.model.session.ImmutableSession;
import com.orbitz.consul.model.session.SessionCreatedResponse;

public class SessionDemo2 {

	private final static Logger LOG = LoggerFactory.getLogger(SessionDemo2.class);

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

		// List<String> locks;
		// String lock;
		// if ((locks = keyValueClient.getValuesAsString("lock")).isEmpty()) {
		SessionCreatedResponse response = session.createSession(ImmutableSession.builder().name("lock").addChecks("so_10088").build());
		// LOG.info(response.toString());
		// keyValueClient.putValue("lock", lock = response.getId());
		// } else {
		// lock = locks.get(0);
		// }

		boolean flags;
		if (flags = keyValueClient.acquireLock("ledder", response.getId())) {
			System.err.println(keyValueClient.putValue("ledder", "sessionDemo2"));
			// synchronized (SessionDemo1.class) {
			// SessionDemo1.class.wait();
			// }
			Thread.sleep(60000);
			keyValueClient.releaseLock("ledder", response.getId());
		}
		session.destroySession(response.getId());
		System.err.println("is ledder:" + flags + " sessionId:" + response.getId());

	}

	public static void main(String[] args) throws InterruptedException {
		SessionDemo2 sd = new SessionDemo2();
		// //健康检查
		// sd.server_health();
		// //注册检测服务
		// sd.registerCheck();
		// Thread.sleep(2000);
		//
		sd.a();
		synchronized (SessionDemo2.class) {
			SessionDemo2.class.wait();
		}
	}

	public void registerCheck() {
		AgentClient client = consul.agentClient();
		ImmutableCheck check = ImmutableCheck.builder().tcp("10.73.11.115:10088").id("so_10088").name("本地10088端口").interval("10s").build();
		client.registerCheck(check);
	}

	public void server_health() {
		Thread thread = new Thread(new Runnable() {

			public void run() {
				try {
					ServerSocket server = new ServerSocket(10088);
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
