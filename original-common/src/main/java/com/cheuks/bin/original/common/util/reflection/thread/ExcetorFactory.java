package com.cheuks.bin.original.common.util.reflection.thread;

import java.security.AccessControlContext;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import sun.security.util.SecurityConstants;

public class ExcetorFactory {

	 public static ExecutorService newFixedThreadPool(int nThreads) {
	        return new ThreadPoolExecutor(nThreads, nThreads,
	                                      0L, TimeUnit.MILLISECONDS,
	                                      new LinkedBlockingQueue<Runnable>());
	    }

	    public static ExecutorService newWorkStealingPool(int parallelism) {
	        return new ForkJoinPool
	            (parallelism,
	             ForkJoinPool.defaultForkJoinWorkerThreadFactory,
	             null, true);
	    }

	    public static ExecutorService newWorkStealingPool() {
	        return new ForkJoinPool
	            (Runtime.getRuntime().availableProcessors(),
	             ForkJoinPool.defaultForkJoinWorkerThreadFactory,
	             null, true);
	    }

	    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
	        return new ThreadPoolExecutor(nThreads, nThreads,
	                                      0L, TimeUnit.MILLISECONDS,
	                                      new LinkedBlockingQueue<Runnable>(),
	                                      threadFactory);
	    }

	    public static ExecutorService newSingleThreadExecutor() {
	        return new FinalizableDelegatedExecutorService
	            (new ThreadPoolExecutor(1, 1,
	                                    0L, TimeUnit.MILLISECONDS,
	                                    new LinkedBlockingQueue<Runnable>()));
	    }

	    public static ExecutorService newSingleThreadExecutor(ThreadFactory threadFactory) {
	        return new FinalizableDelegatedExecutorService
	            (new ThreadPoolExecutor(1, 1,
	                                    0L, TimeUnit.MILLISECONDS,
	                                    new LinkedBlockingQueue<Runnable>(),
	                                    threadFactory));
	    }

	    public static ExecutorService newCachedThreadPool() {
	        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
	                                      60L, TimeUnit.SECONDS,
	                                      new SynchronousQueue<Runnable>());
	    }

	    public static ExecutorService newCachedThreadPool(ThreadFactory threadFactory) {
	        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
	                                      60L, TimeUnit.SECONDS,
	                                      new SynchronousQueue<Runnable>(),
	                                      threadFactory);
	    }

	    public static ScheduledExecutorService newSingleThreadScheduledExecutor() {
	        return new DelegatedScheduledExecutorService
	            (new ScheduledThreadPoolExecutor(1));
	    }

	    public static ScheduledExecutorService newSingleThreadScheduledExecutor(ThreadFactory threadFactory) {
	        return new DelegatedScheduledExecutorService
	            (new ScheduledThreadPoolExecutor(1, threadFactory));
	    }

	    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
	        return new ScheduledThreadPoolExecutor(corePoolSize);
	    }

	    public static ScheduledExecutorService newScheduledThreadPool(
	            int corePoolSize, ThreadFactory threadFactory) {
	        return new ScheduledThreadPoolExecutor(corePoolSize, threadFactory);
	    }

	    public static ExecutorService unconfigurableExecutorService(ExecutorService executor) {
	        if (executor == null)
	            throw new NullPointerException();
	        return new DelegatedExecutorService(executor);
	    }

	    public static ScheduledExecutorService unconfigurableScheduledExecutorService(ScheduledExecutorService executor) {
	        if (executor == null)
	            throw new NullPointerException();
	        return new DelegatedScheduledExecutorService(executor);
	    }

	    public static ThreadFactory defaultThreadFactory() {
	        return new DefaultThreadFactory();
	    }

	    public static ThreadFactory privilegedThreadFactory() {
	        return new PrivilegedThreadFactory();
	    }

	    public static <T> Callable<T> callable(Runnable task, T result) {
	        if (task == null)
	            throw new NullPointerException();
	        return new RunnableAdapter<T>(task, result);
	    }

	    public static Callable<Object> callable(Runnable task) {
	        if (task == null)
	            throw new NullPointerException();
	        return new RunnableAdapter<Object>(task, null);
	    }

	    public static Callable<Object> callable(final PrivilegedAction<?> action) {
	        if (action == null)
	            throw new NullPointerException();
	        return new Callable<Object>() {
	            public Object call() { return action.run(); }};
	    }

	    public static Callable<Object> callable(final PrivilegedExceptionAction<?> action) {
	        if (action == null)
	            throw new NullPointerException();
	        return new Callable<Object>() {
	            public Object call() throws Exception { return action.run(); }};
	    }

	    public static <T> Callable<T> privilegedCallable(Callable<T> callable) {
	        if (callable == null)
	            throw new NullPointerException();
	        return new PrivilegedCallable<T>(callable);
	    }

	    public static <T> Callable<T> privilegedCallableUsingCurrentClassLoader(Callable<T> callable) {
	        if (callable == null)
	            throw new NullPointerException();
	        return new PrivilegedCallableUsingCurrentClassLoader<T>(callable);
	    }
	    
	    static final class RunnableAdapter<T> implements Callable<T> {
	        final Runnable task;
	        final T result;
	        RunnableAdapter(Runnable task, T result) {
	            this.task = task;
	            this.result = result;
	        }
	        public T call() {
	            task.run();
	            return result;
	        }
	    }

	    /**
	     * A callable that runs under established access control settings
	     */
	    static final class PrivilegedCallable<T> implements Callable<T> {
	        private final Callable<T> task;
	        private final AccessControlContext acc;

	        PrivilegedCallable(Callable<T> task) {
	            this.task = task;
	            this.acc = AccessController.getContext();
	        }

	        public T call() throws Exception {
	            try {
	                return AccessController.doPrivileged(
	                    new PrivilegedExceptionAction<T>() {
	                        public T run() throws Exception {
	                            return task.call();
	                        }
	                    }, acc);
	            } catch (PrivilegedActionException e) {
	                throw e.getException();
	            }
	        }
	    }

	    /**
	     * A callable that runs under established access control settings and
	     * current ClassLoader
	     */
	    static final class PrivilegedCallableUsingCurrentClassLoader<T> implements Callable<T> {
	        private final Callable<T> task;
	        private final AccessControlContext acc;
	        private final ClassLoader ccl;

	        PrivilegedCallableUsingCurrentClassLoader(Callable<T> task) {
	            SecurityManager sm = System.getSecurityManager();
	            if (sm != null) {
	                // Calls to getContextClassLoader from this class
	                // never trigger a security check, but we check
	                // whether our callers have this permission anyways.
	                sm.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);

	                // Whether setContextClassLoader turns out to be necessary
	                // or not, we fail fast if permission is not available.
	                sm.checkPermission(new RuntimePermission("setContextClassLoader"));
	            }
	            this.task = task;
	            this.acc = AccessController.getContext();
	            this.ccl = Thread.currentThread().getContextClassLoader();
	        }

	        public T call() throws Exception {
	            try {
	                return AccessController.doPrivileged(
	                    new PrivilegedExceptionAction<T>() {
	                        public T run() throws Exception {
	                            Thread t = Thread.currentThread();
	                            ClassLoader cl = t.getContextClassLoader();
	                            if (ccl == cl) {
	                                return task.call();
	                            } else {
	                                t.setContextClassLoader(ccl);
	                                try {
	                                    return task.call();
	                                } finally {
	                                    t.setContextClassLoader(cl);
	                                }
	                            }
	                        }
	                    }, acc);
	            } catch (PrivilegedActionException e) {
	                throw e.getException();
	            }
	        }
	    }

	    /**
	     * The default thread factory
	     */
	    static class DefaultThreadFactory implements ThreadFactory {
	        private static final AtomicInteger poolNumber = new AtomicInteger(1);
	        private final ThreadGroup group;
	        private final AtomicInteger threadNumber = new AtomicInteger(1);
	        private final String namePrefix;

	        DefaultThreadFactory() {
	            SecurityManager s = System.getSecurityManager();
	            group = (s != null) ? s.getThreadGroup() :
	                                  Thread.currentThread().getThreadGroup();
	            namePrefix = "pool-" +
	                          poolNumber.getAndIncrement() +
	                         "-thread-";
	        }

	        public Thread newThread(Runnable r) {
	            Thread t = new Thread(group, r,
	                                  namePrefix + threadNumber.getAndIncrement(),
	                                  0);
	            if (t.isDaemon())
	                t.setDaemon(false);
	            if (t.getPriority() != Thread.NORM_PRIORITY)
	                t.setPriority(Thread.NORM_PRIORITY);
	            return t;
	        }
	    }

	    /**
	     * Thread factory capturing access control context and class loader
	     */
	    static class PrivilegedThreadFactory extends DefaultThreadFactory {
	        private final AccessControlContext acc;
	        private final ClassLoader ccl;

	        PrivilegedThreadFactory() {
	            super();
	            SecurityManager sm = System.getSecurityManager();
	            if (sm != null) {
	                // Calls to getContextClassLoader from this class
	                // never trigger a security check, but we check
	                // whether our callers have this permission anyways.
	                sm.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);

	                // Fail fast
	                sm.checkPermission(new RuntimePermission("setContextClassLoader"));
	            }
	            this.acc = AccessController.getContext();
	            this.ccl = Thread.currentThread().getContextClassLoader();
	        }

	        public Thread newThread(final Runnable r) {
	            return super.newThread(new Runnable() {
	                public void run() {
	                    AccessController.doPrivileged(new PrivilegedAction<Void>() {
	                        public Void run() {
	                            Thread.currentThread().setContextClassLoader(ccl);
	                            r.run();
	                            return null;
	                        }
	                    }, acc);
	                }
	            });
	        }
	    }

	    /**
	     * A wrapper class that exposes only the ExecutorService methods
	     * of an ExecutorService implementation.
	     */
	    static class DelegatedExecutorService extends AbstractExecutorService {
	        private final ExecutorService e;
	        DelegatedExecutorService(ExecutorService executor) { e = executor; }
	        public void execute(Runnable command) { e.execute(command); }
	        public void shutdown() { e.shutdown(); }
	        public List<Runnable> shutdownNow() { return e.shutdownNow(); }
	        public boolean isShutdown() { return e.isShutdown(); }
	        public boolean isTerminated() { return e.isTerminated(); }
	        public boolean awaitTermination(long timeout, TimeUnit unit)
	            throws InterruptedException {
	            return e.awaitTermination(timeout, unit);
	        }
	        public Future<?> submit(Runnable task) {
	            return e.submit(task);
	        }
	        public <T> Future<T> submit(Callable<T> task) {
	            return e.submit(task);
	        }
	        public <T> Future<T> submit(Runnable task, T result) {
	            return e.submit(task, result);
	        }
	        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
	            throws InterruptedException {
	            return e.invokeAll(tasks);
	        }
	        public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,
	                                             long timeout, TimeUnit unit)
	            throws InterruptedException {
	            return e.invokeAll(tasks, timeout, unit);
	        }
	        public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
	            throws InterruptedException, ExecutionException {
	            return e.invokeAny(tasks);
	        }
	        public <T> T invokeAny(Collection<? extends Callable<T>> tasks,
	                               long timeout, TimeUnit unit)
	            throws InterruptedException, ExecutionException, TimeoutException {
	            return e.invokeAny(tasks, timeout, unit);
	        }
	    }

	    static class FinalizableDelegatedExecutorService
	        extends DelegatedExecutorService {
	        FinalizableDelegatedExecutorService(ExecutorService executor) {
	            super(executor);
	        }
	        protected void finalize() {
	            super.shutdown();
	        }
	    }

	    /**
	     * A wrapper class that exposes only the ScheduledExecutorService
	     * methods of a ScheduledExecutorService implementation.
	     */
	    static class DelegatedScheduledExecutorService
	            extends DelegatedExecutorService
	            implements ScheduledExecutorService {
	        private final ScheduledExecutorService e;
	        DelegatedScheduledExecutorService(ScheduledExecutorService executor) {
	            super(executor);
	            e = executor;
	        }
	        public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
	            return e.schedule(command, delay, unit);
	        }
	        public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
	            return e.schedule(callable, delay, unit);
	        }
	        public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
	            return e.scheduleAtFixedRate(command, initialDelay, period, unit);
	        }
	        public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
	            return e.scheduleWithFixedDelay(command, initialDelay, delay, unit);
	        }
	    }
	
}
