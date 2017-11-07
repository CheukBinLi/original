package com.cheuks.bin.original.prototype.mesos.scheduler;

import java.util.List;

import org.apache.mesos.Protos.ExecutorID;
import org.apache.mesos.Protos.FrameworkID;
import org.apache.mesos.Protos.MasterInfo;
import org.apache.mesos.Protos.Offer;
import org.apache.mesos.Protos.OfferID;
import org.apache.mesos.Protos.SlaveID;
import org.apache.mesos.Protos.TaskStatus;
import org.apache.mesos.Scheduler;
import org.apache.mesos.SchedulerDriver;

public class SchedulerDemo implements Scheduler{

	@Override
	public void registered(SchedulerDriver driver, FrameworkID frameworkId, MasterInfo masterInfo) {
		System.out.println(String.format("registered driver:%s , frameworkId:%s , masterInfo:%s", driver.toString(),frameworkId.getValue(),masterInfo.toString()));
		
	}

	@Override
	public void reregistered(SchedulerDriver driver, MasterInfo masterInfo) {
		System.out.println("reregistered 2");		
	}

	@Override
	public void resourceOffers(SchedulerDriver driver, List<Offer> offers) {
		System.out.println("resourceOffers:"+offers.size());		
	}

	@Override
	public void offerRescinded(SchedulerDriver driver, OfferID offerId) {
		System.out.println("offerRescinded");		
	}

	@Override
	public void statusUpdate(SchedulerDriver driver, TaskStatus status) {
		System.out.println("statusUpdate");		
	}

	@Override
	public void frameworkMessage(SchedulerDriver driver, ExecutorID executorId, SlaveID slaveId, byte[] data) {
		System.out.println("frameworkMessage");
	}

	@Override
	public void disconnected(SchedulerDriver driver) {
		System.out.println("disconnected");		
	}

	@Override
	public void slaveLost(SchedulerDriver driver, SlaveID slaveId) {
		System.out.println("slaveLost");
	}

	@Override
	public void executorLost(SchedulerDriver driver, ExecutorID executorId, SlaveID slaveId, int status) {
		System.out.println("executorLost");
	}

	@Override
	public void error(SchedulerDriver driver, String message) {
		System.out.println("error");
	}

}
