package com.cheuks.bin.original.reflect.rmi.model;

import java.io.Serializable;

/***
 * 传输模型
 * 
 * @author ben
 *
 */
public final class TransmissionModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private int serviceType;// ping/close/wait.....

	/***
	 * 校验ID
	 */
	private long id;
	/***
	 * md5: className+version+method
	 */
	private String methodCode;
	/***
	 * 方法传参
	 */
	private Object[] params;
	/***
	 * 回调出错
	 */
	private Throwable error;
	/***
	 * 返回
	 */
	private Object result;

	public long getId() {
		return id;
	}

	public TransmissionModel setId(long id) {
		this.id = id;
		return this;
	}

	public int getServiceType() {
		return serviceType;
	}

	public TransmissionModel setServiceType(int serviceType) {
		this.serviceType = serviceType;
		return this;
	}

	public String getMethodCode() {
		return methodCode;
	}

	public TransmissionModel setMethodCode(String methodCode) {
		this.methodCode = methodCode;
		return this;
	}

	public Object[] getParams() {
		return params;
	}

	public TransmissionModel setParams(Object[] params) {
		this.params = params;
		return this;
	}

	public Throwable getError() {
		return error;
	}

	public TransmissionModel setError(Throwable error) {
		this.error = error;
		return this;
	}

	public Object getResult() {
		return result;
	}

	public TransmissionModel setResult(Object result) {
		this.result = result;
		return this;
	}

	public TransmissionModel() {
		super();
	}

	public TransmissionModel(int serviceType) {
		super();
		this.serviceType = serviceType;
	}

}
