/*
 * Copyright (c) 2018 Network International.
 * The copyright notice above does not evidence any
 * actual or intended publication of such source code.
 */

package ae.network.gateway.bin.rest;

import ae.network.gateway.bin.rest.providers.ObjectMapperContextResolver;
import ae.network.gateway.bin.rest.providers.UnknownEntityExceptionMapper;
import ae.network.gateway.bin.rest.providers.UnprocessableEntityExceptionMapper;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/")
public class ResourceConfiguration extends ResourceConfig {

    public ResourceConfiguration() {
        register(ObjectMapperContextResolver.class);
        register(UnknownEntityExceptionMapper.class);
        register(UnprocessableEntityExceptionMapper.class);
    }
}
