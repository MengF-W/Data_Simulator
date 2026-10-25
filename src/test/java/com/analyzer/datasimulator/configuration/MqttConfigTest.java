package com.analyzer.datasimulator.configuration;

import com.analyzer.datasimulator.camera.configuration.CameraConfigurator;
import com.analyzer.datasimulator.camera.controller.CameraController;
import com.analyzer.datasimulator.camera.controller.CameraFxController;
import com.analyzer.datasimulator.messenger.configuration.MqttConfig;
import com.analyzer.datasimulator.messenger.processor.DeviceMessageProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MqttConfig.class)
@TestPropertySource(properties = {"running.mode=Testing"})
@MockBeans({@MockBean(CameraController.class),@MockBean(CameraFxController.class),@MockBean(CameraConfigurator.class),@MockBean(DeviceMessageProcessor.class)})
@EnableAutoConfiguration(exclude= {DataSourceAutoConfiguration.class,})
public class MqttConfigTest {

    @Autowired
    MqttConfig mqttConfig;

    @Autowired
    Environment environment;
    @Test
    public void testMqttConfigurationIsValid() {
        assertNotNull(mqttConfig);
        assertTrue(mqttConfig.initMqttClient().getServerURI().contains(Objects.requireNonNull(environment.getProperty("mqtt.server.address"))));

    }
}
