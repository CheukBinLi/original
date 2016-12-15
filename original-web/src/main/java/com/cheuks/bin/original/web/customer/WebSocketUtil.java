package com.cheuks.bin.original.web.customer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import com.cheuks.bin.original.web.customer.MessageOption.SenderType;

public class WebSocketUtil {

	/***
	 * 
	 * @param annotatedEndpointHandleClass
	 *            连接成功回调处理:要注解 @ClientEndpoint(javax.websocket.ClientEndpoint)
	 * @param serverPath
	 *            服务器地址 localhost:8081
	 * @param params
	 *            "id=1","sex=男","age=21"
	 * @return
	 * @throws DeploymentException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static Session connection(final Class<?> annotatedEndpointHandleClass, String serverPath, String... params) throws DeploymentException, IOException, URISyntaxException {
		WebSocketContainer container = ContainerProvider.getWebSocketContainer(); // 获取WebSocket连接器，其中具体实现可以参照websocket-api.jar的源码,Class.forName("org.apache.tomcat.websocket.WsWebSocketContainer");
		// String uri = "localhost:8888/original-web/test?partyId=f41234567890f4&psid=110&senderType=system";
		StringBuilder sb = new StringBuilder("ws://" + serverPath + "?");
		if (null != params) {
			for (String str : params) {
				sb.append(str).append("&");
			}
			sb.setLength(sb.length() - 1);
		}
		Session session = container.connectToServer(annotatedEndpointHandleClass, new URI(sb.toString())); // 连接会话
		return session;
	}

	/***
	 * 
	 * @param annotatedEndpointHandleClass
	 *            连接成功回调处理:要注解 @ClientEndpoint(javax.websocket.ClientEndpoint)
	 * @param serverPath
	 *            服务器地址 localhost:8081
	 * @param psid
	 * @param partyId
	 * @param senderType
	 *            (SYSTEM, CUSTOMER_SERVICE......)
	 * @return
	 * @throws DeploymentException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public static Session connectionCluster(final Class<?> annotatedEndpointHandleClass, String serverPath, String psid, String partyId, SenderType senderType) throws DeploymentException, IOException, URISyntaxException {
		return connection(annotatedEndpointHandleClass, serverPath, String.format("psid=%s&partyId=%s&senderType=%s", psid, partyId, senderType.toString()));
	}
}
