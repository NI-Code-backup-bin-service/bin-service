/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.fake;

import org.junit.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class FakeBinServiceTest {

    @Test
    public void shouldStartUpFakeServiceSuccessfully() {
        ConfigurableApplicationContext ctx = new SpringApplicationBuilder(FakeBinService.class)
                .web(WebApplicationType.SERVLET)
                .properties("spring.config.name:fake-bin-service")
                .addCommandLineProperties(true)
                .run();
    }
}