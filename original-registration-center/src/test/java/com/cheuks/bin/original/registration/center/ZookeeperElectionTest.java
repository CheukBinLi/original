package com.cheuks.bin.original.registration.center;

import java.util.concurrent.CountDownLatch;

import com.cheuks.bin.original.common.registrationcenter.ElectionCallBack;

public class ZookeeperElectionTest {

	public static void main(String[] args) {
		ZookeeperRegistrationFactory zrf = new ZookeeperRegistrationFactory("10.73.18.106:2181");
		try {
			zrf.start();
			ElectionCallBack electionCallBack = new ElectionCallBack() {

				public void callBack(boolean isLeader) {
					if (isLeader) {
						System.out.println(Thread.currentThread().getName()+" is leader");
					}
				}
			};

			zrf.election("/LeaderElection", electionCallBack);
			CountDownLatch cdl = new CountDownLatch(1);
			cdl.await();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
