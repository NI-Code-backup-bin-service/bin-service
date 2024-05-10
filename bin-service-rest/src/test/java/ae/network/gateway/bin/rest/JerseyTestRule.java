/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.rest;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.inmemory.InMemoryTestContainerFactory;
import org.junit.rules.ExternalResource;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Application;

import java.net.URI;

public class JerseyTestRule extends ExternalResource {

    static {
        // Get JerseyTest to use SLF4J instead of JUL
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private final JerseyTest jerseyTest;

    public JerseyTestRule(final ResourceConfig config, String baseUri) {

        config
            .property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_SERVER, LoggingFeature.Verbosity.PAYLOAD_ANY)
            .property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "INFO");

        this.jerseyTest = new JerseyTest(new InMemoryTestContainerFactory()) {
            @Override
            protected Application configure() {
                return config;
            }

            @Override
            protected URI getBaseUri() {
                return URI.create(baseUri);
            }
        };
    }

    @Override
    public void before() throws Throwable {
        this.jerseyTest.setUp();
    }

    @Override
    public void after() {
        try {
            this.jerseyTest.tearDown();
        } catch (Exception exception) {
            throw new RuntimeException("Failed to tear down JerseyTest.", exception);
        }
    }

    public Client client() {
        return this.jerseyTest.client();
    }
}
