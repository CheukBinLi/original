package com.cheuks.bin.original.common.registrationcenter;

import java.util.List;

public interface RegistrationFactory<Client, SubNodeEvent, NodeEvent> {

	void setUrl(String url);

	Client getClient();

	/***
	 * 建立/监听服务节点
	 * 
	 * @param serviceDirectory
	 * @param eventListener
	 * @return
	 * @throws Throwable
	 */
	String createService(String serviceDirectory, RegistrationEventListener<SubNodeEvent> eventListener) throws Throwable;

	/***
	 * 注册服务
	 * 
	 * @param serviceDirectory
	 *            目录
	 * @param value
	 *            注册内容
	 * @return
	 * @throws Throwable
	 */

	String register(String serviceDirectory, String value, RegistrationEventListener<NodeEvent> eventListener) throws Throwable;

	/***
	 * 修改服务内容
	 * 
	 * @param serviceDirectory
	 * @param key
	 * @param value
	 * @return
	 * @throws Throwable
	 */
	String setValue(String serviceDirectory, String value) throws Throwable;

	String getValue(String serviceDirectory) throws Throwable;

	List<String> getServiceList(String serviceDirectory) throws Throwable;

	/***
	 * 删除服务节点
	 * 
	 * @param serviceDirectory
	 * @throws Throwable
	 */
	void removeServiceDirectory(String serviceDirectory) throws Throwable;

	/***
	 * 是否已注册
	 * 
	 * @param serviceDirectory
	 *            目录
	 * @param value
	 *            注册内容
	 * @return
	 * @throws Throwable
	 */
	boolean isRegister(String serviceDirectory, String value) throws Throwable;

	/***
	 * 服务选举
	 * 
	 * @param serviceDirectory
	 *            目录
	 * @param ledderDirectory
	 *            注册内容
	 * @param electionCallBack
	 *            选举回调
	 * @return
	 * @throws Throwable
	 */
	void election(String ledderDirectory, ElectionCallBack electionCallBack) throws Throwable;

	/***
	 * 重新选举
	 * 
	 * @throws Throwable
	 */
	void reelect() throws Throwable;

	/***
	 * 初始化
	 * 
	 * @throws Throwable
	 */
	void init() throws Throwable;
}
