package com.cheuks.bin.original.registration.center;

import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

import com.cheuks.bin.original.common.registrationcenter.ElectionCallBack;
import com.cheuks.bin.original.common.registrationcenter.RegistrationEventListener;

public class ZookeeperTest {

	public static void main(String[] args) {
		ZookeeperRegistrationFactory zrf = new ZookeeperRegistrationFactory("10.73.18.106:2181");
		try {
			zrf.start();
			String directory = zrf.createService("/service", new RegistrationEventListener<PathChildrenCacheEvent>() {

				public void nodeChanged(PathChildrenCacheEvent params, Object... obj) throws Exception {
					System.err.println("createService:" + new String(params.getData().getData()));
				}
			});

			zrf.register(directory + "/mmx_1", "朋友小红是我的！", new RegistrationEventListener<NodeCache>() {

				public void nodeChanged(NodeCache params, Object... obj) throws Exception {
					System.err.println("register:" + new String(params.getCurrentData().getData()));
				}
			});
			zrf.register(directory + "/mmx_4", "我是mmx_4", null);
			// zrf.register(directory + "/mmx_4/temp_node", "我是mmx_4的临时节点字节点", null);

			zrf.setValue(directory + "/mmx_1", "441");

			ElectionCallBack electionCallBack = new ElectionCallBack() {

				public void callBack(boolean isLeader) {
					while (isLeader) {
						try {
							Thread.sleep(5000);
							System.out.println(Thread.currentThread().getName());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

			};

			zrf.election("/LeaderElection", electionCallBack);

			zrf.reelect();

			CountDownLatch cdl = new CountDownLatch(1);
			cdl.await();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
