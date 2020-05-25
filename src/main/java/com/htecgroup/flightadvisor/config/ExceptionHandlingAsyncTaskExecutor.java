package com.htecgroup.flightadvisor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.AsyncTaskExecutor;

import javax.annotation.Nonnull;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Slf4j
public class ExceptionHandlingAsyncTaskExecutor implements AsyncTaskExecutor, InitializingBean, DisposableBean {

    static final String EXCEPTION_MESSAGE = "Caught async exception";
    private final AsyncTaskExecutor executor;

    public ExceptionHandlingAsyncTaskExecutor(AsyncTaskExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(@Nonnull Runnable task) {
        executor.execute(createWrappedRunnable(task));
    }

    @Override
    public void execute(@Nonnull Runnable task, long startTimeout) {
        executor.execute(createWrappedRunnable(task), startTimeout);
    }

    private <T> Callable<T> createCallable(final Callable<T> task) {
        return () -> {
            try {
                return task.call();
            } catch (Exception e) {
                handle(e);
                throw e;
            }
        };
    }

    private Runnable createWrappedRunnable(final Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                handle(e);
            }
        };
    }

    protected void handle(Exception e) {
        log.error(EXCEPTION_MESSAGE, e);
    }

    @Override
    @Nonnull
    public Future<?> submit(@Nonnull Runnable task) {
        return executor.submit(createWrappedRunnable(task));
    }

    @Override
    @Nonnull
    public <T> Future<T> submit(@Nonnull Callable<T> task) {
        return executor.submit(createCallable(task));
    }

    @Override
    public void destroy() throws Exception {
        if (executor instanceof DisposableBean) {
            DisposableBean bean = (DisposableBean) executor;
            bean.destroy();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (executor instanceof InitializingBean) {
            InitializingBean bean = (InitializingBean) executor;
            bean.afterPropertiesSet();
        }
    }
}
