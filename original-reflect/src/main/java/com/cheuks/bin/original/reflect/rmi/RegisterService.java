package com.cheuks.bin.original.reflect.rmi;

public interface RegisterService {

	final String SERVICE_ROOT = "/orinal_rmi";
	final String SERVICE_CUSTOMER = "/customer";
	final String SERVICE_PRODUCER = "/producer";
	final String SERVICE_LOAD = "/load";
	final String SERVICE_LEDDER = "/ledder";
	final String SEPARATOR = "@";

	String register() throws Throwable;
}
