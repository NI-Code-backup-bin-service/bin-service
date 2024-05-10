/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.fake;

import ae.network.commons.monitoring.FakeActuator;
import ae.network.gateway.bin.domain.BinInfoQueryOperations;
import ae.network.gateway.bin.rest.ResourceConfiguration;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.ws.rs.Path;

import static org.mockito.Mockito.mock;

@Configuration
@ComponentScan("ae.network.gateway.bin.rest")
@Import({JacksonAutoConfiguration.class,
        ServletWebServerFactoryAutoConfiguration.class,
})
public class FakeBinService {

    public static void main(String[] args) {
        new SpringApplicationBuilder(FakeBinService.class)
                .web(WebApplicationType.SERVLET)
                .properties("spring.config.name:fake-bin-service")
                .addCommandLineProperties(true)
                .run();
    }

    @Bean
    FilterRegistrationBean<ServletContainer> jersey(ApplicationContext context) {
        ResourceConfig resourceConfig = new ResourceConfiguration();
        resourceConfig.property(ServletProperties.FILTER_FORWARD_ON_404, true);

        context.getBeansWithAnnotation(Path.class)
                .values()
                .forEach(resourceConfig::register);
        ServletContainer jersey = new ServletContainer(resourceConfig);
        return new FilterRegistrationBean<>(jersey);
    }

    @Bean
    BinInfoQueryOperations binInfoQueryOperations() {
        return mock(BinInfoQueryOperations.class);
    }

    @Bean
    FakeActuator fakeActuator() {
        return mock(FakeActuator.class);
    }
}
