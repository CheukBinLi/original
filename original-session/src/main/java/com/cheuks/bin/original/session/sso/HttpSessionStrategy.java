package com.cheuks.bin.original.session.sso;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.session.Session;
import org.springframework.session.web.http.MultiHttpSessionStrategy;

public class HttpSessionStrategy implements MultiHttpSessionStrategy {

	public String getRequestedSessionId(HttpServletRequest request) {
		String sessionId = UUID.randomUUID().toString();
		System.out.println("getRequestedSessionId:" + sessionId);
		return sessionId;
	}

	public void onNewSession(Session session, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("onNewSession:" + session.toString());
	}

	public void onInvalidateSession(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("onInvalidateSession");
	}

	public HttpServletRequest wrapRequest(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("wrapRequest");
		return request;
	}

	public HttpServletResponse wrapResponse(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("wrapResponse");
		return response;
	}

}
