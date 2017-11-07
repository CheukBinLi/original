package com.cheuks.bin.original.prototype.elastic.job.dataflow;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;

public class SimpleDataFlowJob implements DataflowJob<String> {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleDataFlowJob.class);

	private static final AtomicLong ATOMIC_LONG = new AtomicLong();

	public List<String> fetchData(ShardingContext sc) {
		if (ATOMIC_LONG.addAndGet(1) > 1)
			return null;
		List<String> result = new ArrayList<String>();
		try {
			LOG.info("SERVER:{},JOB:{},PARAM:{},ITEM:{},TOTAL:{},TASKID:{}\n", InetAddress.getLocalHost().getHostName(), sc.getJobName(), sc.getJobParameter(), sc.getShardingItem(), sc.getShardingTotalCount(), sc.getTaskId());
			for (int i = 0; i < 10; i++) {
				result.add("job_" + i);
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return result;
	}

	public void processData(ShardingContext arg0, List<String> arg1) {
		System.err.println(Arrays.toString(arg1.toArray()));
	}

}
