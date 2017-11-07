package com.cheuks.bin.original.prototype.mesos.official;

import java.util.List;

import org.apache.mesos.MesosSchedulerDriver;
import org.apache.mesos.Protos.Credential;
import org.apache.mesos.Protos.ExecutorID;
import org.apache.mesos.Protos.FrameworkID;
import org.apache.mesos.Protos.FrameworkInfo;
import org.apache.mesos.Protos.MasterInfo;
import org.apache.mesos.Protos.Offer;
import org.apache.mesos.Protos.OfferID;
import org.apache.mesos.Protos.SlaveID;
import org.apache.mesos.Protos.Status;
import org.apache.mesos.Protos.TaskStatus;
import org.apache.mesos.Scheduler;
import org.apache.mesos.SchedulerDriver;


public class TestExceptionFramework {
  static class TestExceptionScheduler implements Scheduler {
    @Override
    public void registered(SchedulerDriver driver,
                           FrameworkID frameworkId,
                           MasterInfo masterInfo) {
      throw new ArrayIndexOutOfBoundsException();
    }

    @Override
    public void reregistered(SchedulerDriver driver, MasterInfo masterInfo) {}

    @Override
    public void disconnected(SchedulerDriver driver) {}

    @Override
    public void resourceOffers(SchedulerDriver driver,
                               List<Offer> offers) {}

    @Override
    public void offerRescinded(SchedulerDriver driver, OfferID offerId) {}

    @Override
    public void statusUpdate(SchedulerDriver driver, TaskStatus status) {}

    @Override
    public void frameworkMessage(SchedulerDriver driver,
                                 ExecutorID executorId,
                                 SlaveID slaveId,
                                 byte[] data) {}

    @Override
    public void slaveLost(SchedulerDriver driver, SlaveID slaveId) {}

    @Override
    public void executorLost(SchedulerDriver driver,
                             ExecutorID executorId,
                             SlaveID slaveId,
                             int status) {}

    public void error(SchedulerDriver driver, String message) {}
  }

  private static void usage() {
    String name = TestExceptionFramework.class.getName();
    System.err.println("Usage: " + name + " master");
  }

  public static void main(String[] args) throws Exception {
    if (args.length != 1) {
      usage();
      System.exit(1);
    }

    FrameworkInfo.Builder frameworkBuilder = FrameworkInfo.newBuilder()
        .setUser("") // Have Mesos fill in the current user.
        .setName("Exception Framework (Java)");

    MesosSchedulerDriver driver = null;
    if (System.getenv("MESOS_AUTHENTICATE_FRAMEWORKS") != null) {
      System.out.println("Enabling authentication for the framework");

      if (System.getenv("DEFAULT_PRINCIPAL") == null) {
        System.err.println("Expecting authentication principal in the environment");
        System.exit(1);
      }

      if (System.getenv("DEFAULT_SECRET") == null) {
        System.err.println("Expecting authentication secret in the environment");
        System.exit(1);
      }

      Credential credential = Credential.newBuilder()
        .setPrincipal(System.getenv("DEFAULT_PRINCIPAL"))
        .setSecret(System.getenv("DEFAULT_SECRET"))
        .build();

      frameworkBuilder.setPrincipal(System.getenv("DEFAULT_PRINCIPAL"));

      driver = new MesosSchedulerDriver(
          new TestExceptionScheduler(),
          frameworkBuilder.build(),
          args[0],
          credential);
    } else {
      frameworkBuilder.setPrincipal("exception-framework-java");

      driver = new MesosSchedulerDriver(
          new TestExceptionScheduler(),
          frameworkBuilder.build(),
          args[0]);
    }

    int status = driver.run() == Status.DRIVER_STOPPED ? 0 : 1;

    // Ensure that the driver process terminates.
    driver.stop();

    System.exit(status);
  }
}
