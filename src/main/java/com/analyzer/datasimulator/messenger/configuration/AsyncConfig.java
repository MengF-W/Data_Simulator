package com.analyzer.datasimulator.messenger.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync(proxyTargetClass = true)
public class AsyncConfig {

    @Autowired
    private Environment environment;
    @Bean
    public ThreadPoolTaskExecutor initThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(Integer.parseInt(environment.getProperty("thread.CorePoolSize")));
        threadPoolTaskExecutor.setMaxPoolSize(Integer.parseInt(environment.getProperty("thread.MaxPoolSize")));;
        threadPoolTaskExecutor.setQueueCapacity(Integer.parseInt(environment.getProperty("thread.QueueCapacity")));
        threadPoolTaskExecutor.setThreadNamePrefix(environment.getProperty("thread.ThreadNamePrefix"));
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}

