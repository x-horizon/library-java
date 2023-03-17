/**
 * Copyright © 2016-2021 The Thingsboard Authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.srd.itcp.sugar.tool.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;
import java.util.concurrent.ThreadFactory;

/**
 * thread tool
 *
 * @author wjm
 * @since 2023-03-16 18:57:12
 */
public class Threads {

    /**
     * create {@link ThreadFactory} with name
     *
     * @param name thread pool name
     * @return {@link ThreadFactory}
     */
    public static ThreadFactory newThreadFactory(String name) {
        return new ThreadFactoryBuilder().setNameFormat(name).build();
    }

    /**
     * create {@link ForkJoinPool.ForkJoinWorkerThreadFactory} with name
     *
     * @param name thread pool name
     * @return {@link ForkJoinPool.ForkJoinWorkerThreadFactory}
     */
    public static ForkJoinPool.ForkJoinWorkerThreadFactory newForkJoinWorkerThreadFactory(String name) {
        return pool -> {
            ForkJoinWorkerThread forkJoinWorkerThread = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
            forkJoinWorkerThread.setName(name);
            return forkJoinWorkerThread;
        };
    }

    /**
     * create {@link ForkJoinPool} with name
     *
     * @param parallelism see {@link ForkJoinPool#ForkJoinPool(int, ForkJoinPool.ForkJoinWorkerThreadFactory, Thread.UncaughtExceptionHandler, boolean)}
     * @param name        thread pool name
     * @return {@link ForkJoinPool}
     */
    public static ExecutorService newWorkStealingPool(int parallelism, String name) {
        return new ForkJoinPool(parallelism, newForkJoinWorkerThreadFactory(name), null, true);
    }

}
