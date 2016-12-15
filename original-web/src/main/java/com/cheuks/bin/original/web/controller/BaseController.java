package com.cheuks.bin.original.web.controller;

import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.ContainerProvider;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cheuks.bin.original.web.Client;
import com.cheuks.bin.original.web.customer.MessageOption.SenderType;
import com.cheuks.bin.original.web.customer.MessagePackage;

@Controller
@Scope("prototype")
public class BaseController {

	/***
	 * 
	 * @param request
	 * @param response
	 * @param business
	 *            业务名
	 * @param process
	 *            流程序号
	 * @return
	 */
	// @RequestMapping("**/{business}_{process}")
	// public ModelAndView businessStream(HttpServletRequest request, HttpServletResponse response, @PathVariable("business") String business, @PathVariable("process") String process) {
	// return new ModelAndView(request.getPathInfo());
	// }

	/***
	 * 
	 * @param request
	 * @param response
	 * @param path
	 *            所有路径
	 * @return
	 */
	@RequestMapping("**")
	public ModelAndView anythingPath(HttpServletRequest request, HttpServletResponse response) {
		// System.out.println(request.getScheme());
		// System.out.println(request.getPathInfo());
		return new ModelAndView(request.getPathInfo());
	}

	@RequestMapping("hh")
	public ModelAndView hh(HttpServletRequest request, HttpServletResponse response) {
		// System.out.println(request.getScheme());
		// System.out.println(request.getPathInfo());
		return new ModelAndView("hh");
	}

	@RequestMapping("systemSend")
	public void systemSend(HttpServletRequest request, HttpServletResponse response) {
		f1();
	}

	@RequestMapping("systemSend2")
	public void systemSend2(HttpServletRequest request, HttpServletResponse response) {
		f2();
	}

	ObjectMapper mapper = new ObjectMapper();

	public void f1() {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer(); // 获取WebSocket连接器，其中具体实现可以参照websocket-api.jar的源码,Class.forName("org.apache.tomcat.websocket.WsWebSocketContainer");
			String uri = "ws://localhost:8889/original-web/test?partyId=f41234567890f4&psid=110&senderType=system";
			Session session = container.connectToServer(Client.class, new URI(uri)); // 连接会话
			MessagePackage messagePackage = new MessagePackage("110", SenderType.SYSTEM, "system_110", "web_cs_110");
			messagePackage.setMsg("f1发送的东西");
			session.getBasicRemote().sendText(mapper.writeValueAsString(messagePackage)); // 发送文本消息
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void f2() {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer(); // 获取WebSocket连接器，其中具体实现可以参照websocket-api.jar的源码,Class.forName("org.apache.tomcat.websocket.WsWebSocketContainer");
			String uri = "ws://localhost:8889/original-web/test?psid=1&partyId=1&senderType=CLUSTER";
			Session session = container.connectToServer(Client.class, new URI(uri)); // 连接会话
			// MessagePackage messagePackage = new MessagePackage("110", SenderType.SYSTEM, "system_110", "web_cs_110");
			// messagePackage.setMsg("f1发送的东西");
			// session.getBasicRemote().sendText(mapper.writeValueAsString(messagePackage)); // 发送文本消息
			System.out.println(null == session);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
