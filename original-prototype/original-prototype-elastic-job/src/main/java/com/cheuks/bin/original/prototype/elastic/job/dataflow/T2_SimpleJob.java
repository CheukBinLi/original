package com.cheuks.bin.original.prototype.elastic.job.dataflow;

import com.cheuks.bin.original.prototype.elastic.job.SchedulingTaskFactory;
import com.cheuks.bin.original.prototype.elastic.job.SchedulingTaskFactory.Context;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

public class T2_SimpleJob implements SimpleJob {

	SchedulingTaskFactory.SchedulingJob<?> job;

	public void execute(ShardingContext shardingContext) {
		if (null != job) {
			job.fetchData(new Context(shardingContext.getJobName(), shardingContext.getTaskId(), shardingContext.getShardingTotalCount(), shardingContext.getJobParameter(), shardingContext.getShardingItem(), shardingContext.getShardingParameter()));
		}
		System.out.println("aaaaaa");
		System.err.println(T2_SimpleJob.class.getCanonicalName());
	}

}
