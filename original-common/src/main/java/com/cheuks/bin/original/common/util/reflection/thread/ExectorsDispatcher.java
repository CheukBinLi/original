package com.cheuks.bin.original.common.util.reflection.thread;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExectorsDispatcher implements ExecutorService {

	private final ExecutorService executorService;

	private final LinkedList<DispatcherRunnable<Object>> RUNNABLE_QUEUE = new LinkedList<DispatcherRunnable<Object>>();

	public ExectorsDispatcher(ExecutorService executorService) {
		this.executorService = executorService;
	}

	public void addRunnableGroupJob(Object job) {
		synchronized (RUNNABLE_QUEUE) {
			if (RUNNABLE_QUEUE.isEmpty()) {
				return;
			}
			DispatcherRunnable<Object> runnable = RUNNABLE_QUEUE.removeFirst();
			runnable.add(job);
			RUNNABLE_QUEUE.addLast(runnable);
		}
	}

	public void execute(final DispatcherRunnable<Object> command) {
		synchronized (RUNNABLE_QUEUE) {
			RUNNABLE_QUEUE.addLast(command);
			executorService.execute(command);
		}
	}

	@Override
	public void execute(Runnable command) {

	}

	@Override
	public void shutdown() {
		executorService.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return executorService.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return executorService.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return executorService.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return executorService.awaitTermination(timeout, unit);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return executorService.submit(task);
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return executorService.submit(task, result);
	}

	@Override
	public Future<?> submit(Runnable task) {
		return executorService.submit(task);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return executorService.invokeAll(tasks);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
		return executorService.invokeAll(tasks, timeout, unit);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return executorService.invokeAny(tasks);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return executorService.invokeAny(tasks, timeout, unit);
	}

}
