package com.adamsmods.adamsarsplus.entities.ai.pathfinding;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.fml.LogicalSide;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;


/**
 * Static class the handles all the Pathfinding.
 */
public final class Pathfinding {
    private static final BlockingQueue<Runnable> jobQueue = new LinkedBlockingDeque<>();
    private static ThreadPoolExecutor executor;

    private Pathfinding() {
        //Hides default constructor.
    }

    public static boolean isDebug() {
        return false;
    }

    /**
     * Creates a new thread pool for pathfinding jobs
     *
     * @return the threadpool executor.
     */
    public static ThreadPoolExecutor getExecutor() {
        if (executor == null) {
            executor = new ThreadPoolExecutor(1, 3, 10, TimeUnit.SECONDS, jobQueue, new IafThreadFactory());
        }
        return executor;
    }

    /**
     * Ice and Fire specific thread factory.
     */
    public static class IafThreadFactory implements ThreadFactory {
        /**
         * Ongoing thread IDs.
         */
        public static int id;

        @Override
        public Thread newThread(final @NotNull Runnable runnable) throws RuntimeException {
            BlockableEventLoop<?> workqueue = LogicalSidedProvider.WORKQUEUE.get(LogicalSide.SERVER);
            ClassLoader classLoader;
            if (workqueue.isSameThread()) {
                classLoader = Thread.currentThread().getContextClassLoader();
            } else if (workqueue instanceof MinecraftServer server){
                classLoader = server.getRunningThread().getContextClassLoader();
            } else {
                classLoader = CompletableFuture.supplyAsync(() -> Thread.currentThread().getContextClassLoader(), workqueue).orTimeout(10, TimeUnit.SECONDS).exceptionally((ex)-> {
                    throw new RuntimeException(String.format("Couldn't join threads within timeout range. Tried joining '%s' on '%s'", Thread.currentThread().getName(), workqueue.name()));
                }).join();
            }
            final Thread thread = new Thread(runnable, "Ice and Fire Pathfinding Worker #" + (id++));
            thread.setDaemon(true);
            thread.setPriority(Thread.MAX_PRIORITY);
            if (thread.getContextClassLoader() != classLoader) {

                thread.setContextClassLoader(classLoader);
            }

            return thread;
        }
    }
}
