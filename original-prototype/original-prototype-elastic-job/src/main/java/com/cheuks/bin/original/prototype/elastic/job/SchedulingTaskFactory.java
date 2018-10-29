package com.cheuks.bin.original.prototype.elastic.job;

import java.util.List;

public interface SchedulingTaskFactory {

	public interface SchedulingJob<T> {
		List<T> fetchData(Context context);
	}

	SchedulingTaskFactory add(SchedulingJob<?>... jobs);

	SchedulingTaskFactory remove(String taskId);

	public static class Context {
		/**
		 * 作业名称.
		 */
		private final String jobName;

		/**
		 * 作业任务ID.
		 */
		private final String taskId;

		/**
		 * 分片总数.
		 */
		private final int shardingTotalCount;

		/**
		 * 作业自定义参数. 可以配置多个相同的作业, 但是用不同的参数作为不同的调度实例.
		 */
		private final String jobParameter;

		/**
		 * 分配于本作业实例的分片项.
		 */
		private final int shardingItem;

		/**
		 * 分配于本作业实例的分片参数.
		 */
		private final String shardingParameter;

		public Context(String jobName, String taskId, int shardingTotalCount, String jobParameter, int shardingItem, String shardingParameter) {
			super();
			this.jobName = jobName;
			this.taskId = taskId;
			this.shardingTotalCount = shardingTotalCount;
			this.jobParameter = jobParameter;
			this.shardingItem = shardingItem;
			this.shardingParameter = shardingParameter;
		}

		public String getJobName() {
			return jobName;
		}

		public String getTaskId() {
			return taskId;
		}

		public int getShardingTotalCount() {
			return shardingTotalCount;
		}

		public String getJobParameter() {
			return jobParameter;
		}

		public int getShardingItem() {
			return shardingItem;
		}

		public String getShardingParameter() {
			return shardingParameter;
		}

	}
}
