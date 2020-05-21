package tool.xfy9326.course.tool;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadScheduler {
    private static final Handler mainHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    private static final ExecutorService ioExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
    private static final ExecutorService defaultExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

    private final Lifecycle lifecycle;

    public ThreadScheduler(@NonNull Lifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    public void post(@NonNull final ExecutorType threadType, @NonNull final Runnable runnable) {
        LifecycleObserver observer;
        switch (threadType) {
            case MAIN:
                mainHandler.post(runnable);
                observer = new SchedulerLifeCycleObserver() {
                    @Override
                    public void onThreadCancel() {
                        mainHandler.removeCallbacks(runnable);
                    }
                };
                break;
            case DEFAULT:
                Future<?> defaultFuture = defaultExecutor.submit(new SimpleRunnable(runnable));
                observer = new FutureSchedulerLifeCycleObserver(defaultFuture);
                break;
            case IO:
                Future<?> ioFuture = ioExecutor.submit(new SimpleRunnable(runnable));
                observer = new FutureSchedulerLifeCycleObserver(ioFuture);
                break;
            default:
                throw new IllegalArgumentException("Unknown ExecutorType! " + threadType.name());
        }
        lifecycle.addObserver(observer);
    }

    public enum ExecutorType {
        MAIN,
        DEFAULT,
        IO
    }

    private static class FutureSchedulerLifeCycleObserver extends SchedulerLifeCycleObserver {
        private final Future<?> future;

        FutureSchedulerLifeCycleObserver(@NonNull Future<?> future) {
            this.future = future;
        }

        @Override
        public void onThreadCancel() {
            if (!future.isDone() && !future.isCancelled()) {
                future.cancel(true);
            }
        }
    }

    private static abstract class SchedulerLifeCycleObserver implements LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        protected abstract void onThreadCancel();
    }
}
