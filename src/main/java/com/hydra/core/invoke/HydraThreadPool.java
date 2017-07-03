package com.hydra.core.invoke;


import java.util.concurrent.*;

public class HydraThreadPool {

    public static Executor getExecutor(int threads, int queues) {
        String name = "HydraThreadPool";
        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS,
                queues == 0 ? new SynchronousQueue<Runnable>()
                        : (queues < 0 ? new LinkedBlockingQueue<Runnable>()
                        : new LinkedBlockingQueue<Runnable>(queues)),
                new HydraThreadFactory(name, true), new AbortPolicyWithReport(name));
    }
}
