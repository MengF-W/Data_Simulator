package com.analyzer.datasimulator.configuration;

import com.analyzer.datasimulator.messenger.configuration.AsyncConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AsyncConfig.class)
public class AsyncConfigTest {

    @Autowired
    private Environment environment;

    @Autowired
    private AsyncConfig asyncConfig;

    @Test
    public void testAsyncConfigurationIsValid() {

        assertNotNull(asyncConfig);
        assertEquals(Integer.parseInt(environment.getProperty("thread.CorePoolSize")),asyncConfig.initThreadPoolTaskExecutor().getCorePoolSize());
        assertEquals(Integer.parseInt(environment.getProperty("thread.MaxPoolSize")),asyncConfig.initThreadPoolTaskExecutor().getMaxPoolSize());
        assertEquals(Integer.parseInt(environment.getProperty("thread.QueueCapacity")),asyncConfig.initThreadPoolTaskExecutor().getQueueCapacity());
        assertEquals(asyncConfig.initThreadPoolTaskExecutor().getThreadNamePrefix(),environment.getProperty("thread.ThreadNamePrefix"));

    }
}
