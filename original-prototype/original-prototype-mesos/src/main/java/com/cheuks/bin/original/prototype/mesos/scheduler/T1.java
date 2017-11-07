package com.cheuks.bin.original.prototype.mesos.scheduler;

import org.apache.mesos.MesosSchedulerDriver;
import org.apache.mesos.Protos.FrameworkInfo;

public class T1 {

	public static void main(String[] args) {
		
		FrameworkInfo frameworkInfo=FrameworkInfo.newBuilder().setName("first_demo").setUser("wa_ha_ha").build();
		MesosSchedulerDriver driver=new MesosSchedulerDriver(new SchedulerDemo(), frameworkInfo, "zk://10.73.18.103:2181/mesos");
//		MesosSchedulerDriver driver=new MesosSchedulerDriver(new SchedulerDemo(), frameworkInfo, "zk://192.168.3.115:2181/mesos");
		driver.run();
		
	}

}
