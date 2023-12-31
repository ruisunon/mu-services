package com.rycoding.ecommerce.configurataion;

import com.rycoding.ecommerce.exception.AsyncExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@RequiredArgsConstructor
public class AsyncConfig extends AsyncConfigurerSupport {
  final private AsyncExceptionHandler asyncExceptionHandler;

  @Override
  public Executor getAsyncExecutor() {
      ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
      taskExecutor.setCorePoolSize(5);
      taskExecutor.setMaxPoolSize(10);
      taskExecutor.setQueueCapacity(500);
      taskExecutor.setThreadNamePrefix("Async-example-thread-");
      taskExecutor.initialize();
      return taskExecutor;
  }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return asyncExceptionHandler;
    }
}
