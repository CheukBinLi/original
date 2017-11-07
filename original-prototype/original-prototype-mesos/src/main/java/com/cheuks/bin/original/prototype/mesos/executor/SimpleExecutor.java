package com.cheuks.bin.original.prototype.mesos.executor;

import org.apache.mesos.Executor;
import org.apache.mesos.ExecutorDriver;
import org.apache.mesos.Protos.ExecutorInfo;
import org.apache.mesos.Protos.FrameworkInfo;
import org.apache.mesos.Protos.SlaveInfo;
import org.apache.mesos.Protos.TaskID;
import org.apache.mesos.Protos.TaskInfo;

public class SimpleExecutor implements Executor {

	@Override
	public void registered(ExecutorDriver driver, ExecutorInfo executorInfo, FrameworkInfo frameworkInfo, SlaveInfo slaveInfo) {
		System.out.println("registered");
	}

	@Override
	public void reregistered(ExecutorDriver driver, SlaveInfo slaveInfo) {
		System.out.println("reregistered");
	}

	@Override
	public void disconnected(ExecutorDriver driver) {
		System.out.println("disconnected");
	}

	@Override
	public void launchTask(ExecutorDriver driver, TaskInfo task) {
		System.out.println("launchTask");
	}

	@Override
	public void killTask(ExecutorDriver driver, TaskID taskId) {
		System.out.println("killTask");
	}

	@Override
	public void frameworkMessage(ExecutorDriver driver, byte[] data) {
		System.out.println("frameworkMessage");
	}

	@Override
	public void shutdown(ExecutorDriver driver) {
		System.out.println("shutdown");
	}

	@Override
	public void error(ExecutorDriver driver, String message) {
		System.out.println("error");
	}

}
