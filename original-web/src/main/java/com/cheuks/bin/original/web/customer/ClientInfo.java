package com.cheuks.bin.original.web.customer;

public class ClientInfo {
	private String partyId;// 用户店城ID
	private String openId;// 用户微信ID
	private String nickName;// 别名
	private String headImage;// 头像
	private int status;// 等待、等待发回、等待回复
	private long lasterOperation;// 最新接触时间

	public String getPartyId() {
		return partyId;
	}

	public ClientInfo setPartyId(String partyId) {
		this.partyId = partyId;
		return this;
	}

	public String getOpenId() {
		return openId;
	}

	public ClientInfo setOpenId(String openId) {
		this.openId = openId;
		return this;
	}

	public String getNickName() {
		return nickName;
	}

	public ClientInfo setNickName(String nickName) {
		this.nickName = nickName;
		return this;
	}

	public String getHeadImage() {
		return headImage;
	}

	public ClientInfo setHeadImage(String headImage) {
		this.headImage = headImage;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public ClientInfo setStatus(int status) {
		this.status = status;
		return this;
	}

	public long getLasterOperation() {
		return lasterOperation;
	}

	public ClientInfo setLasterOperation(long lasterOperation) {
		this.lasterOperation = lasterOperation;
		return this;
	}

}
