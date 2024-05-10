/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.rest;

import ae.network.gateway.bin.domain.BinInfoQueryOperations;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DomainObjectResourceTest {

    @Rule
    public JerseyTestRule jerseyTestRule = new JerseyTestRule(resourceConfig(), "http://config.ni/");

    @Mock
    private BinInfoQueryOperations binInfoQueryOperations;

    private ResourceConfig resourceConfig() {
        MockitoAnnotations.initMocks(this);
        return new ResourceConfiguration()
            .register(new BinResource(binInfoQueryOperations));
    }
}
