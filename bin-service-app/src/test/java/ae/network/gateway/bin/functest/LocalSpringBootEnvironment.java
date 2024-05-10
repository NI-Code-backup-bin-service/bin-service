/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.functest;

import ae.network.gateway.bin.Application;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import java.net.URI;

public class LocalSpringBootEnvironment {

    public static final class SpringHolder {

        public static ApplicationContext INSTANCE = instance();

        private static ApplicationContext instance() {

            return new SpringApplicationBuilder(Application.class)
                    .profiles("test")
                    .build()
                    .run();
        }
    }

    private int port;

    public void start() {
        ApplicationContext instance = SpringHolder.INSTANCE;
        port = instance.getEnvironment().getProperty("local.server.port", Integer.class);

    }

    public void stop() {


    }

    public URI getBaseUri() {
        return URI.create("http://localhost:" + port);
    }
}
