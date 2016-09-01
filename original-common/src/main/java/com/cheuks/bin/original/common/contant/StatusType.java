package com.cheuks.bin.original.common.contant;

public interface StatusType {

	int INIT = 0;// 初始状态
	int ACCOUNT_NORMAL = 2;// 正常
	int ACCOUNT_LOCK = 4;// 锁定、冻结
	int ACCOUNT_ABNORMAL = 8;// 异常
	int ENABLE = 2;// 启用
	int DISABLE = 4;// 禁用
	int WAIT_FOR_DEL = -127;// 删除等待
}
